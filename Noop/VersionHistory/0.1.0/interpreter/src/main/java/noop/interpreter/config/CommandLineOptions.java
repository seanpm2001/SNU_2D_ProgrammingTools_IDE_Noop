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

package noop.interpreter.config;

import org.kohsuke.args4j.Argument;
import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.kohsuke.args4j.Option;

import java.util.List;

/**
 * @author alexeagle@google.com (Alex Eagle)
 */
public class CommandLineOptions implements InterpreterOptions {

  @Argument(metaVar = "mainLib", required = true, index = 0,
            usage = "Library containing the entry point")
  private String mainLib;

  @Argument(metaVar = "entryPoint", required = true, index = 1,
            usage = "Entry point of the application to execute")
  private Integer entryPoint;

  @Option(name = "-lib", aliases = "--library",
          usage = "A relative path to a library to load. May be used multiple times.")
  private List<String> libraryPaths;

  @Option(name = "-v", aliases = "--verbose",
          usage = "Whether to print the graph as the interpreter visits it")
  private boolean verbose;

  public static CommandLineOptions fromCmdLineArgs(String[] args) throws CmdLineException {
    CommandLineOptions options = new CommandLineOptions();
    CmdLineParser parser = new CmdLineParser(options);
    try {
      parser.parseArgument(args);
    } catch (CmdLineException e) {
      System.err.printf("Invalid command line: %s\n\nUsage:\n", e.getMessage());
      parser.setUsageWidth(100);
      parser.printUsage(System.err);
      throw e;
    }
    return options;
  }

  @Override
  public Integer getEntryPoint() {
    return entryPoint;
  }

  @Override
  public String getMainLib() {
    return mainLib;
  }

  @Override
  public List<String> getLibraryPaths() {
    return libraryPaths;
  }

  @Override
  public boolean isVerbose() {
    return verbose;
  }
}
