package main.models;

import java.util.ArrayList;

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
    private String roomCode;
    private int capacity;
    private University university;

    // Bookable room stuff
    private int occupancy;
    private boolean bookable;
    private ArrayList<Booking> bookings;

    public Room(int pk, String roomCode, int capacity, boolean bookable) {
        super(pk);
        this.roomCode = roomCode;
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
        this.bookable = bookable;
        this.bookings = new ArrayList<>();
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

    public void addBooking(Booking booking) {
        if (this.bookable) {
            this.bookings.add(booking);
        } else {
            throw new IllegalArgumentException("This room is not bookable");
        }
    }
}
