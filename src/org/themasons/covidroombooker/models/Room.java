package org.themasons.covidroombooker.models;

/**
 * Assistant object is defined as follows:
 * <ul>
 *     <li>The university has several rooms, and some of the rooms can be allocated to apply COVID tests.</li>
 *     <li>A room must have a string code (e.g., IC215) and a capacity.</li>
 *     <li>The code is used to identify the room and, therefore, must be unique.</li>
 *     <li>The capacity must be an integer value greater than zero. It represents the number of concurrent assistants that can be safely allocated in the room to perform tests.</li>
 *     <li>Print template: | code | capacity: capacity |</li>
 * </ul>
 */
public class Room extends BaseModel {
    private String roomCode;
    private int capacity;
    private University university;

    public Room(int pk, String roomCode, int capacity) {
        super(pk);
        // TODO: Create a unique check for the roomCode
        this.roomCode = roomCode;
        if (capacity > 0) {
            this.capacity = capacity;
        } else {
            throw new IllegalArgumentException("Capacity must be greater than 0");
        }
    }

    public String toString() {
        return String.format("| %s | Capacity: %s |", roomCode, capacity);
    }
}
