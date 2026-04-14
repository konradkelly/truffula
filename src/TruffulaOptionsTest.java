import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.io.FileNotFoundException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

public class TruffulaOptionsTest {

  @Test
  void testValidDirectoryIsSet(@TempDir File tempDir) throws FileNotFoundException {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String directoryPath = directory.getAbsolutePath();
    String[] args = {"-h", "-nc", directoryPath};

    TruffulaOptions options = new TruffulaOptions(args);

    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertFalse(options.isUseColor());
  }

  @Test
  void testNoArgs() {
    String[] args = {};

    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testNonExistentDirectory() {
    String[] args = {"/nonexistent/fake/path"};

    assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testPathIsFileNotDirectory(@TempDir File tempDir) throws Exception {
    File file = new File(tempDir, "notAFolder.txt");
    file.createNewFile();
    String[] args = {file.getAbsolutePath()};

    assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testUndefinedFlag(@TempDir File tempDir) {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String[] args = {"-ls", directory.getAbsolutePath()};

    assertThrows(IllegalArgumentException.class, () -> new TruffulaOptions(args));
  }

  @Test
  void testPathNoFlags(@TempDir File tempDir) throws FileNotFoundException {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String[] args = {directory.getAbsolutePath()};

    TruffulaOptions options = new TruffulaOptions(args);

    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertFalse(options.isShowHidden());
    assertTrue(options.isUseColor());
  }

  @Test
  void testHiddenFlagOnly(@TempDir File tempDir) throws FileNotFoundException {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String[] args = {"-h", directory.getAbsolutePath()};

    TruffulaOptions options = new TruffulaOptions(args);

    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertTrue(options.isUseColor());
  }

  @Test
  void testNoColorFlagOnly(@TempDir File tempDir) throws FileNotFoundException {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String[] args = {"-nc", directory.getAbsolutePath()};

    TruffulaOptions options = new TruffulaOptions(args);

    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertFalse(options.isShowHidden());
    assertFalse(options.isUseColor());
  }

  @Test
  void testBothFlagsReversedOrder(@TempDir File tempDir) throws FileNotFoundException {
    File directory = new File(tempDir, "subfolder");
    directory.mkdir();
    String[] args = {"-nc", "-h", directory.getAbsolutePath()};

    TruffulaOptions options = new TruffulaOptions(args);

    assertEquals(directory.getAbsolutePath(), options.getRoot().getAbsolutePath());
    assertTrue(options.isShowHidden());
    assertFalse(options.isUseColor());
  }
}
