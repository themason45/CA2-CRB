package main.support;

import main.BookingApp;
import main.models.*;
import main.support.menu.BaseMenuOption;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.stream.Collectors;

/**
 * This creates bookings in rooms, for a given university
 *
 * <ul>
 *     <li>The booking system is responsible for most functionalities. It has a list of bookable rooms, a list of assistants on shift, and a list of bookings.</li>
 *     <li>This class must be able to manage general functionalities on these lists, i.e., you should implement functions to add, remove, and to show bookable rooms, assistants on shift, and bookings.</li>
 *     <li>There is a time-slot concept that will guide the booking system. For instance, rooms will be available, and assistants will work at a specific time-slot, i.e., date, time and duration. Hence, tests should be booked at available slots.</li>
 *     <li>
 * 4. Every time-slot has a fixed duration – a positive number representing the duration of a test, in minutes. This quantity includes the time spent doing the test and the time to sanitize the room. The current policy establishes this duration to be 60 minutes.</li>
 * </ul>
 * <p>
 * Time slots should be 60 minutes apart
 */
public class BookingManager {
    private ArrayList<TimeSlot> timeSlots;

    private ArrayList<Booking> bookings;
    private University university;

    public BookingManager(University university) {
        this.university = university;
        this.timeSlots = new ArrayList<>();
        this.bookings = new ArrayList<>();
        generateTimeSlots();
    }

    private void generateTimeSlots() {
        int weeksAhead = Integer.parseInt((String) BookingApp.appProps().get("weeks.ahead"));
        int dow = LocalDate.now().getDayOfWeek().getValue();
        LocalDate currentDate = LocalDate.now().minusDays(dow - 1); // Get date of the week beginning
        // For each week
        for (int i = 0; i < weeksAhead; i++) {
            // For each day
            for (int j = 0; j < 7; j++) {
                // Create a time slot for that day
                timeSlots.addAll(TimeSlot.slotsForDate(currentDate));
                currentDate = currentDate.plusDays(1);
            }
        }
    }

    public void createBooking(TimeSlot timeSlot, Room room, Assistant assistant, String studentEmail) {
        ModelWrapper<Booking> bookingModelWrapper = new ModelWrapper<>();
        Booking booking = new Booking(bookingModelWrapper.nextPk(bookings), timeSlot, room, assistant, studentEmail);

        // Add bookings to the room, and assistant, so that they can track back along the relation
        room.addBooking(booking);
        assistant.addBooking(booking);

        // Update the existing lists
        new ModelWrapper<Room>().updateArr(university.rooms, room);
        new ModelWrapper<Assistant>().updateArr(university.assistants, assistant);

        // Add this booking the the manager's list of bookings
        bookings.add(booking);
    }

    public ArrayList<Assistant> availableAssistants(TimeSlot timeSlot) {
        return university.assistants.stream().filter(x -> x.checkAvailability(timeSlot)).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Room> availableRooms(TimeSlot timeSlot) {
        return university.rooms.stream().filter(x -> x.checkAvailability(timeSlot)).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<TimeSlot> getTimeSlots() {
        return this.timeSlots;
    }

    public ArrayList<Room> bookableRooms() {
        return this.university.rooms.stream().filter(x -> x.bookable).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Assistant> getAssistants() {
        return university.assistants;
    }

    public ArrayList<BaseMenuOption> tsAssistantOptionMap() {
        ArrayList<BaseMenuOption> ol = new ArrayList<>();

        ArrayList<Assistant> assistants = this.getAssistants();
        for (TimeSlot ts : this.getTimeSlots()) {
            ArrayList<BaseMenuOption> mapped = assistants.stream().map(x ->
                    new BaseMenuOption(String.format("%s | %s | %s",
                            ts.getFormattedStartTime(), x.checkAvailability(ts) ? "FREE" : "BUSY", x.getEmail()),
                            BookingManager.class, "addToShift", x))
                    .collect(Collectors.toCollection(ArrayList::new));
            ol.addAll(mapped);
        }

        return ol;
    }

    public ArrayList<String> tsAssistantMap() {
        ArrayList<String> ol = new ArrayList<>();

        ArrayList<Assistant> assistants = this.getAssistants();
        for (TimeSlot ts : this.getTimeSlots()) {
            ArrayList<String> mapped = assistants.stream().map(x ->
                    String.format("%s | %s | %s", ts.getFormattedStartTime(), x.checkAvailability(ts) ? "FREE" : "BUSY",
                            x.getEmail())).collect(Collectors.toCollection(ArrayList::new));
            ol.addAll(mapped);
        }
        return ol;
    }

        /**
         * This method is invoked by {@link main.menus.AddAssistantsOnShitMenu#performCreation(String)}.
         *
         * @param assistant The assistant to be put on shift
         */
        @SuppressWarnings("unused")
        public void addToShift(Assistant assistant, LocalDate localDate){
            int dow = localDate.getDayOfWeek().getValue();
            assistant.addDayActive(dow);

            university.assistants = new ModelWrapper<Assistant>().updateArr(university.assistants, assistant);
        }
    }
