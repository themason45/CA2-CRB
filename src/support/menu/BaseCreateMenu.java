package support.menu;

import support.BookingManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class BaseCreateMenu extends BaseListMenu {
    protected String instructions = "";

    public BaseCreateMenu(BaseMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);
        blockOtherOptions = true;
    }

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

    public BaseMenuOption decodeOption(int input) {
        return BaseMenuOption.NOOP;
    }

    public void performCreation(String input) {
        throw new UnsupportedOperationException("This function must be overridden");
    }

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
