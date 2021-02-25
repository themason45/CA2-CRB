package menus.assistants;

import menus.MainMenu;
import support.BookingManager;
import support.menu.BaseListMenu;

import java.util.Scanner;

/**
 * Displays a list of {@link models.Assistant} that are on shift at each {@link support.TimeSlot}
 * that have assistants on shift at.
 */
public class AssistantsOnShiftList extends BaseListMenu {
    public AssistantsOnShiftList(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner, bookingManager);

        this.list = bookingManager.tsAssistantMap();
        this.title = "Assistants on shift";
    }

    @Override
    public void draw() {
        super.draw();
    }
}
