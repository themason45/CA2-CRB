package menus.rooms;

import menus.MainMenu;
import models.Room;
import support.BookingManager;
import support.TimeSlot;
import support.menu.BaseCreateMenu;
import support.menu.BaseMenuOption;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;
import java.util.zip.DataFormatException;

public class AddBookableRoomMenu extends BaseCreateMenu {

    public AddBookableRoomMenu(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);

        this.list = bookingManager.getRooms().stream()
                .map(room -> new BaseMenuOption(room.toString(), BookingManager.class, "addBookableRoom",
                        room)).collect(Collectors.toCollection(ArrayList::new));

        this.title = "bookable rooms";

        this.instructions = "The sequential ID listed to a room, a date (dd/mm/yyyy), and a time (HH:MM), separated by a white space.";
    }

    @Override
    public void performCreation(String input) {
        String[] split = input.split(" ");

        if (split.length != 3) try {
            throw new DataFormatException("Data inputted here must be in the format ID dd/mm/yyyy HH:MM");
        } catch (DataFormatException e) {
            this.error(e);
        }

        int listIndex = Integer.parseInt(split[0]);
        BaseMenuOption option = (BaseMenuOption) list.get(listIndex - offset);

        LocalDate date = LocalDate.parse(split[1], DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalTime time = LocalTime.parse(split[2], DateTimeFormatter.ofPattern("H:m"));
        try {
            Room bookableRoom = (Room) option.executeOnInstance(bookingManager, date, time);

            this.redrawWithMessage(String.format("Bookable Room added successfully: \n%s",
                    bookableRoom.toBookableRoomString(bookingManager.getTimeSlotForStartTime(
                            TimeSlot.cleanDateTime(date, time))))
            );
        } catch (Exception e) {
            this.error(e);
        }
    }
}
