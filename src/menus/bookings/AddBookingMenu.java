package menus.bookings;

import menus.MainMenu;
import models.Assistant;
import models.Booking;
import support.BookingManager;
import support.menu.BaseCreateMenu;
import support.menu.BaseMenuOption;

import java.util.Scanner;
import java.util.zip.DataFormatException;

public class AddBookingMenu extends BaseCreateMenu {

    public AddBookingMenu(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);
        this.bookingManager = bookingManager;

        this.list = bookingManager.getTimeSlotOptions();

        this.title = "Adding booking (appointment for a COVID test) to the system";
        this.instructions = "The sequential ID of an available time-slot and the student email, separated by a white space.";
    }

    @Override
    public void performCreation(String input) {
        String[] split = input.split(" ");
        if (split.length != 2) try {
            throw new DataFormatException("Data inputted here must be in the format ID EMAIL");
        } catch (DataFormatException e) {
            this.error(e);
        }

        if (!Assistant.checkEmail(split[1])) try {
            throw new DataFormatException("The given email is not in the correct format");
        } catch (DataFormatException e) {
            this.error(e);
        }

        int listIndex = Integer.parseInt(split[0]);
        BaseMenuOption option = (BaseMenuOption) list.get(listIndex - offset);

        try {
            Booking booking = (Booking) option.executeOnInstance(bookingManager, split[1]);
            this.redrawWithMessage(String.format("Booking added successfully:\n%s", booking));
        } catch (Exception e) {
            this.error(e);
        }
    }
}
