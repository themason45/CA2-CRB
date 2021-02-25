package menus.assistants;

import menus.MainMenu;
import support.BookingManager;
import support.menu.BaseDeleteMenu;

import java.util.Scanner;

/**
 * Removes an assistant from shift given a sequential ID referring to the list of options
 */
public class DeleteAssistantOnShiftMenu extends BaseDeleteMenu {
    public DeleteAssistantOnShiftMenu(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);

        this.title = "assitant on shift";

        this.methodName = "removeFromShift";

        this.list = bookingManager.tsAssistantOptionMap(false);
    }

    /**
     * @param obj The object to be displayed in the on complete message
     * @return A string telling the user that the given item has been deleted successfully
     */
    @Override
    public String generateOnCompleteMessage(Object obj) {
        return String.format("Assistant on Shift removed successfully:\n%s", obj.toString());
    }

    /**
     * Update the displayed list of assistants on shift once one has been deleted
     */
    @Override
    public void postDelete() {
        // Update the list
        this.list = bookingManager.tsAssistantOptionMap(false);
    }
}
