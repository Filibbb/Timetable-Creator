package ch.zhaw.pm2.napp.school.building;

import java.util.ArrayList;
import java.util.List;

/**
 * A data class holding all floor data
 *
 * @author buechad1, wartmnic
 * @version 1.0.0
 */
public class Floor {
    private final String floorIdentifier;
    private final List<Room> rooms = new ArrayList<>();

    /**
     * Creates a floor
     *
     * @param floorIdentifier - the unique room identifier, could be a number or a name
     */
    public Floor(String floorIdentifier) {
        this.floorIdentifier = floorIdentifier;
    }

    public String getFloorIdentifier() {
        return floorIdentifier;
    }

    public void addRoom(Room room) {
        rooms.add(room);
    }

    public List<Room> getRooms() {
        return rooms;
    }
}
