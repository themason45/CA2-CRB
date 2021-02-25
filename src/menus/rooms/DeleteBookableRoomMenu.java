package menus.rooms;

import menus.MainMenu;
import support.BookingManager;
import support.menu.BaseDeleteMenu;

import java.util.Scanner;

/**
 * Lets the user select a {@link models.Room}, paired with a {@link support.TimeSlot}, which will make it un-bookable
 * at that time.
 */
public class DeleteBookableRoomMenu extends BaseDeleteMenu {
    public DeleteBookableRoomMenu(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);

        this.title = "bookable room";

        this.list = bookingManager.deleteBookableRoomOptions();
    }

    /**
     * Updates the list to be displayed once the deletion has finished
     */
    @Override
    public void postDelete() {
        this.list = bookingManager.deleteBookableRoomOptions();
    }

    /**
     * @param obj The object to be displayed in the on complete message
     * @return A string telling the user that the given item has been deleted successfully
     */
    @Override
    public String generateOnCompleteMessage(Object obj) {
        return String.format("Bookable Room removed successfully:\n%s", obj.toString());
    }
}
