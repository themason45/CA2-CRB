package menus.rooms;

import menus.MainMenu;
import support.BookingManager;
import support.menu.BaseDeleteMenu;

import java.util.Scanner;

public class DeleteBookableRoomMenu extends BaseDeleteMenu {
    public DeleteBookableRoomMenu(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);

        this.title = "bookable room";

        this.list = bookingManager.deleteBookableRoomOptions();
    }

    @Override
    public void postDelete() {
        this.list = bookingManager.deleteBookableRoomOptions();
    }

    @Override
    public String generateOnCompleteMessage(Object obj) {
        return String.format("Bookable Room removed successfully:\n%s", obj.toString());
    }
}
