package main.menus.rooms;

import main.menus.MainMenu;
import main.support.BookingManager;
import main.support.menu.BaseListMenu;

import java.util.Scanner;

public class BookableRoomList extends BaseListMenu {
    public BookableRoomList(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner);

        this.list = bookingManager.bookableRooms();
        this.title = "Bookable rooms";
    }

    @Override
    public void draw() {
        super.draw();
    }
}
