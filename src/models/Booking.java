package models;

import support.TimeSlot;

/**
 * <ul>
 *     <li>A booking consists of matching a bookable room and an assistant on shift at a specific time-slot to perform
 *     a COVID-19 test on a student. It is the main function of the system.</li>
 *     <li>A booking has a unique sequential number (identification code -> pk) and the email of the student being tested (enforce “*@uok.ac.uk”).</li>
 *     <li>To create a booking in a time-slot, the system must certify the availability of resources. That is, must have a bookable room not FULL and an assistant on shift which is FREE.</li>
 *     <li>
 *         Once a booking is created, the statuses of the bookable room and of the assistant on shift must be updated accordingly. The status of a booking can be:
 *         <ul>
 *             <li>SCHEDULED – the test has not been done yet.</li>
 *             <li>COMPLETED – test completed.</li>
 *         </ul>
 *     </li>
 *     <li>A booking not COMPLETED can be cancelled, i.e., deleted from the system. After cancellation, the resources (room and assistant) should be released for booking again, i.e., their statuses must be updated.</li>
 *     <li>A booking SCHEDULED can become COMPLETED. Once completed, the booking cannot be deleted due to audit processes.</li>
 *     <li>Print template: | dd/mm/yyyy HH:MM | status | assistant_email | room_code | student_email |</li>
 * </ul>
 */
public class Booking extends BaseModel {
    private final TimeSlot timeSlot;
    private final Room room;
    private final Assistant assistant;
    private final String studentEmail;

    private boolean isCompleted;

    public Booking(int pk, TimeSlot timeSlot, Room room, Assistant assistant, String studentEmail) {
        super(pk);
        this.timeSlot = timeSlot;
        this.room = room;
        this.assistant = assistant;
        this.studentEmail = studentEmail;
    }

    public TimeSlot getTimeSlot() {
        return timeSlot;
    }

    public String getStatus() {
        return isCompleted ? "COMPLETED" : "SCHEDULED";
    }

    /**
     * @return A string in the format | start time | status | assistant email | room code | student email |
     */
    @Override
    public String toString() {
        return String.format("| %s | %s | %s | %s | %s |",
                timeSlot.getFormattedStartTime(), getStatus(), assistant.getEmail(),
                room.getRoomCode(), studentEmail);
    }

    public Boolean getIsCompleted() {
        return isCompleted;
    }

    /**
     * Marks this booking as complete
     */
    public void completeTest() {
        this.isCompleted = true;
    }

    public Room getRoom() {
        return this.room;
    }

    public Assistant getAssistant() {
        return this.assistant;
    }
}
