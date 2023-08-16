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
package noop.grammar

import org.scalatest.Spec
import org.scalatest.matchers.ShouldMatchers
import java.io.{FileInputStream, File}

class ParseExamplesTest extends Spec with ShouldMatchers {

  val parser = new Parser();

  describe("the parser") {
    it("should parse all the .noop files found under the /examples directory") {
      val examplesDir = new File(new File(System.getProperty("user.dir")), "examples");
      parseAllFilesInDirectory(examplesDir);
    }
  }

  def parseAllFilesInDirectory(dir: File): Unit = {
    for (file: File <- dir.listFiles) {
      if (file.isDirectory) {
        parseAllFilesInDirectory(file);
      } else {
        if (file.getName.endsWith(".noop")) {
          try {
            parser.parseFile(new FileInputStream(file));
          } catch {
            case e: ParseException => fail("failed to parse " + file.getAbsolutePath());
          }
        }
      }
    }
  }
}