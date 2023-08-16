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

package noop.graph;

import com.thoughtworks.xstream.XStream;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.Arrays;

import noop.model.Project;
import noop.persistence.LibraryRepository;
import noop.persistence.ModelSerializer;
import noop.persistence.ModelSerializer.SerializationFormat;
import static noop.persistence.ModelSerializer.SerializationFormat.DOT;
import static noop.persistence.ModelSerializer.SerializationFormat.TXT;
import static noop.persistence.ModelSerializer.SerializationFormat.XML;
import noop.stdlib.StandardLibraryBuilder;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class DumpExamplesMain {
  private final File outDir;

  public DumpExamplesMain(File outDir) {
    this.outDir = outDir;
  }

  public static void main(String[] args) throws FileNotFoundException {
    new DumpExamplesMain(new File(args[0])).run();
  }

  public void run() throws FileNotFoundException {
    StandardLibraryBuilder stdLib = new StandardLibraryBuilder();

    for (Example example : Arrays.asList(
        new Example(stdLib) {
          @Override
          public void createProgram(Controller controller) {}
        },
        new ArithmeticExample(stdLib),
        new HelloWorldExample(stdLib),
        new ControlFlowExample(stdLib))) {
      Workspace workspace = new Workspace();

      Controller controller = new Controller(workspace, new VertexCreatingVisitor());
      stdLib.build(controller);

      example.createProgram(controller);
      for (SerializationFormat serializationFormat : Arrays.asList(DOT, TXT)) {
        String outFileName = example.getClass().getName() + "."
            + serializationFormat.name().toLowerCase();
        PrintStream output = new PrintStream(new FileOutputStream(new File(outDir, outFileName)));
        new ModelSerializer(serializationFormat, new XStream()).write(workspace, output);
      }

      LibraryRepository repository = new LibraryRepository(outDir,
          new ModelSerializer(XML, new XStream()));
      for (Project project : workspace.getProjects()) {
        repository.save(project);
      }
    }
  }
}
