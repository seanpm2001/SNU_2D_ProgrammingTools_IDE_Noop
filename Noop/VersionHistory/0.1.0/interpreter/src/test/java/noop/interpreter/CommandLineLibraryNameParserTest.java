package noop.interpreter;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * @author Alex Eagle (alexeagle@google.com)
 */
public class CommandLineLibraryNameParserTest {
  @Test public void shouldParseLibraryName() {
    CommandLineLibraryNameParser parser =
        new CommandLineLibraryNameParser("com.google.Noop/foo").invoke();
    assertEquals("com.google", parser.getProject().getNamespace());
    assertEquals("Noop", parser.getProject().getName());
    assertEquals("foo", parser.getLibraryName());

  }
}
