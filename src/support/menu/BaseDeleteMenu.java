package support.menu;

import support.BookingManager;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Scanner;

public class BaseDeleteMenu extends BaseListMenu {
    public Class<?> aClass;
    public String methodName;

    public BaseDeleteMenu(BaseMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);
        this.blockOtherOptions = false;

        this.aClass = BookingManager.class;
        this.methodName = "noop";
    }

    public ByteArrayOutputStream printStream() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);

        printStream.print("University of Knowledge - COVID Test \n\n");

        printStream = renderList(printStream);

        printStream.printf("Removing %s\n\n", title);

        printStream.println("Please, enter one of the following:\n");
        printStream.printf("""
                The sequential ID to select the %s to be removed from the listed bookings above.
                0. Back to main menu.
                -1. Quit application.
                """, this.title);

        return byteArrayOutputStream;
    }

    /**
     * Executed once the given object has been deleted
     */
    public void postDelete() {}

    /**
     * @param obj The object to be displayed in the on complete message
     * @return A string telling the user that the given item has been deleted successfully
     */
    public String generateOnCompleteMessage(Object obj) {
        return obj.toString();
    }

    public BaseMenuOption decodeOption(int input) {
        try {
            Object object = list.get(input - offset);

            BaseMenuOption option;
            if (object instanceof BaseMenuOption) {
                option = (BaseMenuOption) object;
            } else {
                option = new BaseMenuOption(Integer.toString(input), aClass, methodName, object);
            }

            try {
                option.executeOnInstance(bookingManager);
                postDelete();
            } catch (Exception e) {
                this.error(e);
            }
            return BaseMenuOption.REDRAW(generateOnCompleteMessage(object));
        } catch (IndexOutOfBoundsException e) {
            this.error(new IndexOutOfBoundsException(String.format("Please select an ID between %d, and %d",
                    offset, list.size() + offset - 1)));
        }
        return BaseMenuOption.NOOP;
    }

    public void postDraw(boolean skipLine) {
        // Take the user's input
        if (!skipLine) scanner.nextLine();
        String input = scanner.nextLine();

        clearScreen();

        try {
            int selection = Integer.parseInt(input);
            this.selectOption(selection);
        } catch (NumberFormatException e) {
            this.error(new NumberFormatException("An integer must be inputted, either 0, -1, or the desired ID"));
        } catch (IllegalArgumentException e) {
            this.error(e);
        }
    }
}
