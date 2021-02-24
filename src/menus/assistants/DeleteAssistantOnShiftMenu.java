package menus.assistants;

import menus.MainMenu;
import support.BookingManager;
import support.menu.BaseDeleteMenu;

import java.util.Scanner;

public class DeleteAssistantOnShiftMenu extends BaseDeleteMenu {
    public DeleteAssistantOnShiftMenu(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);

        this.title = "assitant on shift";

        this.methodName = "removeFromShift";

        this.list = bookingManager.tsAssistantOptionMap(false);
    }

    @Override
    public void postDelete() {
        // Update the list
        this.list = bookingManager.tsAssistantOptionMap(false);
    }
}
