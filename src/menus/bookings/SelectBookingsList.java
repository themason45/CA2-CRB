package menus.bookings;

import menus.MainMenu;
import support.BookingManager;
import support.menu.BaseMenu;
import support.menu.SegueOption;

import java.util.ArrayList;
import java.util.Scanner;

public class SelectBookingsList extends BaseMenu {
    BookingManager bookingManager;

    public SelectBookingsList(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);
        this.bookingManager = bookingManager;

        this.title = "Select which booking to list:";
        this.options = new ArrayList<>();
        options.add(new SegueOption("All", BookingsList.class, this.previousMenu,
                scanner, bookingManager, 0));
        options.add(new SegueOption("Only bookings status:SCHEDULED", BookingsList.class, this.previousMenu,
                scanner, bookingManager, 1));
        options.add(new SegueOption("Only bookings status:COMPLETED", BookingsList.class, this.previousMenu,
                scanner, bookingManager, 2));
    }
}