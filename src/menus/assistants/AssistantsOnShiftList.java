package menus.assistants;

import menus.MainMenu;
import support.BookingManager;
import support.menu.BaseListMenu;

import java.util.Scanner;

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
