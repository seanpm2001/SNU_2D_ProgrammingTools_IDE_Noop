package noop.interpreter.config;

import java.util.List;

import static java.util.Collections.emptyList;

/**
 * Test-friendly way to create interpreter options without command-line parsing.
 * @author Alex Eagle (alexeagle@google.com)
 */
public class OptionsBuilder {
  private Integer entryPoint = 1;
  private String mainLib = "Default from OptionsBuilder";
  private List<String> libraryPaths = emptyList();
  private boolean verbose;

  public InterpreterOptions build() {
    return new ConfigurableOptions();
  }

  public class ConfigurableOptions implements InterpreterOptions {

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
}
