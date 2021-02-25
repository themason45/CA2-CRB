package menus.bookings;

import menus.MainMenu;
import models.Booking;
import support.BookingManager;
import support.menu.BaseListMenu;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Displays a list of bookings given a status which is passed in the constructor
 */
public class BookingsList extends BaseListMenu {

    public BookingsList(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager, Integer status) {
        super(previousMenu, scanner, bookingManager);
        this.bookingManager = bookingManager;

        switch (status) {
            case 0 -> {
                title = "all bookings";
                list = bookingManager.getBookings();
            }
            case 1 -> {
                title = "scheduled bookings";
                list = bookingManager.getBookings().stream().filter(x -> !x.getIsCompleted()).
                        collect(Collectors.toCollection(ArrayList::new));
            }
            case 2 -> {
                title = "completed bookings";
                list = bookingManager.getBookings().stream().filter(Booking::getIsCompleted).
                        collect(Collectors.toCollection(ArrayList::new));
            }
        }
    }
}
