# Truffula Notes
As part of Wave 0, please fill out notes for each of the below files. They are in the order I recommend you go through them. A few bullet points for each file is enough. You don't need to have a perfect understanding of everything, but you should work to gain an idea of how the project is structured and what you'll need to implement. Note that there are programming techniques used here that we have not covered in class! You will need to do some light research around things like enums and and `java.io.File`.

PLEASE MAKE FREQUENT COMMITS AS YOU FILL OUT THIS FILE.

## App.java
App.java is where the main method resides. The implementation notes state that an instance of the TruffulaOptions class should be created. The options should be initialized with the command-line arguments passed to the main method. Then, an instance of the TruffulaPrinter class should be created, passing the options as a parameter. Finally, the printTree() method of the TruffulaPrinter instance should be called to display the directory tree structure in the console.

## ConsoleColor.java
ConsoleColor is an enum that stores ANSI escape codes for different colors. It has constants for various colors such as BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE, and RESET. Each constant has a corresponding ANSI escape code that can be used to change the color of text in the console. The enum also has a method getCode() that returns the ANSI escape code for the color. This allows other classes to easily access and use the color codes when printing colored text to the console. There is also a public toString() method that returns the ANSI escape code as a String, allowing the enum to be used directly in print statements.

## ColorPrinter.java / ColorPrinterTest.java
ColorPrinter is a utility class that uses ANSI escape codes to print colored text to the console. It contains fields, currentColor and printStream that sets the current color and the print stream, that prints the colored text to stdout. There is also a constructor with a default color of white. The class has getters and setters as well as methods to set the color, reset the color, and print colored text.

The ColorPrinterTest class currently only contains a unit test for the ColorPrinter class. It tests the setColor method by creating an instance of ColorPrinter, setting the color to red. It asserts that the current color is red. This test fails as the print(String message, boolean reset) method has not been implemented yet. 

## TruffulaOptions.java / TruffulaOptionsTest.java
The TruffulaOptions class is a utility class that manages the configuration for displaying the Truffula directory tree.  It has fields for the maximum depth of the tree and whether to show hidden files. The class provides getters and setters for these fields, allowing other classes to access and modify the options as needed. The root directory is specified by the user.

The TruffulaOptionsTest class only contains only one unit-test, testValidDirectoryIsSet(@TempDir File tempDir), which tests that a valid directory can be set as the root directory in the TruffulaOptions class. It creates a temporary directory using the @TempDir annotation and sets it as the root directory in an instance of TruffulaOptions. The test then asserts that the root directory is correctly set to the temporary directory. This test is currently failing because the TruffulaOptions(String[] args) constructor has not been implemented yet.

## TruffulaPrinter.java / TruffulaPrinterTest.java
TruffulaPrinter is a class responsible for printing the directory tree structure of a given root directory in a case-insensitive and alphabetical order. It uses the options specified in the TruffulaOptions class to determine whether to show hidden files and whether to use color. The class can cycle through a sequence of colors for each directory level. The class has a method printDirectoryTree() that recursively traverses the directory structure and prints it in a formatted way. The class also uses the ColorPrinter class to print the directory names in different colors based on their type (e.g., directories in blue, files in white).

The TruffulaPrinterTest class contains utility methods to create hidden files in Unix and Windows operating systems, as well as the ability to check if the user's directory is a Windows OS one. Additionally, the class contains one test method, testPrintTree_ExactOutput_WithCustomPrintStream(@TempDir File tempDir), which sets up a sample directory with visible and hidden files. It retrieves the output of printer.printTree() method and asserts that the output matches the expected string. This test is currently failing because the printDirectoryTree() method has not been implemented yet.

## AlphabeticalFileSorter.java
The AlphabeticalFileSorter class is a utility class with a method sortFiles(File[] files) that takes an array of File objects and sorts them in a case-insensitive alphabetical order. The method uses the Arrays.sort() method with a compareToIgnoreCase() that compares the file names in a case-insensitive manner. Arrays.sort() uses the output of the comparator method to sort lexicographically. This class is used by the TruffulaPrinter class to ensure that the directory tree is printed in the correct order.