package models;

import support.TimeSlot;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.regex.Pattern;

/**
 * Assistant object is defined as follows:
 * <ul>
 *     <li>A COVID-19 test assistant is someone related to the <strong>university</strong> (staff or student) who is volunteering to perform COVID tests.</li>
 *     <li>To register an assistant in the system, you need their university email and a non-blank name.</li>
 *     <li>The email must be unique and follow the pattern “*@uok.ac.uk”.</li>
 *     <li>Print template: | name | email |</li>
 * </ul>
 */
public class Assistant extends BaseModel {
    // These attributes never change, so can be final
    private final String name;
    private final String email;
    private University university;
    public Integer[] daysActive;

    public ArrayList<Booking> bookings;

    public Assistant(int pk, String name, String email, Integer[] daysActive) {
        super(pk); // Set the pk
        if (name.length() > 0) {
            this.name = name;
        } else {
            throw new IllegalArgumentException("Name may not be empty");
        }
        if (checkEmail(email)) {
            this.email = email;
        } else {
            throw new IllegalArgumentException("That email is not valid, it must have the suffix \"@uok.ac.uk\"");
        }
        this.daysActive = daysActive;
        this.bookings = new ArrayList<>();
    }

    /**
     * Uses Regexp to make sure that both the prefix of the email, and the suffix are valid
     *
     * @return True if email is valid
     */
    public static Boolean checkEmail(String email) {
        Pattern re = Pattern.compile("^[A-Za-z0-9._%+-]+@uok.ac.uk");
        return re.matcher(email).find();
    }

    /**
     * Since an Assistant is on shift for a whole day (3 time slots by default) we can simply just compare the days
     * of each time slot instead of each individual time slot on each day
     *
     * @param timeSlot The time slot to check if the Assistant is on duty for
     * @return True if the assistant is on shift during that time slot
     */
    public boolean checkOnShift(TimeSlot timeSlot) {
        LocalDate startOfWeek = timeSlot.startOfWeek();
        // Get the date for each available day of the week
        for (int j : daysActive) {
            LocalDate dayOfWeek = startOfWeek.plusDays(j);
            if (dayOfWeek.equals(timeSlot.start.toLocalDate())) {
                // Return true if the day of week in question matches the given time slot
                return true;
            }
        }
        return false;
    }

    /**
     * @param timeSlot The time slot to check availability for
     * @return True if the Assistant is available for that time slot
     */
    public boolean checkAvailability(TimeSlot timeSlot) {
        if (checkOnShift(timeSlot)) {
            // Check if there are no bookings with the current time slot
            boolean noBookings = true;
            for (Booking booking : bookings) {
                noBookings = booking.getTimeSlot() != timeSlot;
            }
            return noBookings;
        }
        return false;
    }

    /**
     * @return String in the format: | <name> | <email> |
     */
    public String toString() {
        return String.format("| %s | %s |", name, email);
    }

    public void setUniversity(University university) {
        this.university = university;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public void addBooking(Booking booking) {
        if (this.checkAvailability(booking.getTimeSlot())) {
            this.bookings.add(booking);
        } else {
            throw new IllegalArgumentException("Booking time slot is not available");
        }
    }

    public void addDayActive(Integer day) {
        if (1 <= day & day <= 7) {
            Integer[] copy = Arrays.copyOf(daysActive, daysActive.length + 1);
            copy[daysActive.length] = day - 1;
            this.daysActive = Arrays.stream(copy).sorted(((o1, o2) -> (o1 > o2) ? 1 : 0)).toArray(Integer[]::new);
        }
    }

    public void removeDayActive(Integer day) {
        if (1 <= day & day <= 7) {
            int index = Arrays.asList(daysActive).indexOf(day - 1);
            Integer[] copy = new Integer[daysActive.length - 1];

            for (int i = 0, j = 0; i < daysActive.length; i++) {
                if (i != index) {
                    copy[j++] = daysActive[i];
                }
            }
            daysActive = copy;
        }
    }
}
