package main.support.menu;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.stream.Collectors;


/**
 * Extension of {@link BaseMenu} which prioritises the displaying of lists over interaction, this can be used to
 * list out specific objects (using their <code>.toString()</code> method), such as {@link main.models.Room} objects.
 */
public class BaseListMenu extends BaseMenu {
    public int offset = 11;
    public ArrayList<?> list;

    public String fullTitle;

    public BaseListMenu(BaseMenu previousMenu, Scanner scanner) {
        super(previousMenu, scanner);
    }

    public PrintStream renderList(PrintStream stream) {
        int i = offset;
        for (Object option : list) {
            stream.printf("\t %d.\t %s\n", i, option.toString());
            i++;
        }
        return stream;
    }

    /**
     * Same as {@link BaseMenu#printStream()}, but outputs a different style of view.
     * Most notably, the list items start at number 11, not 1.
     *
     * @return A stream which can be converted to a string, this is what will be displayed on screen
     */
    public ByteArrayOutputStream printStream() {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        PrintStream printStream = new PrintStream(byteArrayOutputStream);

        printStream.print("University of Knowledge - COVID Test \n\n");
        if (this.fullTitle == null) {
            //noinspection ConstantConditions
            printStream.println(fullTitle);
        } else {
            printStream.printf("List of %s:\n", title);
        }
        printStream = renderList(printStream);

        printStream.println();  // Add trailing new line
        printStream.print("""
                0. Back to main menu.
                -1. Quit application.
                """);

        return byteArrayOutputStream;
    }

    /**
     * @param input The input number from the user
     * @return The desired option
     */
    public BaseMenuOption decodeOption(int input) {
        ArrayList<BaseMenuOption> optionList = this.options.stream().filter(x -> x.selectable).
                collect(Collectors.toCollection(ArrayList::new));
        return optionList.get(input - offset);
    }

    @Override
    public void draw() {
        super.draw();
    }
}
