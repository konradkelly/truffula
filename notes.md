# Truffula Notes
As part of Wave 0, please fill out notes for each of the below files. They are in the order I recommend you go through them. A few bullet points for each file is enough. You don't need to have a perfect understanding of everything, but you should work to gain an idea of how the project is structured and what you'll need to implement. Note that there are programming techniques used here that we have not covered in class! You will need to do some light research around things like enums and and `java.io.File`.

PLEASE MAKE FREQUENT COMMITS AS YOU FILL OUT THIS FILE.

## App.java

## ConsoleColor.java
ConsoleColor is an enum that stores ANSI escape codes for different colors. It has constants for various colors such as BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE, and RESET. Each constant has a corresponding ANSI escape code that can be used to change the color of text in the console. The enum also has a method getCode() that returns the ANSI escape code for the color. This allows other classes to easily access and use the color codes when printing colored text to the console. There is also a public toString() method that returns the ANSI escape code as a String, allowing the enum to be used directly in print statements.

## ColorPrinter.java / ColorPrinterTest.java
ColorPrinter is a utility class that uses ANSI escape codes to print colored text to the console. It contains fields, currentColor and printStream that sets the current color and the print stream, that prints the colored text to stdout. There is also a constructor with a default color of white. The class has getters and setters as well as methods to set the color, reset the color, and print colored text.

The ColorPrinterTest class currently only contains a unit test for the ColorPrinter class. It tests the setColor method by creating an instance of ColorPrinter, setting the color to red. It asserts that the current color is red. This test fails as the print(String message, boolean reset) method has not been implemented yet. 

## TruffulaOptions.java / TruffulaOptionsTest.java

## TruffulaPrinter.java / TruffulaPrinterTest.java

## AlphabeticalFileSorter.java