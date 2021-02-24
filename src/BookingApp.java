import menus.MainMenu;
import models.University;
import support.BookingManager;
import support.Support;
import support.TimeSlot;

import java.util.Scanner;

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

    public static void main(String[] args) {
        // Create a University instance
        University university = new University(0);

        // Populate with CSV data
        ClassLoader cl = Support.getClassloader(BookingApp.class);
        university.populateRooms(Support.parseCsv(cl.getResourceAsStream("data/rooms.csv")));
        university.populateAssistants(Support.parseCsv(cl.getResourceAsStream("data/assistants.csv")));

        // Create a BookingManager instance
        BookingManager bookingManager = new BookingManager(university);

        TimeSlot lastTimeSlot = bookingManager.getTimeSlots().get(bookingManager.getTimeSlots().size() -1);
        bookingManager.createBooking(lastTimeSlot, "sam@uok.ac.uk", true);
        bookingManager.createBooking(bookingManager.getTimeSlots().get(0), "sam@uok.ac.uk");

        // Create a booking that lets us have a full room (Room 4 has a capacity of 1)
        bookingManager.createBooking(bookingManager.getTimeSlots().get(9),"jim@uok.ac.uk",
                bookingManager.getRooms().get(4), bookingManager.getAssistants().get(1));

        Scanner scanner = new Scanner(System.in);

        MainMenu mainMenu = new MainMenu(bookingManager, scanner);
        mainMenu.draw();
    }

}
