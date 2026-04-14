import org.junit.jupiter.api.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ColorPrinterTest {

  @Test
  void testPrintlnWithRedColorAndReset() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.RED);

    String message = "I speak for the trees";
    printer.println(message);

    String expectedOutput = ConsoleColor.RED + "I speak for the trees" + System.lineSeparator() + ConsoleColor.RESET;

    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintlnThreeColorsNoResetUntilLast() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);

    printer.setCurrentColor(ConsoleColor.GREEN);
    printer.println("Color: green", false);

    printer.setCurrentColor(ConsoleColor.RED);
    printer.println("Color: red", false);

    printer.setCurrentColor(ConsoleColor.BLUE);
    printer.println("Color: blue", true);

    String expectedOutput = ConsoleColor.GREEN + "Color: green" + System.lineSeparator()
        + ConsoleColor.RED + "Color: red" + System.lineSeparator()
        + ConsoleColor.BLUE + "Color: blue" + System.lineSeparator() + ConsoleColor.RESET;

    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintWithRedNoNewline() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.RED);

    printer.print("no newline");

    String expectedOutput = ConsoleColor.RED + "no newline" + ConsoleColor.RESET;

    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testPrintNoResetThenPrintlnReset() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);

    printer.setCurrentColor(ConsoleColor.PURPLE);
    printer.print("purple part", false);

    printer.setCurrentColor(ConsoleColor.YELLOW);
    printer.println("yellow part", true);

    String expectedOutput = ConsoleColor.PURPLE + "purple part"
        + ConsoleColor.YELLOW + "yellow part" + System.lineSeparator() + ConsoleColor.RESET;

    assertEquals(expectedOutput, outputStream.toString());
  }

  @Test
  void testConstructorWithThreeColors() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream, ConsoleColor.CYAN);

    printer.println("1. Cyan", false);

    printer.setCurrentColor(ConsoleColor.PURPLE);
    printer.println("2. Purple", false);

    printer.setCurrentColor(ConsoleColor.YELLOW);
    printer.println("3. Yellow", true);

    String expectedOutput = ConsoleColor.CYAN + "1. Cyan" + System.lineSeparator()
        + ConsoleColor.PURPLE + "2. Purple" + System.lineSeparator()
        + ConsoleColor.YELLOW + "3. Yellow" + System.lineSeparator() + ConsoleColor.RESET;

    assertEquals(expectedOutput, outputStream.toString());
}

@Test
void testAllColors() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);

    printer.setCurrentColor(ConsoleColor.BLACK);
    printer.println("black", false);

    printer.setCurrentColor(ConsoleColor.RED);
    printer.println("red", false);

    printer.setCurrentColor(ConsoleColor.GREEN);
    printer.println("green", false);

    printer.setCurrentColor(ConsoleColor.YELLOW);
    printer.println("yellow", false);

    printer.setCurrentColor(ConsoleColor.BLUE);
    printer.println("blue", false);

    printer.setCurrentColor(ConsoleColor.PURPLE);
    printer.println("purple", false);

    printer.setCurrentColor(ConsoleColor.CYAN);
    printer.println("cyan", false);

    printer.setCurrentColor(ConsoleColor.WHITE);
    printer.println("white", true);

    String expectedOutput = ConsoleColor.BLACK + "black" + System.lineSeparator()
        + ConsoleColor.RED + "red" + System.lineSeparator()
        + ConsoleColor.GREEN + "green" + System.lineSeparator()
        + ConsoleColor.YELLOW + "yellow" + System.lineSeparator()
        + ConsoleColor.BLUE + "blue" + System.lineSeparator()
        + ConsoleColor.PURPLE + "purple" + System.lineSeparator()
        + ConsoleColor.CYAN + "cyan" + System.lineSeparator()
        + ConsoleColor.WHITE + "white" + System.lineSeparator()
        + ConsoleColor.RESET;

    assertEquals(expectedOutput, outputStream.toString());
}

  @Test
  void testGetCurrentColorReturnsSetColor() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);
    printer.setCurrentColor(ConsoleColor.BLUE);

    assertEquals(ConsoleColor.BLUE, printer.getCurrentColor());
  }

  @Test
  void testDefaultConstructorColorIsWhite() {
    ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
    PrintStream printStream = new PrintStream(outputStream);

    ColorPrinter printer = new ColorPrinter(printStream);

    assertEquals(ConsoleColor.WHITE, printer.getCurrentColor());
  }
}
