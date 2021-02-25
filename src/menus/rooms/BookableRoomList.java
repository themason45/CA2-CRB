package menus.rooms;

import menus.MainMenu;
import support.BookingManager;
import support.menu.BaseListMenu;

import java.util.Scanner;

/**
 * Displays a list of {@link models.Room}s that are bookable, along with the timeslots that they are bookable
 */
public class BookableRoomList extends BaseListMenu {
    public BookableRoomList(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);

        this.list = bookingManager.formattedBookableRooms();
        this.title = "bookable rooms";
    }

    @Override
    public void draw() {
        super.draw();
    }
}
