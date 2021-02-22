package main.menus;

import main.models.Assistant;
import main.support.BookingManager;
import main.support.TimeSlot;
import main.support.menu.BaseListMenu;

import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;

public class AssistantsOnShiftList extends BaseListMenu {
    public AssistantsOnShiftList(MainMenu previousMenu, Scanner scanner, BookingManager bookingManager) {
        super(previousMenu, scanner);

        this.list = bookingManager.tsAssistantMap();
        this.title = "Assistants on shift";
    }

    @Override
    public void draw() {
        super.draw();
    }
}
