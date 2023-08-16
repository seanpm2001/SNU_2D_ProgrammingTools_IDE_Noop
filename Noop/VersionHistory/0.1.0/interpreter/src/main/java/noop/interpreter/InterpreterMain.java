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

package noop.interpreter;

import com.google.inject.Guice;
import noop.interpreter.config.CommandLineOptions;
import noop.interpreter.config.InterpreterModule;
import org.kohsuke.args4j.CmdLineException;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class InterpreterMain {
  public static int exitCodeForTesting;
  private static boolean testing = false;

  public static void disableSystemExitForTesting() {
    testing = true;
  }

  public static void main(String[] args) throws Exception {
    try {
      CommandLineOptions options = CommandLineOptions.fromCmdLineArgs(args);
      exit(Guice.createInjector(new InterpreterModule(options))
          .getInstance(Interpreter.class)
          .run());
    } catch (CmdLineException e) {
      exit(1);
    }
  }

  private static void exit(int exitCode) {
    if (testing) {
      exitCodeForTesting = exitCode;
    } else {
      System.exit(exitCode);
    }
  }
}
