/**
 * Copyright 2009 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package noop.interpreter;

import collection.mutable.Map
import java.io.{InputStream, FileInputStream, File}
import org.slf4j.LoggerFactory;

import noop.model.{Parameter, ClassDefinition};
import noop.grammar.{ParseException, Parser};

/**
 * @author alexeagle@google.com (Alex Eagle)
 * @author tocman@gmail.com (Jeremie Lenfant-Engelmann)
 */
class SourceFileClassLoader(parser: Parser, srcPaths: List[String]) extends ClassLoader with
    ClassSearch {
  val logger = LoggerFactory.getLogger(classOf[SourceFileClassLoader]);
  val cache = Map.empty[String, ClassDefinition];

  def getClassDefinition(file: File): ClassDefinition = {
    try {
      getClassDefinition(new FileInputStream(file));
    } catch {
      case ex: ParseException =>
        throw new ParseException("Failed to parse " + file.getAbsolutePath());
    }
  }

  def getClassDefinition(stream: InputStream): ClassDefinition = parser.file(stream).classDef;

  def findClass(className: String): ClassDefinition = {
    if (cache.contains(className)) {
      return cache(className);
    }
    val parts = className.split("\\.");
    val expectedFile = parts.last + ".noop";
    val relativePath = parts.take(parts.size - 1).mkString(File.separator);

    val classDef: ClassDefinition = searchInClasspath(relativePath, expectedFile) match {
      case Some(c) => c;
      case None => searchInFilesystem(relativePath, expectedFile) match {
        case Some(c) => c;
        case None => throw new ClassNotFoundException("Could not find class: " + className);
      }
    }

    return postProcess(classDef, className);
  }

  /**
   * We want the AST produced by the ANTLR tree parser to be free from interpreter specific stuff.
   * But, the AST is in a raw form, so we fill in some details to make interpreting easier.
   */
  def postProcess(classDef: ClassDefinition, className: String): ClassDefinition = {
    if (classDef.namespace == "") {
      val parts = className.split("\\.");
      classDef.namespace = parts.take(parts.size - 1).mkString(".");
    }
    return classDef;
  }

  def searchInClasspath(relativePath: String, expectedFile: String): Option[ClassDefinition] = {
    val locationInClasspath = String.format("/%s/%s", relativePath, expectedFile);
    val stream = getClass().getResourceAsStream(locationInClasspath);
    if (stream != null) {
      return Some(getClassDefinition(stream));
    }
    logger.info("Class {} not found in classpath", locationInClasspath);
    return None;
  }

  def searchInFilesystem(relativePath: String, expectedFile: String): Option[ClassDefinition] = {
    for (path <- srcPaths) {
      val dir = new File(path, relativePath);
      if (!dir.isDirectory()) {
        throw new RuntimeException("Wrong srcPath given: " + dir + " is not a directory");
      }
      val files = dir.listFiles();

      files.find(f => f.getName() == expectedFile) match {
        case Some(file) => return Some(getClassDefinition(file));
        case None => // will try in next directory
      }
    }
    return None;
  }

  def addNativeClass(name: String, classDef: ClassDefinition) = {
    cache += Pair(name, classDef);
  }

  def eachClass(f: ClassDefinition => Unit) = {
    for (path <- srcPaths) {
      var srcRoot = new File(path);
      eachClassInPath(srcRoot, "", f);
    }
  }

  def eachClassInPath(dir: File, relativePath: String, f: ClassDefinition => Unit): Unit = {
    for(file: File <- dir.listFiles()) {
      if (file.isDirectory()) {
        val newRelativePath = relativePath + file.getName() + ".";
        eachClassInPath(file, newRelativePath, f);
      } else if (file.getName().endsWith(".noop")) {
        val classDef = getClassDefinition(file)
        val className = relativePath + file.getName().substring(0, file.getName().length - 5);
        f.apply(postProcess(classDef, className));
      }
    }
  }
}
