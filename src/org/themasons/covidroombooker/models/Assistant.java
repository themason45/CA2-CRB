package org.themasons.covidroombooker.models;

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

    public Assistant(int pk, String name, String email) {
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
     * @return String in the format: | <name> | <email> |
     */
    public String toString() {
        return String.format("| %s | %s |", name, email);
    }
}
