package ch.zhaw.pm2.napp.school.timetable;

import ch.zhaw.pm2.napp.school.schoolclasses.SchoolClass;
import ch.zhaw.pm2.napp.school.timetable.exceptions.TimetableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static ch.zhaw.pm2.napp.school.SchoolUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * Tests {@link TimetableGenerator} whether timetable generation works correctly by checking if timeslots are filled up as well as parameters with amount of lessons per day or similar is being looked at correctly
 */
public class TimetableGeneratorTest {
    private static final int FIRST_LIST_INDEX = 0;
    private static final int SECOND_LIST_INDEX = 1;

    private SchoolClass schoolClass1;
    private Timetable generatedTimetableClass1;
    private SchoolClass schoolClass2;

    /**
     * Setup multiple school classes and generating timetables for each school class separately to work with during test
     *
     * @throws TimetableException - should not be thrown
     */
    @BeforeEach
    public void setUp() throws TimetableException {
        schoolClass1 = createAllSchoolClasses().get(FIRST_LIST_INDEX);
        schoolClass2 = createAllSchoolClasses().get(SECOND_LIST_INDEX);
        TimetableGenerator timetableGenerator = new TimetableGenerator(createAllTeachers(), createAllBuildings());
        timetableGenerator.generateTimeTableForClass(schoolClass1);
        timetableGenerator.generateTimeTableForClass(schoolClass2);
        generatedTimetableClass1 = schoolClass1.getTimetable();
    }

    /**
     * Tests whether timetable generation works as expected and has the correct default setup defined in {@link TimetableModel}
     */
    @Test
    public void timetableGeneratorTimeSlotsTest() {
        TimetableModel timetableModel = new TimetableModel();
        Timetable generatedTimetable = schoolClass1.getTimetable();
        for (int i = 0; i < generatedTimetable.getTimetable().get(DayOfWeek.MONDAY).size(); i++) {
            assertEquals(timetableModel.getTimetableModel().get(DayOfWeek.MONDAY).get(i), generatedTimetable.getTimetable().get(DayOfWeek.MONDAY).get(i).timeSlot());
            List<TimeSlot> allTimeSlotsInTimetable = new ArrayList<>();
            generatedTimetableClass1.getTimetable().forEach((dayOfWeek, lessons) -> lessons.stream().map(Lesson::timeSlot).forEach(allTimeSlotsInTimetable::add));

            for (TimeSlot timeSlotInTimetable : allTimeSlotsInTimetable) {
                assertTrue(timetableModel.getTimetableModel().get(DayOfWeek.MONDAY).contains(timeSlotInTimetable));
            }
        }
    }

    /**
     * Tests whether the amount of lessons do not exceed a maximum amount a day by checking if timetable size of a day is not more than a specified constant (8)
     */
    @Test
    public void timetableGeneratorAmountOfLessonsTest() {
        assertEquals(7, generatedTimetableClass1.getTimetable().get(DayOfWeek.MONDAY).size());
    }

    /**
     * Test whether the creation of a timetable also works with several school classes and whether
     * the algorithm uses other teachers and other resources in general.
     */
    @Test
    public void timetableGeneratorAmountOfLessonsTestWithMultipleSchoolClasses() {
        Timetable generatedTimetableClass2 = schoolClass2.getTimetable();

        assertEquals(7, generatedTimetableClass1.getTimetable().get(DayOfWeek.MONDAY).size());
        assertEquals(7, generatedTimetableClass2.getTimetable().get(DayOfWeek.MONDAY).size());
    }

    /**
     * Tests whether all subjects that are in the module plan are also in the timetable.
     */
    @Test
    public void timetableGeneratorAllSubjectsWereAssignedTest() {
        List<Subject> allSubjectsInTimetable = new ArrayList<>();
        generatedTimetableClass1.getTimetable().forEach((dayOfWeek, lessons) -> lessons.stream().map(Lesson::subject).forEach(allSubjectsInTimetable::add));

        for (Subject requiredSubject : schoolClass1.getModuleSchedule().requiredSubjects()) {
            assertTrue(allSubjectsInTimetable.contains(requiredSubject));
        }
    }

    /**
     * Tests if every lessen has an assigned teacher.
     */
    @Test
    public void timetableGeneratorTeacherAssignedTest() {
        List<Lesson> allLessonsInTimetable = new ArrayList<>();
        generatedTimetableClass1.getTimetable().forEach((dayOfWeek, lessons) -> allLessonsInTimetable.addAll(lessons));

        for (Lesson lesson : allLessonsInTimetable) {
            assertNotNull(lesson.teacher());
        }
    }

    /**
     * Tests if every lessen has an assigned room.
     */
    @Test
    public void timetableGeneratorRoomAssignedTest() {
        List<Lesson> allLessonsInTimetable = new ArrayList<>();
        generatedTimetableClass1.getTimetable().forEach((dayOfWeek, lessons) -> allLessonsInTimetable.addAll(lessons));

        for (Lesson lesson : allLessonsInTimetable) {
            assertNotNull(lesson.room());
        }
    }
}
