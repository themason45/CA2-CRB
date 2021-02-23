package main;

import main.menus.MainMenu;
import main.models.University;
import main.support.BookingManager;
import main.support.TimeSlot;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Properties;
import java.util.Scanner;
import java.util.stream.Collectors;

/**
 * Starting conditions:
 * <ul>
 *     <li>3 rooms</li>
 *     <li>3 assistants</li>
 *     <li>9 bookable room</li>
 *     <li>6 assistants on shift</li>
 *     <li>Create bookings such that:
 *          <ul>
 *              <li>The system has at least one booking SCHEDULED and one COMPLETED.</li>
 *              <li>The system has at least one bookable room FULL, one AVAILABLE, and one EMPTY.</li>
 *              <li>The system has at least one assistant on shift FREE and one BUSY.</li>
 *          </ul>
 *     </li>
 * </ul>
 */
public class BookingApp {
    /**
     * @param is Input stream
     * @return Parsed CSV
     */
    public static String[][] parseCsv(InputStream is) {
        Scanner scanner = new Scanner(is);
        String[][] output = new String[][]{};

        scanner.nextLine();  // Skip header
        while (scanner.hasNext()) {
            String line = scanner.nextLine();
            String[][] newArr = Arrays.copyOf(output, output.length + 1);
            newArr[output.length] = (line.replace(", ", ",").split(","));
            output = newArr;
        }
        scanner.close();
        return output;
    }

    public static void main(String[] args) {
        // TODO: Add remaining values to csv
        // Create a University instance
        University university = new University(0);

        // Populate with CSV data
        ClassLoader cl = getClassloader();
        university.populateRooms(parseCsv(cl.getResourceAsStream("data/rooms.csv")));
        university.populateAssistants(parseCsv(cl.getResourceAsStream("data/assistants.csv")));

        // Create a BookingManager instance
        BookingManager bookingManager = new BookingManager(university);

        // Create bookings at the start, and end of the week, so we can have a scheduled, and a complete one
        TimeSlot lastTimeSlot = bookingManager.getTimeSlots().get(bookingManager.getTimeSlots().size() -1);
        bookingManager.createBooking(lastTimeSlot, "sam@uok.ac.uk");
        bookingManager.createBooking(bookingManager.getTimeSlots().get(0), "sam@uok.ac.uk");

        // Create a booking that lets us have a full room (Room 4 has a capacity of 1)
        bookingManager.createBooking(bookingManager.getTimeSlots().get(9),"jim@uok.ac.uk",
                bookingManager.getRooms().get(4), bookingManager.getAssistants().get(1));

        Scanner scanner = new Scanner(System.in);

        MainMenu mainMenu = new MainMenu(bookingManager, scanner);
        mainMenu.draw();
    }

    /**
     * @param input String in the format [a b c d]
     * @return Parsed input list into a Java array
     */
    public static String[] parseSublist(String input) {
        return input.replaceAll("[\\[\\]]", "").split(" ");
    }


    /**
     * @param input String in the format [a b c d]
     * @return Parsed input list into a Java array where each value is an integer
     */
    public static ArrayList<Integer> parseSublistAsInts(String input) {
        String[] stringList = input.replaceAll("[\\[\\]]", "").split(" ");
        return (ArrayList<Integer>) Arrays.stream(stringList).map(Integer::parseInt).collect(Collectors.toList());
    }

    protected static ClassLoader getClassloader() {
        return BookingApp.class.getClassLoader();
    }

    public static Properties appProps() {
        ClassLoader cl = getClassloader();

        // Load properties for the project
        Properties properties = new Properties();
        try {
            properties.load(cl.getResourceAsStream("config.properties"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return properties;
    }
}
