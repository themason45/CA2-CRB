package main.support.menu;

import jdk.jshell.spi.ExecutionControl;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

public class BaseCreateMenu extends BaseListMenu {
    protected String instructions = "";

    public BaseCreateMenu(BaseMenu previousMenu, Scanner scanner) {
        super(previousMenu, scanner);
    }

    public ByteArrayOutputStream printStream() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);

        printStream.print("University of Knowledge - COVID Test \n\n");

        printStream.printf("Adding %s\n", title);
        printStream = renderList(printStream);

        printStream.println(instructions);
        printStream.print("""
                0. Back to main menu.
                -1. Quit application.
                """);

        return byteArrayOutputStream;
    }

    public BaseMenuOption decodeOption(int input) {
        return BaseMenuOption.NOOP;
    }

    public void performCreation(String input) {
        throw new UnsupportedOperationException("This function must be overridden");
    }

    public void draw() {
        ByteArrayOutputStream baos = this.printStream();
        System.out.print(baos.toString());

        // Take the user's input
        scanner.nextLine();
        String input = scanner.nextLine();

        clearScreen();

        try {
            // Allow for integers to be input in order to check if the user wishes to go back
            int selection = Integer.parseInt(input);
            this.selectOption(selection);
        } catch (NumberFormatException e) {
            // If the input is a string, we want to parse it
            this.performCreation(input);
        }

    }
}
