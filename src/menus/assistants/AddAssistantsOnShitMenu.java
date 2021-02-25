package menus.assistants;

import menus.MainMenu;
import models.Assistant;
import support.BookingManager;
import support.menu.BaseCreateMenu;
import support.menu.BaseMenuOption;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Scanner;
import java.util.zip.DataFormatException;

/**
 * Adds a day of the week to the selected {@link models.Assistant} at the given date (which creates a corresponding
 * {@link support.TimeSlot}).
 */
public class AddAssistantsOnShitMenu extends BaseCreateMenu {

    public AddAssistantsOnShitMenu(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);
        this.bookingManager = bookingManager;

        this.list = bookingManager.tsAssistantOptionMap();
        this.title = "assistants on shift";
        this.instructions = "The sequential ID of an assistant and date (dd/mm/yyyy), separated by a white space.";
    }

    /**
     * Invokes the {@link BookingManager#addToShift(Assistant, LocalDate)} method on the current instance, with the given
     * attributes.
     *
     * @param input The string inputted by the user
     */
    @Override
    public void performCreation(String input) {
        String[] split = input.split(" ");
        if (split.length != 2) try {
            throw new DataFormatException("Data inputted here must be in the format ID dd/mm/yy");
        } catch (DataFormatException e) {
            this.error(e);
        }

        int listIndex = Integer.parseInt(split[0]);

        BaseMenuOption option = (BaseMenuOption) list.get(listIndex - offset);

        // Add the time shift to the assistant through the booking manager
        try {
            option.executeOnInstance(bookingManager, LocalDate.parse(split[1], DateTimeFormatter.ofPattern("dd/MM/yyyy")));
        } catch (Exception e) {
            this.error(e);
        }
    }
}
