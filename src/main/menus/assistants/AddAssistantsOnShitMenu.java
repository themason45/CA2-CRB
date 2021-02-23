package main.menus.assistants;

import main.menus.MainMenu;
import main.support.BookingManager;
import main.support.menu.BaseCreateMenu;
import main.support.menu.BaseMenuOption;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.IllegalFormatException;
import java.util.Scanner;
import java.util.zip.DataFormatException;

public class AddAssistantsOnShitMenu extends BaseCreateMenu {

    public AddAssistantsOnShitMenu(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);
        this.bookingManager = bookingManager;

        this.list = bookingManager.tsAssistantOptionMap();
        this.title = "assistants on shift";
        this.instructions = "The sequential ID of an assistant and date (dd/mm/yyyy), separated by a white space.";
    }

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
