package ch.zhaw.pm2.napp.school.building;

import ch.zhaw.pm2.napp.school.timetable.TimeSlot;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A data class holding all room data
 *
 * @author buechad1, wartmnic
 * @version 1.0.0
 */
public class Room {
    private final String roomIdentifier;

    private final Integer capacity;
    private final Map<DayOfWeek, List<TimeSlot>> roomAvailability;

    /**
     * Creates a room
     *
     * @param roomIdentifier - the unique room identifier, could be a number or a name
     * @param capacity       - the room capacity describing how many pupils can use the room
     */
    public Room(String roomIdentifier, Integer capacity) {
        this.roomIdentifier = roomIdentifier;
        this.capacity = capacity;
        this.roomAvailability = new HashMap<>();
    }

    /**
     * Returns the availability of the room
     *
     * @param dayOfWeek the day the room is needed
     * @param roomSize  the maximum number of people that must fit in the room
     * @param timeSlot  the timeslot to check
     * @return true if the room is free
     */
    public boolean isAvailable(DayOfWeek dayOfWeek, TimeSlot timeSlot, int roomSize) {
        return (roomAvailability.isEmpty() || roomAvailability.get(dayOfWeek) == null || !roomAvailability.get(dayOfWeek).contains(timeSlot)) && roomSize <= capacity;
    }

    /**
     * Adds a timeslot to a Weekday if the Weekday exists already or creates a weekday and adds the Timeslot otherwise
     * @param dayOfWeek The day of the Week the timeslot has to be added to
     * @param timeSlot the timeslot to be added
     */
    public void setRoomAvailability(DayOfWeek dayOfWeek, TimeSlot timeSlot) {
        if (roomAvailability.containsKey(dayOfWeek)) {
            roomAvailability.get(dayOfWeek).add(timeSlot);
        } else {
            List<TimeSlot> timeSlots = new ArrayList<>();
            timeSlots.add(timeSlot);
            roomAvailability.put(dayOfWeek, timeSlots);
        }
    }

    public String getRoomIdentifier() {
        return roomIdentifier;
    }

    public Integer getCapacity() {
        return capacity;
    }
}