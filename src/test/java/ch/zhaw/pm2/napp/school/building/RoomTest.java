package ch.zhaw.pm2.napp.school.building;

import ch.zhaw.pm2.napp.school.timetable.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static ch.zhaw.pm2.napp.school.School.SCHOOL_OPENING_HOURS;
import static ch.zhaw.pm2.napp.school.SchoolUtil.createAllBuildings;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit Tests for the room data model {@link Room} to make sure logic if rooms are free works correctly and rooms can be occupied and are displayed as such.
 */
public class RoomTest {
    private static final int ROOM_SIZE_2 = 2;
    private static final int ROOM_SIZE_12 = 12;
    private static final int FIRST_LIST_INDEX = 0;

    private Room room;
    private DayOfWeek dayOfWeek;

    /**
     * Creates a test environment with rooms and a default day (monday)
     */
    @BeforeEach
    public void setUp() {
        room = createAllBuildings().get(FIRST_LIST_INDEX).getAllRooms().get(FIRST_LIST_INDEX);
        dayOfWeek = DayOfWeek.MONDAY;
    }

    /**
     * Tests whether a room that should be free is actually displayed as being available
     */
    @Test
    public void testIfTheRoomIsFree() {
        assertTrue(room.isAvailable(dayOfWeek, new TimeSlot(SCHOOL_OPENING_HOURS), ROOM_SIZE_2));
        assertFalse(room.isAvailable(dayOfWeek, new TimeSlot(SCHOOL_OPENING_HOURS), ROOM_SIZE_12));
    }

    /**
     * Tests whether a room that should be occupied is actually displayed as being occupied
     */
    @Test
    public void testIfTheRoomIsOccupied() {
        TimeSlot timeSlot = new TimeSlot(SCHOOL_OPENING_HOURS);
        room.setRoomAvailability(dayOfWeek, timeSlot);
        assertFalse(room.isAvailable(dayOfWeek, timeSlot, ROOM_SIZE_2));
    }
}
