package menus.bookings;

import menus.MainMenu;
import models.Booking;
import support.BookingManager;
import support.menu.BaseDeleteMenu;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class DeleteBookingMenu extends BaseDeleteMenu {
    public DeleteBookingMenu(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);

        this.title = "booking";
        this.fullTitle = "Removing booking from the system";

        this.methodName = "deleteBooking";

        this.list = bookingManager.getScheduledBookings();
    }

    @Override
    public void postDelete() {
        this.list = bookingManager.getScheduledBookings();
    }

    @Override
    public String generateOnCompleteMessage(Object obj) {
        return String.format("Booking removed successfully:\n%s", obj.toString());
    }
}
