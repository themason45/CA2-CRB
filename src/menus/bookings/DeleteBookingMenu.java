package menus.bookings;

import menus.MainMenu;
import support.BookingManager;
import support.menu.BaseDeleteMenu;

import java.util.Scanner;

public class DeleteBookingMenu extends BaseDeleteMenu {
    public DeleteBookingMenu(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);

        this.title = "booking";
        this.fullTitle = "Removing booking from the system";

        this.methodName = "deleteBooking";

        this.list = bookingManager.getBookings();
    }

    @Override
    public String generateOnCompleteMessage(Object obj) {
        return String.format("Booking removed successfully:\n%s", obj.toString());
    }
}
