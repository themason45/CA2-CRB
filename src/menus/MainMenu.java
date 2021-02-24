package menus;

import menus.assistants.AddAssistantsOnShitMenu;
import menus.assistants.AssistantsOnShiftList;
import menus.assistants.DeleteAssistantOnShiftMenu;
import menus.bookings.AddBookingMenu;
import menus.bookings.ConcludeBookingMenu;
import menus.bookings.DeleteBookingMenu;
import menus.rooms.AddBookableRoomMenu;
import menus.rooms.BookableRoomList;
import menus.bookings.SelectBookingsList;
import menus.rooms.DeleteBookableRoomMenu;
import support.BookingManager;
import support.menu.BaseMenu;
import support.menu.BaseMenuOption;
import support.menu.SegueOption;

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
        options.add(new SegueOption("Delete", DeleteBookableRoomMenu.class, this, scanner, bookingManager));

        // Assistants on shift
        options.add(new BaseMenuOption("To manage Assistants on shift:"));
        options.add(new SegueOption("List", AssistantsOnShiftList.class, this, scanner, bookingManager));
        options.add(new SegueOption("Add", AddAssistantsOnShitMenu.class, this, scanner, bookingManager));
        options.add(new SegueOption("Delete", DeleteAssistantOnShiftMenu.class, this, scanner, bookingManager));

        // Bookings
        options.add(new BaseMenuOption("To manage Bookings:"));
        options.add(new SegueOption("List", SelectBookingsList.class, this, scanner, bookingManager));
        options.add(new SegueOption("Add", AddBookingMenu.class, this, scanner, bookingManager));
        options.add(new SegueOption("Delete", DeleteBookingMenu.class, this, scanner, bookingManager));
        options.add(new SegueOption("Conclude", ConcludeBookingMenu.class, this, scanner, bookingManager));
    }

    public void setBookingManager(BookingManager bookingManager) {
        this.bookingManager = bookingManager;
    }
}
