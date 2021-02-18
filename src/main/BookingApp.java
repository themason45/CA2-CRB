package main;

import main.models.Assistant;
import main.models.University;
import main.support.BookingManager;
import main.support.TimeSlot;

import java.io.File;
import java.io.FileNotFoundException;
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
        // TODO: Implement all the malarkey here
        // TODO: Add remaining values to csv
        // Create a University instance
        University university = new University(0);

        // Populate with CSV data
        ClassLoader cl = getClassloader();
        university.populateRooms(parseCsv(cl.getResourceAsStream("data/rooms.csv")));
        university.populateAssistants(parseCsv(cl.getResourceAsStream("data/assistants.csv")));

        // Create a BookingManager instance
        BookingManager bookingManager = new BookingManager(university);

        Assistant assistant = university.getAssistants().get(0); // Grab first assistant

        bookingManager.createBooking(bookingManager.getTimeSlots().get(0), university.rooms.get(4), assistant, "sam@uok.ac.uk");
        for (TimeSlot ts : bookingManager.getTimeSlots()) {
            System.out.printf("%s | Available: %s \n", ts, assistant.checkAvailability(ts));
        }

        // This booking manager should hold all of the functions required to do stuff

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
     * @return Parsed input list into a Java array where each value is a double
     */
    public static ArrayList<Double> parseSublistAsDoubles(String input) {
        String[] stringList =  input.replaceAll("[\\[\\]]", "").split(" ");
        return (ArrayList<Double>) Arrays.stream(stringList).map(Double::parseDouble).collect(Collectors.toList());
    }

    /**
     * @param input String in the format [a b c d]
     * @return Parsed input list into a Java array where each value is an integer
     */
    public static ArrayList<Integer> parseSublistAsInts(String input) {
        String[] stringList =  input.replaceAll("[\\[\\]]", "").split(" ");
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
