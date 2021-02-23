package models;

import support.Support;

import java.io.InputStream;
import java.util.ArrayList;


/**
 * University object is defined as follows:
 * <ul>
 *      <li>The University has a list of assistants and a list of rooms.</li>
 *      <li>You should implement functions to add, both assistants and rooms.</li>
 *      <li>Due to time constraints, you don’t need to develop screen to manage the university resources, but you
 *      need to pre-load the system with instances of rooms and assistants.</li>
 * </ul>
 */
public class University extends BaseModel {
    public ArrayList<Assistant> assistants;
    public ArrayList<Room> rooms;

    public University(int pk) {
        super(pk);

        this.assistants = new ArrayList<>();
        this.rooms = new ArrayList<>();
    }

    /**
     * @param assistant The assistant to add to the university
     */
    public void addAssistant(Assistant assistant) {
        assistant.setUniversity(this);
        if (assistants.stream().noneMatch(x -> x.getEmail().equals(assistant.getEmail()))) {
            this.assistants.add(assistant);
        } else {
            throw new IllegalArgumentException("Email must be unique");
        }
    }

    /**
     * Fills out the {@link #assistants} list on the current University object
     *
     * @param csv The parsed csv from the {@link Support#parseCsv(InputStream)} method
     */
    public void populateAssistants(String[][] csv) {
        for (String[] row : csv) {
            Integer[] daysActive = Support.parseSublistAsInts(row[3]).toArray(new Integer[0]);
            Assistant assistant = new Assistant(Integer.parseInt(row[0]), row[1], row[2], daysActive);
            this.addAssistant(assistant);
        }
    }

    /**
     * Adds a room to the university, if a room with that code already exists, then throw an exception
     *
     * @param room The room to add to the university
     */
    public void addRoom(Room room) {
        room.setUniversity(this);

        // Check if the room code is unique
        if (rooms.stream().noneMatch(x -> x.getRoomCode().equals(room.getRoomCode()))) {
            this.rooms.add(room);
        } else {
            throw new IllegalArgumentException("Room code must be unique");
        }
    }

    /**
     * Fills out the {@link #rooms} list on the current University object
     *
     * @param csv The parsed csv from the {@link BookingApp#parseCsv(InputStream)} method
     */
    public void populateRooms(String[][] csv) {
        for (String[] row : csv) {
            Room room = new Room(Integer.parseInt(row[0]), row[1], Integer.parseInt(row[2]),
                    Support.parseSublistAsInts(row[5]));
            this.addRoom(room);
        }
    }

    public ArrayList<Assistant> getAssistants() {
        return assistants;
    }
}
