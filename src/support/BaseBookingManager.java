package support;

import models.Assistant;
import models.Booking;
import models.Room;
import models.University;
import support.menu.BaseMenuOption;

import java.util.ArrayList;
import java.util.stream.Collectors;

/**
 * This class just moves all the booking related stuff out of the {@link BookingManager} class, in order
 * to tidy it up
 */
public class BaseBookingManager {
    protected final ArrayList<TimeSlot> timeSlots;
    protected final University university;
    protected ArrayList<Booking> bookings;

    public BaseBookingManager(University university) {
        this.university = university;

        this.timeSlots = new ArrayList<>();
        this.bookings = new ArrayList<>();
    }

    // Methods for acquiring ArrayLists of specific bits of data
    public ArrayList<Assistant> availableAssistants(TimeSlot timeSlot) {
        return university.assistants.stream().filter(x -> x.checkAvailability(timeSlot)).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Room> availableRooms(TimeSlot timeSlot) {
        return university.rooms.stream().filter(x -> x.checkAvailability(timeSlot)).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<Room> bookableRooms(TimeSlot timeSlot) {
        return university.rooms.stream().filter(x -> x.isBookableAt(timeSlot)).collect(Collectors.toCollection(ArrayList::new));
    }

    // Overflow methods for creating bookings
    public Booking createBooking(TimeSlot timeSlot, String studentEmail) throws IllegalArgumentException {
        return createBooking(timeSlot, studentEmail, false);
    }

    public Booking createBooking(TimeSlot timeSlot, String studentEmail, boolean complete) throws IllegalArgumentException {
        Room room = availableRooms(timeSlot).stream().findFirst().orElse(null);
        Assistant assistant = availableAssistants(timeSlot).stream().findFirst().orElse(null);

        return createBooking(timeSlot, studentEmail, room, assistant, complete);
    }

    public void createBooking(TimeSlot timeSlot, String studentEmail, Room room, Assistant assistant) throws IllegalArgumentException {
        createBooking(timeSlot, studentEmail, room, assistant, false);
    }

    public Booking createBooking(TimeSlot timeSlot, String studentEmail, Room room, Assistant assistant, boolean complete) throws IllegalArgumentException {
        if (room == null) {
            throw new IllegalArgumentException("No rooms available at this time slot");
        } else if (assistant == null) {
            throw new IllegalArgumentException("No assistants available at this time slot");
        }

        ModelWrapper<Booking> bookingModelWrapper = new ModelWrapper<>();
        Booking booking = new Booking(bookingModelWrapper.nextPk(bookings), timeSlot, room, assistant, studentEmail);
        if (complete) booking.completeTest();

        // Add bookings to the room, and assistant, so that they can track back along the relation
        room.addBooking(booking);
        assistant.addBooking(booking);

        // Update the existing lists
        new ModelWrapper<Room>().updateArr(university.rooms, room);
        new ModelWrapper<Assistant>().updateArr(university.assistants, assistant);

        // Add this booking the the manager's list of bookings
        this.bookings.add(booking);

        return booking;
    }

    // Methods for getting lists of bookings
    public ArrayList<Booking> getBookings() {
        return this.bookings;
    }

    @SuppressWarnings("unused")
    public void deleteBooking(Booking booking) throws NullPointerException {
        this.bookings.remove(booking);

        Room room = booking.getRoom();
        room.removeBooking(booking);

        Assistant assistant = booking.getAssistant();
        assistant.removeBooking(booking);

        // Update the existing lists
        new ModelWrapper<Room>().updateArr(university.rooms, room);
        new ModelWrapper<Assistant>().updateArr(university.assistants, assistant);
    }

    public ArrayList<Booking> getScheduledBookings() {
        return getBookings().stream().filter(x -> !x.getIsCompleted()).collect(Collectors.toCollection(ArrayList::new));
    }

    public ArrayList<BaseMenuOption> completeBookingOptions() {
        return this.getScheduledBookings().stream().map(booking ->
                new BaseMenuOption(booking.toString(), BookingManager.class, "completeBooking", booking)
        ).collect(Collectors.toCollection(ArrayList::new));
    }

    // Method for completing a given booking
    @SuppressWarnings("unused")
    public void completeBooking(Booking booking) {
        booking.completeTest();
        bookings = new ModelWrapper<Booking>().updateArr(bookings, booking);

        // Update rooms
        Room room = booking.getRoom();
        room.updateBooking(booking);

        Assistant assistant = booking.getAssistant();
        assistant.updateBooking(booking);

        // Update the existing lists
        new ModelWrapper<Room>().updateArr(university.rooms, room);
        new ModelWrapper<Assistant>().updateArr(university.assistants, assistant);
    }
}
