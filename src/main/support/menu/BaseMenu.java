package main.support.menu;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * BaseMenu is a class that defines a list of options, and a heading, and acts as the primary interface for the user
 */
public class BaseMenu {
    public String title;  // Eg: Manage Bookings
    public ArrayList<BaseMenuOption> options;

    public BaseMenu previousMenu;
    public Scanner scanner;

    public BaseMenu(BaseMenu previousMenu, Scanner scanner) {
        this.previousMenu = previousMenu;
        this.scanner = scanner;
    }

    /**
     * Generate the UI for the current menu, this is what should be overridden
     *
     * @return A stream which can be converted to a string, this is what will be displayed on screen
     */
    public ByteArrayOutputStream printStream() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);

        printStream.printf("""
                University of Knowledge - COVID Test\s

                %s

                Please, enter the number to select your option:
                                
                """, title);

        int i = 1;
        for (BaseMenuOption option : options) {
            if (option.selectable) {
                printStream.printf("\t %d.\t %s\n", i, option.title);
                i++;
            } else {
                printStream.println(option.title);
            }
        }
        printStream.println();  // Add trailing new line

        printStream.print("""
                After selecting one the options above, you will be presented other screens.
                If you press 0, you will be able to return to this main menu.
                Press -1 (or ctrl+c) to quit this application.
                """);


        return byteArrayOutputStream;
    }

    /**
     * @param input The input number from the user
     * @return The selected option
     */
    protected BaseMenuOption decodeOption(int input) {
        ArrayList<BaseMenuOption> optionList = this.options.stream().filter(x -> x.selectable).
                collect(Collectors.toCollection(ArrayList::new));
        return optionList.get(input - 1);
    }

    /**
     * Execute the desired option, this can be 0, or -1, and will be acted upon accordingly
     *
     * @param optionNumber The input number from the user
     */
    public void selectOption(int optionNumber) {
        // TODO: Add checks for a valid input

        switch (optionNumber) {
            case -1:
                System.exit(0);
                break;
            case 0:
                if (this.previousMenu != null) {
                    clearScreen();
                    this.previousMenu.draw();
                }
                break;
            default:
                BaseMenuOption option = decodeOption(optionNumber);
                option.execute();
        }
    }

    /**
     * Draw the result of {@link #printStream()} to the console, and wait for user input
     */
    public void draw() {
        ByteArrayOutputStream baos = this.printStream();
        System.out.print(baos.toString());

        // Take the user's input
        int selection = scanner.nextInt();

        clearScreen();

        this.selectOption(selection);
    }

    /**
     * Clear the screen, this is used when moving to a new menu
     */
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
}
