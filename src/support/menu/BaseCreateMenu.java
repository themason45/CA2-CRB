package support.menu;

import support.BookingManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

/**
 * An extension of {@link BaseListMenu} which provides necessary methods for the creation of objects, this could
 * be creating them onto an instance of {@link BookingManager}.
 */
public class BaseCreateMenu extends BaseListMenu {
    protected String instructions = "";

    public BaseCreateMenu(BaseMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);
        blockOtherOptions = true;
    }

    /**
     * Same as {@link BaseListMenu#printStream()}, but outputs a different style of view, some blocks of text have
     * been rearranged for example
     *
     * @return A stream which can be converted to a string, this is what will be displayed on screen
     */
    public ByteArrayOutputStream printStream() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);

        printStream.print("University of Knowledge - COVID Test \n\n");

        if (this.fullTitle == null & this.title != null) {
            printStream.printf("Adding %s\n\n", title);
        } else {
            assert this.fullTitle != null;
            if (this.fullTitle.length() > 0) printStream.println(fullTitle);
        }
        printStream = renderList(printStream);
        if (subTitle.length() > 0) printStream.printf("%s\n\n", subTitle);

        printStream.println("Please, enter one of the following:\n");
        printStream.println(instructions);
        printStream.print("""
                0. Back to main menu.
                -1. Quit application.
                """);

        return byteArrayOutputStream;
    }

    /**
     * @param input The input number from the user
     * @return {@link BaseMenuOption#NOOP}
     */
    public BaseMenuOption decodeOption(int input) {
        return BaseMenuOption.NOOP;
    }

    /**
     * @param input The string inputted by the user
     */
    public void performCreation(String input) {
        throw new UnsupportedOperationException("This function must be overridden");
    }

    /**
     * Once the list has been drawn, this takes a string input, instead of an integer, as it can take more than just
     * the sequential ID.
     *
     * @param skipLine Whether the system should skip a line once the page has been drawn.
     */
    public void postDraw(boolean skipLine) {
        // Take the user's input
        if (!skipLine) scanner.nextLine();
        String input = scanner.nextLine();

        clearScreen();

        try {
            // Allow for integers to be input in order to check if the user wishes to go back
            int selection = Integer.parseInt(input);
            this.selectOption(selection);
        } catch (NumberFormatException e) {
            // If the input is a string, we want to parse it
            this.performCreation(input);
        } catch (IllegalArgumentException e) {
            this.error(e);
        }
    }
}
