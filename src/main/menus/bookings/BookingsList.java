package main.menus.bookings;

import main.menus.MainMenu;
import main.models.Booking;
import main.support.BookingManager;
import main.support.menu.BaseListMenu;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class BookingsList extends BaseListMenu {
    BookingManager bookingManager;

    public BookingsList(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager, Integer status) {
        super(previousMenu, scanner);
        this.bookingManager = bookingManager;
        this.fullTitle = "";

        switch (status) {
            case 0 -> {
                title = "all bookings";
                list = bookingManager.getBookings();
            }
            case 1 -> {
                title = "scheduled bookings";
                list = bookingManager.getBookings().stream().filter(Booking::isCompleted).
                        collect(Collectors.toCollection(ArrayList::new));
            }
            case 2 -> {
                title = "completed bookings";
                list = bookingManager.getBookings().stream().filter(x -> !x.isCompleted()).
                        collect(Collectors.toCollection(ArrayList::new));
            }
        }
    }
}
