package main.menus.bookings;

import main.menus.MainMenu;
import main.menus.bookings.BookingsList;
import main.support.BookingManager;
import main.support.menu.BaseMenu;
import main.support.menu.SegueOption;

import java.util.ArrayList;
import java.util.Scanner;

public class SelectBookingsList extends BaseMenu {
    BookingManager bookingManager;

    public SelectBookingsList(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner);
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
