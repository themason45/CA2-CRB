package support;

import menus.assistants.AddAssistantsOnShitMenu;
import models.Assistant;
import models.Room;
import models.University;
import support.menu.BaseMenuOption;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.stream.Collectors;

//TODO: Tidy this bloody file up

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
public class BookingManager extends BaseBookingManager {

    public BookingManager(University university) {
        super(university);
        // Generate timeslots, and allocate timeslots to the respective rooms
        generateTimeSlots();
        generateRoomTimeslots();
    }

    // Time slot related methods

    /**
     * Generate a list of timeSlots for n weeks ahead, with the correct number of timeslots per day,
     * this can all be changed in the <i>config.properties</i> file.
     */
    private void generateTimeSlots() {
        int weeksAhead = Integer.parseInt((String) Support.appProps().get("weeks.ahead"));
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

    /**
     * This takes the preset indices for time slots (defined in the CSV files), and adds the corresponding
     * time slot to the room's bookable timeslots
     */
    private void generateRoomTimeslots() {
        for (Room room : university.rooms) {
            Integer[] indices = room.getBookableTimeSlotIndices();
            for (Integer index : indices) {
                room.bookableTimeslots.add(timeSlots.get(index));
            }
        }
    }

    /**
     * @return An arraylist of {@link TimeSlot}s for the current instance
     */
    public ArrayList<TimeSlot> getTimeSlots() {
        return this.timeSlots;
    }


    /**
     * @return An ArrayList of timeslots which have available assistants, and rooms
     */
    public ArrayList<TimeSlot> getAvailableTimeSlots() {
        return this.timeSlots.stream()
                .filter(x -> (this.availableAssistants(x).size() > 0 & this.availableRooms(x).size() > 0))
                .collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * @return An ArrayList of MenuOptions that are preloaded for that timeslot, and will automatically pass it forward
     * to the {@link #createBooking(TimeSlot, String)} method.
     */
    public ArrayList<BaseMenuOption> getTimeSlotOptions() {
        return this.getAvailableTimeSlots().stream().map(x -> new BaseMenuOption(x.getFormattedStartTime(),
                this.getClass(), "createBooking", x)).collect(Collectors.toCollection(ArrayList::new));
    }

    /**
     * This function does not rely on direct user input, first the data is cleaned.
     * It is used in the process for converting user inputted Dates, and Times.
     * <br/>
     * See {@link TimeSlot#cleanDateTime(LocalDate, LocalTime)} for how it is cleaned up ready for compatible
     * searching
     * 
     * @param dateTime Start time to match a timeSlot to
     * @return The time slot with the matching start time
     */
    public TimeSlot getTimeSlotForStartTime(LocalDateTime dateTime) {
        return this.getTimeSlots().stream().filter(x -> x.start.isEqual(dateTime)).findFirst().orElse(null);
    }

    // Bookable room related methods

    /**
     * Generates the arraylist of {@link support.menu.BaseMenuOption} which call the
     * {@link #removeBookableRoom(Room, TimeSlot)} function on a, usually given, instance
     *
     * @return An ArrayList of {@link support.menu.BaseMenuOption} for deleting bookable rooms
     */
    public ArrayList<BaseMenuOption> deleteBookableRoomOptions() {
        ArrayList<BaseMenuOption> deleteBookableRoomOptions = new ArrayList<>();

        for (TimeSlot ts : this.getTimeSlots()) {
            deleteBookableRoomOptions.addAll(bookableRooms(ts).stream().filter(x -> x.status(ts).equals("EMPTY"))
                    .map(room -> new BaseMenuOption(room.toBookableRoomString(ts), BookingManager.class,
                            "removeBookableRoom", room, ts))
                    .collect(Collectors.toCollection(ArrayList::new)));
        }
        return deleteBookableRoomOptions;
    }

    /**
     * @return An ArrayList of strings with the formatted details of the room in a bookable way
     */
    public ArrayList<String> formattedBookableRooms() {
        ArrayList<String> formattedBookableRooms = new ArrayList<>();

        for (TimeSlot ts : this.getTimeSlots()) {
            formattedBookableRooms.addAll(bookableRooms(ts).stream().map(room -> room.toBookableRoomString(ts))
                    .collect(Collectors.toCollection(ArrayList::new)));
        }
        return formattedBookableRooms;
    }

    /**
     * @param room The room to make bookable
     * @param date The date to make the room bookable on
     * @param time The time to make the room bookable at
     * @return The updated room, note that the given instance's room list is also updated
     */
    @SuppressWarnings("unused")
    public Room addBookableRoom(Room room, LocalDate date, LocalTime time) {
        int roomIndex = this.university.rooms.indexOf(room);

        TimeSlot timeSlot = this.getTimeSlotForStartTime(TimeSlot.cleanDateTime(date, time));
        room.bookableTimeslots.add(timeSlot);

        this.university.rooms.set(roomIndex, room);

        return room;
    }

    /**
     * @param room The room to make un-bookable
     * @param timeSlot The timeslot to remove the bookable status from
     */
    @SuppressWarnings("unused")
    public void removeBookableRoom(Room room, TimeSlot timeSlot) {
        room.bookableTimeslots.remove(timeSlot);

        university.rooms = new ModelWrapper<Room>().updateArr(university.rooms, room);
    }

    // Assistant related methods

    /**
     * @return An ArrayList of the current instance's {@link Assistant}s.
     */
    public ArrayList<Assistant> getAssistants() {
        return university.assistants;
    }

    /**
     * Overflow for {@link #tsAssistantOptionMap(boolean)}, where creation is true
     *
     * @return An ArrayList of {@link BaseMenuOption}s which combine Assistants, and Time slots, and have the function
     * {@link BookingManager#addToShift(Assistant, LocalDate)}, with the local date already filled
     */
    public ArrayList<BaseMenuOption> tsAssistantOptionMap() {
        return tsAssistantOptionMap(true);
    }

    /**
     * @return An ArrayList of {@link BaseMenuOption}s which combine Assistants, and Time slots, and have the function
     * {@link BookingManager#addToShift(Assistant, LocalDate)}, with the local date already filled
     */
    public ArrayList<BaseMenuOption> tsAssistantOptionMap(boolean creation) {
        ArrayList<BaseMenuOption> ol = new ArrayList<>();

        for (TimeSlot ts : this.getTimeSlots()) {
            ArrayList<Assistant> assistants = this.availableAssistants(ts);

            ArrayList<BaseMenuOption> mapped = assistants.stream().map(x ->
                    creation ?
                            new BaseMenuOption(x.onShiftDescriptionString(ts),
                                    BookingManager.class, "addToShift", x) :
                            new BaseMenuOption(x.onShiftDescriptionString(ts),
                                    BookingManager.class, "removeFromShift", x, ts)
            ).collect(Collectors.toCollection(ArrayList::new));
            ol.addAll(mapped);
        }

        return ol;
    }

    /**
     * Maps together the assistants and time slots in order to display them in the
     * {@link menus.assistants.AssistantsOnShiftList} menu.
     *
     * @return An ArrayList of Strings which combine Assistants, and Time slots
     */
    public ArrayList<String> tsAssistantMap() {
        ArrayList<String> ol = new ArrayList<>();

        for (TimeSlot ts : this.getTimeSlots()) {
            ArrayList<Assistant> assistants = this.availableAssistants(ts);

            ArrayList<String> mapped = assistants.stream().map(x -> x.onShiftDescriptionString(ts))
                    .collect(Collectors.toCollection(ArrayList::new));
            ol.addAll(mapped);
        }
        return ol;
    }

    /**
     * This method is invoked by {@link AddAssistantsOnShitMenu#performCreation(String)}.
     * Note that shifts for Assistants reoccur weekly, so removing them from shift this week, will add them to
     * all subsequent weeks. Only the day of the week is stored (0 = Monday).
     *
     * @param assistant The assistant to be put on shift
     */
    @SuppressWarnings("unused")
    public void addToShift(Assistant assistant, LocalDate localDate) {
        int dow = localDate.getDayOfWeek().getValue();
        assistant.addDayActive(dow);

        university.assistants = new ModelWrapper<Assistant>().updateArr(university.assistants, assistant);
    }

    /**
     * Note that shifts for Assistants reoccur weekly, so removing them from shift this week, will remove them from
     * all subsequent weeks. Only the day of the week is stored (0 = Monday).
     *
     * @param assistant The assistant to take off shift
     * @param timeSlot The timeslot to remove them from
     */
    @SuppressWarnings("unused")
    public void removeFromShift(Assistant assistant, TimeSlot timeSlot) {
        int dow = timeSlot.start.getDayOfWeek().getValue();
        assistant.removeDayActive(dow);

        university.assistants = new ModelWrapper<Assistant>().updateArr(university.assistants, assistant);
    }

    /**
     * @return An ArrayList of the current instance's {@link Room}s.
     */
    public ArrayList<Room> getRooms() {
        return university.rooms;
    }
}
