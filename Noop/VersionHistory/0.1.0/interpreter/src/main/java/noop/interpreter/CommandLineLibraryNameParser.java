package noop.interpreter;

import noop.model.Project;

/**
 * When specifying a library on the command line, a string-valued representation is needed.
 * This Parser looks for a format such as com.google.Noop/lang and provides the
 * Project and library name for the lang library.
 * @author Alex Eagle (alexeagle@google.com)
 */
class CommandLineLibraryNameParser {
  private String libraryPath;
  private String libraryName;
  private Project project;

  public CommandLineLibraryNameParser(String libraryPath) {
    this.libraryPath = libraryPath;
  }

  public String getLibraryName() {
    return libraryName;
  }

  public Project getProject() {
    return project;
  }

  public CommandLineLibraryNameParser invoke() {
    String[] parts = libraryPath.split("/");
    String project = parts[0];
    libraryName = parts[1];
    String namespace = project.substring(0, project.lastIndexOf("."));
    String projectName = project.substring(project.lastIndexOf(".") + 1);
    this.project = new Project(projectName, namespace, "");
    return this;
  }
}
