package ch.zhaw.pm2.napp.school.timetable;

import ch.zhaw.pm2.napp.school.building.Building;
import ch.zhaw.pm2.napp.school.building.Room;
import ch.zhaw.pm2.napp.school.schoolclasses.SchoolClass;
import ch.zhaw.pm2.napp.school.schoolclasses.Teacher;
import ch.zhaw.pm2.napp.school.timetable.Subject;
import ch.zhaw.pm2.napp.school.timetable.TimeSlot;
import ch.zhaw.pm2.napp.school.timetable.Timetable;
import ch.zhaw.pm2.napp.school.timetable.TimetableGenerator;
import ch.zhaw.pm2.napp.school.timetable.exceptions.TimetableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.pm2.napp.school.SchoolUtil.createAllSchoolClasses;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

/**
 * Tests the {@link TimetableGenerator} class whether the exception is thrown under the right conditions.
 */
public class TimetableGeneratorExceptionTest {
    private static final int FIRST_LIST_INDEX = 0;

    @Mock
    private SchoolClass schoolClass;
    @Mock
    private DayOfWeek dayOfWeek;
    @Mock
    private TimeSlot timeSlot;
    @Mock
    private Teacher teacher;
    private List<Teacher> allTeachers;
    @Mock
    private Building building;
    private List<Building> schoolBuildings;
    private TimetableGenerator timetableGenerator;

    /**
     * Sets up the timetableGenerator and mocks the dayOfWeek and the TimeSlot
     */
    @BeforeEach
    public void setUp() {
        schoolClass = createAllSchoolClasses().get(FIRST_LIST_INDEX);
        allTeachers = new ArrayList<>();
        schoolBuildings = new ArrayList<>();
        addTeacherAndBuildingsToList();
        dayOfWeek = mock(DayOfWeek.class);
        timeSlot = mock(TimeSlot.class);

        timetableGenerator = new TimetableGenerator(allTeachers, schoolBuildings);
    }

    /**
     * Tests whether an {@link TimetableException} is thrown when no teacher is available.
     */
    @Test
    public void testNoAvailableTeacherFound() {
        Subject subject = mock(Subject.class);

        doReturn(false).when(teacher).isFreeToTeach(dayOfWeek, timeSlot, subject);
        assertThrows(TimetableException.class, () -> timetableGenerator.generateTimeTableForClass(schoolClass));
    }

    /**
     * Tests whether an {@link TimetableException} is thrown when no Room is available.
     */
    @Test
    public void testNoAvailableRoomFound() {
        Room room = mock(Room.class);
        int schoolClassSize = schoolClass.size();

        doReturn(false).when(room).isAvailable(dayOfWeek, timeSlot, schoolClassSize);
        assertThrows(TimetableException.class, () -> timetableGenerator.generateTimeTableForClass(schoolClass));
    }

    /**
     * Tests whether an {@link TimetableException} is thrown when no free TimeSlots where found.
     */
    @Test
    public void testNoAvailableTimeSlotFound() {
        Timetable timetable = mock(Timetable.class);
        Subject subject = mock(Subject.class);

        doReturn(null).when(timetable).getFreeTimeSlotsForSubjectPerDay(subject, dayOfWeek);
        assertThrows(TimetableException.class, () -> timetableGenerator.generateTimeTableForClass(schoolClass));
    }

    private void addTeacherAndBuildingsToList() {
        teacher = mock(Teacher.class);
        allTeachers.add(teacher);
        building = mock(Building.class);
        schoolBuildings.add(building);
    }
}
