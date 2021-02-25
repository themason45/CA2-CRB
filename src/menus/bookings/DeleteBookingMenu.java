package menus.bookings;

import menus.MainMenu;
import support.BookingManager;
import support.menu.BaseDeleteMenu;

import java.util.Scanner;

/**
 * Deletes a booking from the system, given a list of {@link models.Booking} with the status of SCHEDULED.
 */
public class DeleteBookingMenu extends BaseDeleteMenu {
    public DeleteBookingMenu(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);

        this.title = "booking";
        this.fullTitle = "Removing booking from the system";

        this.methodName = "deleteBooking";

        this.list = bookingManager.getScheduledBookings();
    }

    /**
     * Updates the list to be displayed once the deletion has finished
     */
    @Override
    public void postDelete() {
        this.list = bookingManager.getScheduledBookings();
    }

    /**
     * @param obj The object to be displayed in the on complete message
     * @return A string telling the user that the given item has been deleted successfully
     */
    @Override
    public String generateOnCompleteMessage(Object obj) {
        return String.format("Booking removed successfully:\n%s", obj.toString());
    }
}
