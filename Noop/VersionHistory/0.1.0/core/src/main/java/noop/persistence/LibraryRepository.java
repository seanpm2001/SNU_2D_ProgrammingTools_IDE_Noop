package noop.persistence;

import com.google.inject.Inject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.PrintStream;

import noop.model.Library;
import noop.model.Project;

/**
 * Saves and loads library objects from local disk.
 * @author Alex Eagle (alexeagle@google.com)
 */
public class LibraryRepository {
  private final File xmlDiskCache;
  private final ModelSerializer modelSerializer;

  @Inject
  public LibraryRepository(@XmlDiskCache File xmlDiskCache, ModelSerializer modelSerializer) {
    this.xmlDiskCache = xmlDiskCache;
    this.modelSerializer = modelSerializer;
  }

  public void save(Project project) {
    for (Library library : project.getLibraries()) {
      File outFile = locateLibrary(project, library.name);
      try {
        PrintStream stream = new PrintStream(new FileOutputStream(outFile));
        modelSerializer.write(library, stream);
      } catch (FileNotFoundException e) {
        throw new RuntimeException("Failed to write to disk", e);
      }
    }
  }

  public Library load(Project project, String libraryName) {
    try {
      return modelSerializer.read(new FileReader(locateLibrary(project, libraryName)));
    } catch (FileNotFoundException e) {
      throw new RuntimeException("Library file not found", e);
    }
  }

  private File locateLibrary(Project project, String libraryName) {
    File projectDir = new File(new File(xmlDiskCache, project.getNamespace()), project.getName());
    projectDir.mkdirs();
    File outFile = new File(projectDir, libraryName + ".xml");
    return outFile;
  }
}
