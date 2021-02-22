package main.menus;

import main.support.BookingManager;
import main.support.menu.BaseListMenu;
import main.support.menu.BaseMenu;

import java.util.Scanner;

public class BookingsList extends BaseListMenu {
    BookingManager bookingManager;

    public BookingsList(BaseMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner);
        this.bookingManager = bookingManager;

        this.title = "";
    }
}
