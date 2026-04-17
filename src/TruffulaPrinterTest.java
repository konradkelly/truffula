import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class TruffulaPrinterTest {

    /**
     * Checks if the current operating system is Windows.
     *
     * This method reads the "os.name" system property and checks whether it
     * contains the substring "win", which indicates a Windows-based OS.
     * 
     * You do not need to modify this method.
     *
     * @return true if the OS is Windows, false otherwise
     */
    private static boolean isWindows() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("win");
    }

    /**
     * Creates a hidden file in the specified parent folder.
     * 
     * The filename MUST start with a dot (.).
     *
     * On Unix-like systems, files prefixed with a dot (.) are treated as hidden.
     * On Windows, this method also sets the DOS "hidden" file attribute.
     * 
     * You do not need to modify this method, but you SHOULD use it when creating hidden files
     * for your tests. This will make sure that your tests work on both Windows and UNIX-like systems.
     *
     * @param parentFolder the directory in which to create the hidden file
     * @param filename the name of the hidden file; must start with a dot (.)
     * @return a File object representing the created hidden file
     * @throws IOException if an I/O error occurs during file creation or attribute setting
     * @throws IllegalArgumentException if the filename does not start with a dot (.)
     */
    private static File createHiddenFile(File parentFolder, String filename) throws IOException {
        if(!filename.startsWith(".")) {
            throw new IllegalArgumentException("Hidden files/folders must start with a '.'");
        }
        File hidden = new File(parentFolder, filename);
        hidden.createNewFile();
        if(isWindows()) {
            Path path = Paths.get(hidden.toURI());
            Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
        }
        return hidden;
    }

    private static String printTreeOutput(File root, boolean showHidden, boolean useColor) throws IOException {
        TruffulaOptions options = new TruffulaOptions(root, showHidden, useColor);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream ps = new PrintStream(baos);
        TruffulaPrinter printer = new TruffulaPrinter(options, ps);
        printer.printTree();
        return baos.toString();
    }

@Test
public void testPrintTree_SingleDirectory(@TempDir File tempDir) throws IOException {
    File folder = new File(tempDir, "myFolder");
    folder.mkdir();

    TruffulaOptions options = new TruffulaOptions(folder, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);

    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();
    ConsoleColor white = ConsoleColor.WHITE;
    ConsoleColor reset = ConsoleColor.RESET;

    String expected = "" + white + "myFolder/" + nl + reset;
    assertEquals(expected, output);
}

@Test
public void testPrintTree_FolderWithOneFile(@TempDir File tempDir) throws IOException {
    File folder = new File(tempDir, "root");
    folder.mkdir();
    new File(folder, "hello.txt").createNewFile();

    TruffulaOptions options = new TruffulaOptions(folder, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);

    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();
    ConsoleColor white = ConsoleColor.WHITE;
    ConsoleColor reset = ConsoleColor.RESET;

    String expected = "" + white + "root/" + nl + reset
                    + white + "   hello.txt" + nl + reset;
    assertEquals(expected, output);
}

@Test
public void testPrintTree_contains_SubFolders(@TempDir File tempDir) throws IOException {
    File root = new File(tempDir, "root");
    root.mkdir();
    File sub = new File(root, "subfolder");
    sub.mkdir();
    File deep = new File(sub, "child-subfolder");
    deep.mkdir();

    TruffulaOptions options = new TruffulaOptions(root, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);

    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();
    ConsoleColor white = ConsoleColor.WHITE;
    ConsoleColor reset = ConsoleColor.RESET;

    String expected = "" + white + "root/" + nl + reset
                    + white + "   subfolder/" + nl + reset
                    + white + "      child-subfolder/" + nl + reset;
    assertEquals(expected, output);
}

@Test
public void testPrintTree_MultipleFiles(@TempDir File tempDir) throws IOException {
    File folder = new File(tempDir, "file_folder");
    folder.mkdir();
    new File(folder, "file1.txt").createNewFile();
    new File(folder, "file2.txt").createNewFile();
    new File(folder, "file3.txt").createNewFile();

    TruffulaOptions options = new TruffulaOptions(folder, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);

    printer.printTree();

    String output = baos.toString();
    ConsoleColor white = ConsoleColor.WHITE;

    assertTrue(output.contains("" + white + "file_folder/"));
    assertTrue(output.contains("" + white + "   file1.txt"));
    assertTrue(output.contains("" + white + "   file2.txt"));
    assertTrue(output.contains("" + white + "   file3.txt"));
}

@Test
public void testPrintTree_FolderAndFileWithSimilarNames(@TempDir File tempDir) throws IOException {
    File folder = new File(tempDir, "Jupiter");
    folder.mkdir();

    File file = new File(tempDir, "Jupiter.txt");
    file.createNewFile();

    TruffulaOptions options = new TruffulaOptions(tempDir, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);

    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();
    ConsoleColor white = ConsoleColor.WHITE;
    ConsoleColor reset = ConsoleColor.RESET;

    System.out.println("ACTUAL OUTPUT:\n" + output);

    assertTrue(output.contains(white + "   Jupiter/" + nl + reset));
    assertTrue(output.contains(white + "   Jupiter.txt" + nl + reset));
}

@Test
public void testPrintTree_NestedFoldersAndFiles(@TempDir File tempDir) throws IOException {
    File a = new File(tempDir, "a");
    a.mkdir();
    File b = new File(a, "b");
    b.mkdir();
    File c = new File(b, "c");
    c.mkdir();
    File d = new File(c, "d");
    d.mkdir();
    new File(d, "test.txt").createNewFile();

    TruffulaOptions options = new TruffulaOptions(a, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);
    printer.printTree();
    String output = baos.toString();
    ConsoleColor white = ConsoleColor.WHITE;
    ConsoleColor reset = ConsoleColor.RESET;
    String nl = System.lineSeparator();

    String expected = "" + white + "a/" + nl + reset
                    + white + "   b/" + nl + reset
                    + white + "      c/" + nl + reset
                    + white + "         d/" + nl + reset
                    + white + "            test.txt" + nl + reset;
    assertEquals(expected, output);
}

@Test
public void testPrintTree_FileWithSpaces(@TempDir File tempDir) throws IOException {
    File folder = new File(tempDir, "docs");
    folder.mkdir();
    new File(folder, "lab samples.txt").createNewFile();
    new File(folder, "data analysis.pdf").createNewFile();

    TruffulaOptions options = new TruffulaOptions(folder, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);
    printer.printTree();
    String output = baos.toString();
    ConsoleColor white = ConsoleColor.WHITE;

    assertTrue(output.contains(white + "   lab samples.txt"));
    assertTrue(output.contains(white + "   data analysis.pdf"));
}

@Test
public void testPrintTree_FilesWithNoExtension(@TempDir File tempDir) throws IOException {
    File folder = new File(tempDir, "project");
    folder.mkdir();
    new File(folder, "octupuses").createNewFile();
    new File(folder, "platypuses").createNewFile();

    TruffulaOptions options = new TruffulaOptions(folder, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);
    printer.printTree();
    String output = baos.toString();
    ConsoleColor white = ConsoleColor.WHITE;

    assertTrue(output.contains(white + "   octupuses"));
    assertTrue(output.contains(white + "   platypuses"));
}

@Test
void testNonExistentDirectory() {
    String[] args = {"/nonexistent/fake/path"};
    assertThrows(FileNotFoundException.class, () -> new TruffulaOptions(args));
}

@Test
public void testPrintTree_LongFileAndFolderNames(@TempDir File tempDir) throws IOException {
    File root = new File(tempDir, "longlonglonglonglonglonglonglonglonglong");
    root.mkdir();

    File subfolder = new File(root, "folderfolderfolderfolderfolderfolderfolder");
    subfolder.mkdir();

    new File(root, "longlonglonglonglonglonglonglonglonglong.txt").createNewFile();
    new File(root, "testtesttesttesttesttesttesttesttesttest").createNewFile();
    new File(subfolder, "folderfolderfolderfolderfolderfolderfolder.pdf").createNewFile();

    TruffulaOptions options = new TruffulaOptions(root, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);

    printer.printTree();

    String output = baos.toString();
    ConsoleColor white = ConsoleColor.WHITE;

    assertTrue(output.contains(white + "longlonglonglonglonglonglonglonglonglong/"));
    assertTrue(output.contains(white + "   longlonglonglonglonglonglonglonglonglong.txt"));
    assertTrue(output.contains(white + "   testtesttesttesttesttesttesttesttesttest"));
    assertTrue(output.contains(white + "   folderfolderfolderfolderfolderfolderfolder/"));
    assertTrue(output.contains(white + "      folderfolderfolderfolderfolderfolderfolder.pdf"));
}

@Test
public void testPrintTree_HiddenFileIsHidden(@TempDir File tempDir) throws IOException {
    File folder = new File(tempDir, "tests");
    folder.mkdir();
    new File(folder, "visible.txt").createNewFile();
    createHiddenFile(folder, ".invisible");

    TruffulaOptions options = new TruffulaOptions(folder, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);
    printer.printTree();
    String output = baos.toString();

    assertTrue(output.contains("visible.txt"));
    assertFalse(output.contains(".invisible"));
}

@Test
public void testPrintTree_HiddenFileIsShown(@TempDir File tempDir) throws IOException {
    File folder = new File(tempDir, "tests");
    folder.mkdir();
    new File(folder, "visible.txt").createNewFile();
    createHiddenFile(folder, ".invisible");

    TruffulaOptions options = new TruffulaOptions(folder, true, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);
    printer.printTree();
    String output = baos.toString();

    assertTrue(output.contains("visible.txt"));
    assertTrue(output.contains(".invisible"));
}

@Test
public void testPrintTree_HiddenSubFolderHidden(@TempDir File tempDir) throws IOException {
    File folder = new File(tempDir, "dir");
    folder.mkdir();
    new File(folder, "shown.txt").createNewFile();

    File hiddenDir = new File(folder, ".invisible");
    hiddenDir.mkdir();
    if (isWindows()) {
        Path path = Paths.get(hiddenDir.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
    }
    new File(hiddenDir, "hidden.png").createNewFile();

    TruffulaOptions options = new TruffulaOptions(folder, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);
    printer.printTree();
    String output = baos.toString();

    assertTrue(output.contains("shown.txt"));
    assertFalse(output.contains(".invisible"));
    assertFalse(output.contains("hidden.png"));
}

@Test
public void testPrintTree_HiddenSubFolderShown(@TempDir File tempDir) throws IOException {
    File folder = new File(tempDir, "dir");
    folder.mkdir();
    new File(folder, "shown.txt").createNewFile();

    File hiddenDir = new File(folder, ".invisible");
    hiddenDir.mkdir();
    if (isWindows()) {
        Path path = Paths.get(hiddenDir.toURI());
        Files.setAttribute(path, "dos:hidden", Boolean.TRUE, LinkOption.NOFOLLOW_LINKS);
    }
    new File(hiddenDir, "hidden.txt").createNewFile();

    TruffulaOptions options = new TruffulaOptions(folder, true, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);
    printer.printTree();
    String output = baos.toString();

    assertTrue(output.contains("shown.txt"));
    assertTrue(output.contains(".invisible"));
    assertTrue(output.contains("hidden.txt"));
}

@Test
public void testPrintTree_LoopThroughThreeColors(@TempDir File tempDir) throws IOException {
    File root = new File(tempDir, "dir");
    root.mkdir();
    new File(root, "interface.java").createNewFile();

    File subDir = new File(root, "subdirectory");
    subDir.mkdir();
    new File(subDir, "child-class-x.java").createNewFile();
    new File(subDir, "child-class-y.java").createNewFile();

    String output = printTreeOutput(root, false, true);
    String nl = System.lineSeparator();

    assertTrue(output.contains(ConsoleColor.WHITE + "dir/" + nl + ConsoleColor.RESET));
    assertTrue(output.contains(ConsoleColor.PURPLE + "   interface.java" + nl + ConsoleColor.RESET));
    assertTrue(output.contains(ConsoleColor.PURPLE + "   subdirectory/" + nl + ConsoleColor.RESET));
    assertTrue(output.contains(ConsoleColor.YELLOW + "      child-class-x.java" + nl + ConsoleColor.RESET));
    assertTrue(output.contains(ConsoleColor.YELLOW + "      child-class-y.java" + nl + ConsoleColor.RESET));
}

@Test
public void testPrintTree_LoopsThroughColorsUntilWhiteAgain(@TempDir File tempDir) throws IOException {
    File root = new File(tempDir, "dir");
    root.mkdir();
    File lvl1 = new File(root, "level1");
    lvl1.mkdir();
    File lvl2 = new File(lvl1, "level2");
    lvl2.mkdir();
    File lvl3 = new File(lvl2, "level3");
    lvl3.mkdir();

    String output = printTreeOutput(root, false, true);
    String nl = System.lineSeparator();

    assertTrue(output.contains(ConsoleColor.WHITE + "dir/" + nl + ConsoleColor.RESET));
    assertTrue(output.contains(ConsoleColor.PURPLE + "   level1/" + nl + ConsoleColor.RESET));
    assertTrue(output.contains(ConsoleColor.YELLOW + "      level2/" + nl + ConsoleColor.RESET));
    assertTrue(output.contains(ConsoleColor.WHITE + "         level3/" + nl + ConsoleColor.RESET));
}

@Test
public void testPrintTree_NoColorOnlyWhite(@TempDir File tempDir) throws IOException {
    File root = new File(tempDir, "dir");
    root.mkdir();
    File subDir = new File(root, "subdirectory");
    subDir.mkdir();
    new File(subDir, "notes.txt").createNewFile();

    String output = printTreeOutput(root, false, false);

    assertTrue(output.contains(ConsoleColor.WHITE + "dir/"));
    assertTrue(output.contains(ConsoleColor.WHITE + "   subdirectory/"));
    assertTrue(output.contains(ConsoleColor.WHITE + "      notes.txt"));
    assertFalse(output.contains(ConsoleColor.PURPLE.toString()));
    assertFalse(output.contains(ConsoleColor.YELLOW.toString()));
}

@Test
public void testPrintTree_ColorWithHiddenShown(@TempDir File tempDir) throws IOException {
    File root = new File(tempDir, "dir");
    root.mkdir();
    new File(root, "visible.txt").createNewFile();
    createHiddenFile(root, ".hidden.txt");

    String output = printTreeOutput(root, true, true);
    String nl = System.lineSeparator();

    assertTrue(output.contains(ConsoleColor.WHITE + "dir/" + nl + ConsoleColor.RESET));
    assertTrue(output.contains(ConsoleColor.PURPLE + "   visible.txt" + nl + ConsoleColor.RESET));
    assertTrue(output.contains(ConsoleColor.PURPLE + "   .hidden.txt" + nl + ConsoleColor.RESET));
}

@Test
public void testPrintTree_AlphabetizedCaseInsensitive(@TempDir File tempDir) throws IOException {
    File folder = new File(tempDir, "root");
    folder.mkdir();
    // Create in non-alphabetical order
    new File(folder, "zoo.txt").createNewFile();
    new File(folder, "Alligator.txt").createNewFile();
    new File(folder, "newt.txt").createNewFile();

    TruffulaOptions options = new TruffulaOptions(folder, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);
    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();
    ConsoleColor white = ConsoleColor.WHITE;
    ConsoleColor reset = ConsoleColor.RESET;

    String expected = "" + white + "root/" + nl + reset
                    + white + "   Alligator.txt" + nl + reset
                    + white + "   newt.txt" + nl + reset
                    + white + "   zoo.txt" + nl + reset;
    assertEquals(expected, output);
}

@Test
public void testPrintTree_AlphabetizedDirectories(@TempDir File tempDir) throws IOException {
    File folder = new File(tempDir, "repo");
    folder.mkdir();
    File archive = new File(folder, "tests");
    archive.mkdir();
    File cargo = new File(folder, "dependencies");
    cargo.mkdir();
    File bridge = new File(folder, "classes");
    bridge.mkdir();

    TruffulaOptions options = new TruffulaOptions(folder, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);
    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();
    ConsoleColor white = ConsoleColor.WHITE;
    ConsoleColor reset = ConsoleColor.RESET;

    String expected = "" + white + "repo/" + nl + reset
                    + white + "   classes/" + nl + reset
                    + white + "   dependencies/" + nl + reset
                    + white + "   tests/" + nl + reset;
    assertEquals(expected, output);
}

@Test
public void testPrintTree_AlphabetizedNestedFiles(@TempDir File tempDir) throws IOException {
    File root = new File(tempDir, "root");
    root.mkdir();
    File subDir = new File(root, "miscellaneous");
    subDir.mkdir();
    new File(subDir, "zeplin.md").createNewFile();
    new File(subDir, "almanac.md").createNewFile();
    new File(subDir, "noir.md").createNewFile();

    TruffulaOptions options = new TruffulaOptions(root, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);
    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();
    ConsoleColor white = ConsoleColor.WHITE;
    ConsoleColor reset = ConsoleColor.RESET;

    String expected = "" + white + "root/" + nl + reset
                    + white + "   miscellaneous/" + nl + reset
                    + white + "      almanac.md" + nl + reset
                    + white + "      noir.md" + nl + reset
                    + white + "      zeplin.md" + nl + reset;
    assertEquals(expected, output);
}

@Test
public void testPrintTree_AlphabetizedNestedFilesAndDirectories(@TempDir File tempDir) throws IOException {
    File folder = new File(tempDir, "fromUnknownOriginInTheUniverse");
    folder.mkdir();
    new File(folder, "Oumuamua.txt").createNewFile();
    new File(folder, "borisov.txt").createNewFile();
    new File(folder, "Halley.txt").createNewFile();

    File comets = new File(folder, "comets");
    comets.mkdir();
    new File(comets, "Tempel.txt").createNewFile();
    new File(comets, "encke.txt").createNewFile();
    new File(comets, "Hale-Bopp.txt").createNewFile();

    TruffulaOptions options = new TruffulaOptions(folder, false, false);
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    PrintStream ps = new PrintStream(baos);
    TruffulaPrinter printer = new TruffulaPrinter(options, ps);
    printer.printTree();

    String output = baos.toString();
    String nl = System.lineSeparator();
    ConsoleColor white = ConsoleColor.WHITE;
    ConsoleColor reset = ConsoleColor.RESET;

    String expected = "" + white + "fromUnknownOriginInTheUniverse/" + nl + reset
                    + white + "   borisov.txt" + nl + reset
                    + white + "   comets/" + nl + reset
                    + white + "      encke.txt" + nl + reset
                    + white + "      Hale-Bopp.txt" + nl + reset
                    + white + "      Tempel.txt" + nl + reset
                    + white + "   Halley.txt" + nl + reset
                    + white + "   Oumuamua.txt" + nl + reset;
    assertEquals(expected, output);
}
    @Test
    public void testPrintTree_ExactOutput_WithCustomPrintStream(@TempDir File tempDir) throws IOException {
        // Build the example directory structure:
        // myFolder/
        //    .hidden.txt
        //    Apple.txt
        //    banana.txt
        //    Documents/
        //       images/
        //          Cat.png
        //          cat.png
        //          Dog.png
        //       notes.txt
        //       README.md
        //    zebra.txt

        // Create "myFolder"
        File myFolder = new File(tempDir, "myFolder");
        assertTrue(myFolder.mkdir(), "myFolder should be created");

        // Create visible files in myFolder
        File apple = new File(myFolder, "Apple.txt");
        File banana = new File(myFolder, "banana.txt");
        File zebra = new File(myFolder, "zebra.txt");
        apple.createNewFile();
        banana.createNewFile();
        zebra.createNewFile();

        // Create a hidden file in myFolder
        createHiddenFile(myFolder, ".hidden.txt");

        // Create subdirectory "Documents" in myFolder
        File documents = new File(myFolder, "Documents");
        assertTrue(documents.mkdir(), "Documents directory should be created");

        // Create files in Documents
        File readme = new File(documents, "README.md");
        File notes = new File(documents, "notes.txt");
        readme.createNewFile();
        notes.createNewFile();

        // Create subdirectory "images" in Documents
        File images = new File(documents, "images");
        assertTrue(images.mkdir(), "images directory should be created");

        // Create files in images
        File cat = new File(images, "cat.png");
        File dog = new File(images, "Dog.png");
        cat.createNewFile();
        dog.createNewFile();

        // Set up TruffulaOptions with showHidden = false and useColor = true
        TruffulaOptions options = new TruffulaOptions(myFolder, false, true);

        // Capture output using a custom PrintStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(baos);

        // Instantiate TruffulaPrinter with custom PrintStream
        TruffulaPrinter printer = new TruffulaPrinter(options, printStream);

        // Call printTree (output goes to printStream)
        printer.printTree();

        // Retrieve printed output
        String output = baos.toString();
        String nl = System.lineSeparator();

        // Build expected output with exact colors and indentation
        ConsoleColor reset = ConsoleColor.RESET;
        ConsoleColor white = ConsoleColor.WHITE;
        ConsoleColor purple = ConsoleColor.PURPLE;
        ConsoleColor yellow = ConsoleColor.YELLOW;

        StringBuilder expected = new StringBuilder();
        expected.append(white).append("myFolder/").append(nl).append(reset);
        expected.append(purple).append("   Apple.txt").append(nl).append(reset);
        expected.append(purple).append("   banana.txt").append(nl).append(reset);
        expected.append(purple).append("   Documents/").append(nl).append(reset);
        expected.append(yellow).append("      images/").append(nl).append(reset);
        expected.append(white).append("         cat.png").append(nl).append(reset);
        expected.append(white).append("         Dog.png").append(nl).append(reset);
        expected.append(yellow).append("      notes.txt").append(nl).append(reset);
        expected.append(yellow).append("      README.md").append(nl).append(reset);
        expected.append(purple).append("   zebra.txt").append(nl).append(reset);

        // Assert that the output matches the expected output exactly
        assertEquals(expected.toString(), output);
    }
}
