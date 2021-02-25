package models;

import support.ModelWrapper;
import support.TimeSlot;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * Room object is defined as follows:
 * <ul>
 *     <li>The university has several rooms, and some of the rooms can be allocated to apply COVID tests.</li>
 *     <li>A room must have a string code (e.g., IC215) and a capacity.</li>
 *     <li>The code is used to identify the room and, therefore, must be unique.</li>
 *     <li>The capacity must be an integer value greater than zero. It represents the number of concurrent assistants that can be safely allocated in the room to perform tests.</li>
 *     <li>Print template: | code | capacity: capacity |</li>
 * </ul>
 *
 * Regarding bookable rooms:
 * <ul>
 *     <li>A bookable room is a room registered by the university that can be effectively used for tests. As the name suggests, it is a room available for booking.</li>
 *     <li>A bookable room is a room allocated in a specific time-slot (dd/mm/yyyy HH:MM). Since rooms are available from 7 AM - 10 AM, the system will offer at most three bookable rooms (time-slots) per room per day.</li>
 *     <li>A bookable room has an occupancy and, depending on the room’s capacity, its status can be:
 *     <ul>
 *         <li>EMPTY – when occupancy is zero.</li>
 *         <li>AVAILABLE – when occupancy is less than the room capacity.</li>
 *         <li>FULL – when occupancy is equal to the room capacity.</li>
 *     </ul>
 *     </li>
 *     <li>The occupancy can never be bigger than the room capacity.</li>
 *     <li>Only EMPTY bookable rooms can be removed from the system.</li>
 *     <li>The status of a bookable room must be updated whenever its occupancy changes.</li>
 *     <li>Print template: | dd/mm/yyyy HH:MM | status | room_code | Occupancy: occupancy |</li>
 * </ul>
 */
public class Room extends BaseModel {
    private final String roomCode;
    private final int capacity;
    private University university;

    public ArrayList<TimeSlot> bookableTimeslots;
    private final ArrayList<Booking> bookings;

    ArrayList<Integer> bookableTimeSlotIndices;


    public Room(int pk, String roomCode, int capacity, ArrayList<Integer> timeSlotIndices) {
        super(pk);
        this.roomCode = roomCode;
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }

        this.bookableTimeSlotIndices = timeSlotIndices;

        this.bookableTimeslots = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    /**
     * @param timeSlot The timeslot to check if it is bookable at
     * @return True if the room is bookable at that timeslot
     */
    public boolean isBookableAt(TimeSlot timeSlot) {
        return bookableTimeslots.contains(timeSlot);
    }

    /**
     * @param timeSlot The {@link TimeSlot} to check the availability of a room at
     * @return True if the room is available at that given timeslot
     */
    public boolean checkAvailability(TimeSlot timeSlot) {
        if (!isBookableAt(timeSlot)) return false;

        // Check if there are no bookings with the current time slot
        boolean noBookings = true;
        for (Booking booking : bookings.stream().filter(x -> !x.getIsCompleted()).collect(Collectors.toList())) {
            noBookings = booking.getTimeSlot() != timeSlot;
        }
        return noBookings;
    }

    public String toString() {
        return String.format("| %s | Capacity: %s |", roomCode, capacity);
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public String getRoomCode() {
        return this.roomCode;
    }

    /**
     * @param booking The booking to be added to the assistant
     */
    public void addBooking(Booking booking) {
        if (this.checkAvailability(booking.getTimeSlot())) {
            this.bookings.add(booking);
        } else {
            throw new IllegalArgumentException("This room is not bookable");
        }
    }

    /**
     * @param booking The booking to be updated (updates when status of the booking changes)
     */
    public void updateBooking(Booking booking) {
        new ModelWrapper<Booking>().updateArr(bookings, booking);
    }

    /**
     * @param booking The booking to be removed from the assistant
     */
    public void removeBooking(Booking booking) {
        this.bookings.remove(booking);
    }

    public Integer[] getBookableTimeSlotIndices() {
        return bookableTimeSlotIndices.toArray(new Integer[0]);
    }

    /**
     * @param timeSlot The timeslot to find the status for
     * @return The status of the room at a given timeslot
     */
    public String status(TimeSlot timeSlot) {
        // Bookable room stuff
        int occupancy = getOccupancy(timeSlot);
        if (occupancy == 0) return "EMPTY";
        if (occupancy < capacity) return "AVAILABLE";
        if (occupancy == capacity) return "FULL";
        else return null;
    }

    /**
     * @param timeSlot The {@link TimeSlot} to check the rooms occupancy at
     * @return The number of scheduled bookings in the room at a given timeslot
     */
    public int getOccupancy(TimeSlot timeSlot) {
        return (int) this.bookings.stream()
                .filter(x -> !x.getIsCompleted())
                .filter(x -> x.getTimeSlot() == timeSlot).count();
    }

    /**
     * @param ts The timeslot to return the room in a bookable format
     * @return A string in the form | start timeslot | the status | room code | occupancy |
     */
    public String toBookableRoomString(TimeSlot ts) {
        return String.format(
                "| %s | %s | %s | occupancy: %d |", ts.getFormattedStartTime(), this.status(ts),
                this.getRoomCode(), this.getOccupancy(ts));
    }

    public University getUniversity() {
        return university;
    }
}
