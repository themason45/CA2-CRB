package main.menus;

import main.menus.assistants.AddAssistantsOnShitMenu;
import main.menus.assistants.AssistantsOnShiftList;
import main.menus.bookings.AddBookingMenu;
import main.menus.rooms.AddBookableRoomMenu;
import main.menus.rooms.BookableRoomList;
import main.menus.bookings.SelectBookingsList;
import main.support.BookingManager;
import main.support.menu.BaseMenu;
import main.support.menu.BaseMenuOption;
import main.support.menu.SegueOption;

import java.util.ArrayList;
import java.util.Scanner;

public class MainMenu extends BaseMenu {
    private BookingManager bookingManager;

    public MainMenu(BookingManager bookingManager, Scanner scanner) {
        super(null, scanner, bookingManager);
        this.title = "Manage bookings";
        this.options = new ArrayList<>();
        this.bookingManager = bookingManager;
        this.longFooter = true;

        // Bookable rooms
        options.add(new BaseMenuOption("To manage Bookable rooms:"));
        options.add(new SegueOption("List", BookableRoomList.class, this, scanner, bookingManager));
        options.add(new SegueOption("Add", AddBookableRoomMenu.class, this, scanner, bookingManager));

        // Assistants on shift
        options.add(new BaseMenuOption("To manage Assistants on shift:"));
        options.add(new SegueOption("List", AssistantsOnShiftList.class, this, scanner, bookingManager));
        options.add(new SegueOption("Add", AddAssistantsOnShitMenu.class, this, scanner, bookingManager));

        // Bookings
        options.add(new BaseMenuOption("To manage Bookings:"));
        options.add(new SegueOption("List", SelectBookingsList.class, this, scanner, bookingManager));
        options.add(new SegueOption("Add", AddBookingMenu.class, this, scanner, bookingManager));
    }

    public void setBookingManager(BookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }
}
