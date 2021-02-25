package menus.bookings;

import menus.MainMenu;
import support.BookingManager;
import support.menu.BaseCreateMenu;
import support.menu.BaseMenuOption;

import java.util.Scanner;

/**
 * Lets the user select a booking, which is then marked as completed
 */
public class ConcludeBookingMenu extends BaseCreateMenu {
    public ConcludeBookingMenu(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);

        this.fullTitle = "";
        this.title = "bookings";
        this.subTitle = "Conclude booking";
        this.blockOtherOptions = false;
        this.instructions = "The sequential ID to select the booking to be completed.";

        this.list = bookingManager.completeBookingOptions();
    }

    /**
     * @param input The sequential ID of the given option
     * @return The {@link BaseMenuOption} that corresponds to that given ID
     */
    @Override
    public BaseMenuOption decodeOption(int input) {
        if (input - offset > this.list.size())
            this.error(new IndexOutOfBoundsException(String.format("Please select an ID between %d, and %d",
                    offset, list.size() + offset - 1)));
        return (BaseMenuOption) this.list.get(input - offset);
    }

    /**
     * Executed once the option's method has been invoked
     * @param option The option that was selected
     */
    @Override
    public void postExecute(BaseMenuOption option) {
        this.list = bookingManager.completeBookingOptions();
        this.redrawWithMessage(String.format("Booking completed successfully:\n%s", option.title));
    }
}
