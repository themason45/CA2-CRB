package support;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Properties;

public class TimeSlot {
    public LocalDateTime start;
    public LocalDateTime finish;

    public TimeSlot(LocalDateTime start, LocalDateTime finish) {
        this.start = start;
        this.finish = finish;
    }

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

    public String toString() {
        return String.format("%s | %s", start, finish);
    }

    public Boolean hasElapsed() {
        return LocalDateTime.now().isAfter(this.finish);
    }

    public String getFormattedStartTime() {
        return start.format(DateTimeFormatter.ofPattern("dd/MM/yyyy hh:mm"));
    }

    public static LocalDateTime cleanDateTime(LocalDate date, LocalTime time) {
        Properties props = Support.appProps();
        int slotCount = Integer.parseInt((String) props.get("timeslot.count"));

        if (time.getHour() < 7 | (time.getHour() == 7 + slotCount & time.getMinute() > 0)) throw new IllegalArgumentException(
                String.format("Given time is unavailable, must be between 07:00, and %02d:00", 7 + slotCount));

        return LocalDateTime.of(date, time.withMinute(0));
    }
}
