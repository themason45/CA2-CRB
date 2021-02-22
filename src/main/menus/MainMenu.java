package main.menus;

import main.support.BookingManager;
import main.support.menu.BaseMenu;
import main.support.menu.BaseMenuOption;
import main.support.menu.SegueOption;

import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu extends BaseMenu {
    private BookingManager bookingManager;

    public MainMenu(BookingManager bookingManager, Scanner scanner) {
        super(null, scanner);
        this.title = "Manage bookings";
        this.options = new ArrayList<>();
        this.bookingManager = bookingManager;

        // Bookable rooms
        options.add(new BaseMenuOption("To manage Bookable rooms:"));
        options.add(new SegueOption("List", BookableRoomList.class, this, scanner, bookingManager));

        // Assistants on shift
        options.add(new BaseMenuOption("To manage Assistants on shift:"));
        options.add(new SegueOption("List", AssistantsOnShiftList.class, this, scanner, bookingManager));
        options.add(new SegueOption("Add", AddAssistantsOnShitMenu.class, this, scanner, bookingManager));

        // Bookings
        options.add(new BaseMenuOption("To manage Bookings:"));
        options.add(new SegueOption("Add", SelectBookingsList.class, this, scanner, bookingManager));
    }

    public void setBookingManager(BookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }
}
