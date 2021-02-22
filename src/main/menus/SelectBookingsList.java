package main.menus;

import main.support.menu.BaseListMenu;
import main.support.menu.BaseMenu;

import java.util.Scanner;

public class SelectBookingsList extends BaseListMenu {
    public SelectBookingsList(BaseMenu previousMenu, Scanner scanner) {
        super(previousMenu, scanner);

        this.fullTitle = "Select which booking to list:";
    }
}
