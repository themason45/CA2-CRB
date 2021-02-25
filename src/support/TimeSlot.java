package support;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

/**
 * A predefined couple of {@link LocalDateTime}s, as well as a set of methods that support the use of them
 */
public class TimeSlot {
    public LocalDateTime start;
    public LocalDateTime finish;

    public TimeSlot(LocalDateTime start, LocalDateTime finish) {
        this.start = start;
        this.finish = finish;
    }

    /**
     * @param date The date to find timeslots for
     * @return A collection of TimeSlots for the given date
     */
    public static Collection<? extends TimeSlot> slotsForDate(LocalDate date){
        Properties props = Support.appProps();
        int slotLength = Integer.parseInt((String) props.get("timeslot.length"));
        int slotCount = Integer.parseInt((String) props.get("timeslot.count"));

        Collection<TimeSlot> timeSlots = new ArrayList<>();
        LocalDateTime now = date.atTime(7,0); // Start at 7AM
        for (int i = 0; i < slotCount; i++) {
            LocalDateTime start = LocalDateTime.of(now.getYear(),
                    now.getMonth(),
                    now.getDayOfMonth(),
                    now.getHour(),
                    now.getMinute()).plusMinutes((long) slotLength * i);
            LocalDateTime end = start.plusMinutes(slotLength);

            timeSlots.add(new TimeSlot(start, end));
        }
        return timeSlots;
    }

    /**
     * @return The date of the start of the week for this time slot
     */
    public LocalDate startOfWeek() {
        int dow = start.getDayOfWeek().getValue();
        return this.start.minusDays(dow-1).toLocalDate(); // Get date of the week beginning
    }

    /**
     * @return A string in the format | start time | finish time |
     */
    public String toString() {
        return String.format("| %s | %s |", start, finish);
    }

    /**
     * @return The start time as a string, in the format dd/MM/yyyy hh:mm.
     */
    public String getFormattedStartTime() {
        return start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm"));
    }

    /**
     * @param date The date to build the datetime with
     * @param time The time to build the datetime with
     * @return A {@link LocalDateTime} instance with the given data, but with the time set on the hour, within the
     * correct window of hours
     */
    public static LocalDateTime cleanDateTime(LocalDate date, LocalTime time) throws IllegalArgumentException {
        Properties props = Support.appProps();
        int slotCount = Integer.parseInt((String) props.get("timeslot.count"));

        if (time.getHour() < 7 | (time.getHour() >= (7 + slotCount) & time.getMinute() > 0) | (time.getHour() >
                (7 + slotCount) & time.getMinute() == 0)) throw new IllegalArgumentException(
                String.format("Given time is unavailable, must be between 07:00, and %02d:00", 7 + slotCount));

        return LocalDateTime.of(date, time.withMinute(0));
    }
}
