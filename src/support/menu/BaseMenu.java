package support.menu;

import support.BookingManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * BaseMenu is a class that defines a list of options, and a heading, and acts as the primary interface for the user
 */
public class BaseMenu {
    public String title;  // Eg: Manage Bookings
    public String subTitle = "";  // Eg: Manage Bookings
    public ArrayList<BaseMenuOption> options;

    public BaseMenu previousMenu;
    public Scanner scanner;

    public BaseMenuOption defaultOption;
    public boolean useDefault = false;

    public boolean longFooter = false;
    public boolean blockOtherOptions = false;  // If an integer other than 0, or -1, is inputted, then block it

    public BookingManager bookingManager;

    public BaseMenu(BaseMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        this.previousMenu = previousMenu;
        this.scanner = scanner;
        this.bookingManager = bookingManager;

        this.defaultOption = BaseMenuOption.NOOP;
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
                printStream.printf("\n\t %d.\t %s", i, option.title);
                i++;
            } else {
                printStream.printf("\n%s", option.title);
            }
        }
        if (subTitle.length() > 0) printStream.printf("\n%s\n", subTitle);
        printStream.println();  // Add trailing new line

        if (longFooter) {
            printStream.print("""
                    After selecting one the options above, you will be presented other screens.
                    If you press 0, you will be able to return to this main menu.
                    Press -1 (or ctrl+c) to quit this application.
                    """);
        } else {
            printStream.print("""
                    0. Back to main menu.
                    -1. Quit application.
                    """);
        }

        return byteArrayOutputStream;
    }

    /**
     * @param input The input number from the user
     * @return The selected option
     */
    protected BaseMenuOption decodeOption(int input) {
        ArrayList<BaseMenuOption> optionList = this.options.stream().filter(x -> x.selectable).
                collect(Collectors.toCollection(ArrayList::new));

        if (input > optionList.size() & useDefault) return this.defaultOption;

        return optionList.get(input - 1);
    }

    /**
     * Execute the desired option, this can be 0, or -1, and will be acted upon accordingly
     *
     * @param optionNumber The input number from the user
     */
    public void selectOption(int optionNumber) throws IllegalArgumentException {

        switch (optionNumber) {
            case -1:
                System.exit(0);
                break;
            case 0:
                if (this.previousMenu != null) {
                    clearScreen();
                    // Update the new menu with the new booking manager values
                    this.previousMenu.bookingManager = this.bookingManager;
                    this.previousMenu.draw();
                }
                break;
            default:
                if (blockOtherOptions) throw new IllegalArgumentException("That option is not valid");

                BaseMenuOption option = decodeOption(optionNumber);
                if (option.methodIdentifier.equals(BaseMenuOption.REDRAW("").methodIdentifier)) {
                    this.redrawWithMessage((String) option.funcArgs[0]);
                } else if (option instanceof SegueOption) {
                    option.execute();
                    this.postExecute(option);
                } else {
                    try {
                        option.executeOnInstance(bookingManager);
                        this.postExecute(option);
                    } catch (Exception e) {
                        this.error(e);
                    }
                }
        }
    }

    /**
     * Once the option has been executed, then run this
     * @param option The option that was executed
     */
    public void postExecute(BaseMenuOption option) {
    }

    /**
     * Once the menu has been drawn, then run this, normally to take the user's input as an int, but can be
     * overridden to take strings as the input
     *
     * @param skipLine Whether the system should skip a line once the page has been drawn.
     */
    public void postDraw(boolean skipLine) {
        // Take the user's input
        try {
            if (skipLine) scanner.nextLine();
            int selection = scanner.nextInt();

            clearScreen();

            try {
                this.selectOption(selection);
            } catch (IllegalArgumentException e) {
                this.error(e);
            }
        } catch (InputMismatchException e) {
            this.error(new Exception("An integer must be inputted"));
        }
    }

    /**
     * Draw the result of {@link #printStream()} to the console, and wait for user input
     */
    public void draw() {
        ByteArrayOutputStream baos = this.printStream();
        System.out.print(baos.toString());

        this.postDraw(false);
    }

    /**
     * Clear the screen, this is used when moving to a new menu
     */
    public void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }

    /**
     * Redraws the menu with a given method
     *
     * @param message The message to prepend the view with
     */
    public void redrawWithMessage(String message) {
        ByteArrayOutputStream baos = this.printStream();
        System.out.printf("%s\n%s", message, baos.toString());
        this.postDraw(true);
    }

    /**
     * Prepends the menu display with a given exception's message
     *
     * @param e The method who's {@link Exception#getMessage()} is displayed
     */
    public void error(Exception e) {
        this.clearScreen();
        this.redrawWithMessage(String.format("Error!\n%s", e.getMessage()));
    }
}
