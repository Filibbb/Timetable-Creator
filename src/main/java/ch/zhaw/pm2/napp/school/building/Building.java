package ch.zhaw.pm2.napp.school.building;

import ch.zhaw.pm2.napp.school.building.exception.NoSuchRoomException;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * A data class holding all building data
 *
 * @author wartmnic
 * @version 1.0.0
 */
public class Building {
    private final List<Floor> floors = new ArrayList<>();
    private final String buildingIdentifier;

    /**
     * Creates a Building
     *
     * @param buildingIdentifier - the unique room identifier, could be a number or a name
     */
    public Building(String buildingIdentifier) {
        this.buildingIdentifier = buildingIdentifier;
    }

    /**
     * Iterates over all Floor Objects of the Building to retrieve all Rooms
     *
     * @return List of all Room Objects of this building
     */
    public List<Room> getAllRooms() {
        List<Room> allRooms = new ArrayList<>();
        for (Floor floor : floors) {
            allRooms.addAll(floor.getRooms());
        }
        return allRooms;
    }

    public Room getRoomWithIdentifier(String identifier) throws NoSuchRoomException {
        for (Room room : getAllRooms()) {
            if (room.getRoomIdentifier().equals(identifier)) {
                return room;
            }
        }
        throw new NoSuchRoomException("No Room with this Identifier exists.");
    }

    /**
     * Checks whether a Floor Object with the given floorIdentifier already exists. If so, the Room Object is added to this
     * Floor Object. Else, a new Floor Object with the given Identifier will be created and the Room will be added to it.
     *
     * @param requestedRoom            Room Object to be added to a Floor Object
     * @param requestedFloorIdentifier Identifier of the Floor the room has to be placed into
     */
    public void addRoomToFloor(Room requestedRoom, String requestedFloorIdentifier) {
        Optional<Floor> floor = floors.stream().filter(floorOfList -> requestedFloorIdentifier.equals(floorOfList.getFloorIdentifier())).findFirst();
        if (floor.isEmpty()) {
            Floor newFloor = new Floor(requestedFloorIdentifier);
            floors.add(newFloor);
            newFloor.addRoom(requestedRoom);
        } else {
            floor.get().addRoom(requestedRoom);
        }
    }

    public String getBuildingIdentifier() {
        return buildingIdentifier;
    }

    public List<Floor> getFloors() {
        return floors;
    }
}