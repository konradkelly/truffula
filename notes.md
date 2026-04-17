# Truffula Notes
As part of Wave 0, please fill out notes for each of the below files. They are in the order I recommend you go through them. A few bullet points for each file is enough. You don't need to have a perfect understanding of everything, but you should work to gain an idea of how the project is structured and what you'll need to implement. Note that there are programming techniques used here that we have not covered in class! You will need to do some light research around things like enums and and `java.io.File`.

PLEASE MAKE FREQUENT COMMITS AS YOU FILL OUT THIS FILE.

## App.java
App.java is where the main method resides. `main(String[] args)` creates a `TruffulaOptions` from the command-line args, passes it to a new `TruffulaPrinter`, and calls `printTree()`. It supports the `-h` (show hidden) and `-nc` (no color) flags, with the target path always as the last argument.

## ConsoleColor.java
ConsoleColor is an enum that stores ANSI escape codes for different colors. It has constants for various colors such as BLACK, RED, GREEN, YELLOW, BLUE, PURPLE, CYAN, WHITE, and RESET. Each constant has a corresponding ANSI escape code that can be used to change the color of text in the console. The enum also has a method getCode() that returns the ANSI escape code for the color. This allows other classes to easily access and use the color codes when printing colored text to the console. There is also a public toString() method that returns the ANSI escape code as a String, allowing the enum to be used directly in print statements.

## ColorPrinter.java / ColorPrinterTest.java
ColorPrinter is a utility class that uses ANSI escape codes to print colored text to the console. It contains fields `currentColor` and `printStream`, a default-white constructor, and getters/setters. `print(String message, boolean reset)` was implemented to prepend the current color's ANSI code to the message and append the RESET code if `reset` is true.

Unit tests were added to verify colored output, reset behavior, and the default white color.

## TruffulaOptions.java / TruffulaOptionsTest.java
The TruffulaOptions class manages configuration for the directory tree display: `root` (the target directory), `showHidden`, and `useColor`. The `TruffulaOptions(String[] args)` constructor was implemented to parse `-h` and `-nc` flags in any order, with the path always as the final argument. It throws `IllegalArgumentException` for unknown flags or a missing path, and `FileNotFoundException` if the path doesn't exist or isn't a directory.

Unit tests were added for: valid directory, missing path, path pointing to a file, all flag combinations (`-h`, `-nc`, both, neither), and unknown flags.

## TruffulaPrinter.java / TruffulaPrinterTest.java
TruffulaPrinter prints a directory tree using a `ColorPrinter`. `printTree()` calls a recursive helper `printTreeHelper(File, int)` that indents 3 spaces per depth level and appends `/` to directory names. Hidden files/folders are skipped when `showHidden` is false (checked via `file.isHidden()`). Color cycles through `colorSequence` by depth index (`k % size`): WHITE at the root, PURPLE at depth 1, YELLOW at depth 2, then repeating. When color is disabled, all output uses WHITE. Children are sorted case-insensitively via `AlphabeticalFileSorter.sort()` before recursing.

Unit tests cover: basic tree structure, hidden file exclusion and inclusion, correct color per depth level, and alphabetical ordering of nested files, directories, and mixed content.

## AlphabeticalFileSorter.java
The AlphabeticalFileSorter class is a utility class with a `sort(File[] files)` method that sorts an array of File objects case-insensitively using `Arrays.sort()` with `compareToIgnoreCase()` on file names. Identical case-insensitive names fall back to lexicographic order. On Windows the file system is itself case-insensitive, so files like Cat.png and cat.png cannot coexist in the same folder and cannot be reliably tested as separate entries. This class is called by `TruffulaPrinter` before recursing into a directory's children.