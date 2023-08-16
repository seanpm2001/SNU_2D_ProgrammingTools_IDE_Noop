/*
 * Copyright 2010 Google Inc.
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

package noop.persistence;

import com.google.inject.Inject;

import com.thoughtworks.xstream.XStream;

import java.io.PrintStream;
import java.io.Reader;

import noop.graph.DotGraphPrintingVisitor;
import noop.graph.OutlinePrintingVisitor;
import noop.model.LanguageElement;
import noop.model.Library;

/**
 * Writes the content of a language element in one of a few possible formats.
 * Reads a library back from the XML format only.
 * TODO: a "SOURCE" format would allow text-based editing of noop programs
 * @author alexeagle@google.com (Alex Eagle)
 */
public class ModelSerializer {
  private final SerializationFormat serializationFormat;
  private final XStream xStream;

  @Inject
  public ModelSerializer(SerializationFormat serializationFormat, XStream xStream) {
    this.serializationFormat = serializationFormat;
    this.xStream = xStream;
  }

  public enum SerializationFormat {
    TXT, XML, DOT;
  }
  public void write(LanguageElement element, PrintStream out) {
    switch (serializationFormat) {
      case DOT:
        DotGraphPrintingVisitor visitor = new DotGraphPrintingVisitor(out);
        visitor.enter(element);
        element.accept(visitor);
        visitor.leave(element);
        break;
      case TXT:
        OutlinePrintingVisitor v = new OutlinePrintingVisitor(out);
        v.enter(element);
        element.accept(v);
        v.leave(element);
        break;
      case XML:
        out.append(xStream.toXML(element));
        break;
      default:
        throw new RuntimeException("unknown output type " + serializationFormat);
    }
  }

  public Library read(Reader libraryFile) {
    return (Library) xStream.fromXML(libraryFile);    
  }
}
