package ch.zhaw.pm2.napp.school.schoolclasses;

import ch.zhaw.pm2.napp.school.SchoolUtil;
import ch.zhaw.pm2.napp.school.timetable.Lesson;
import ch.zhaw.pm2.napp.school.timetable.Subject;
import ch.zhaw.pm2.napp.school.timetable.TimeSlot;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.DayOfWeek;

import static ch.zhaw.pm2.napp.school.School.*;
import static ch.zhaw.pm2.napp.school.SchoolUtil.createAllTeachers;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Unit test to test the teacher data model {@link Teacher} and make sure checking if teacher has the capacity to teach certain subjects at certain times works correctly
 */
public class TeacherTest {
    private static final int FIRST_LIST_ELEMENT = 0;

    private Teacher teacher;
    private Subject subject;
    private DayOfWeek dayOfWeek;

    /**
     * Gets a teacher to test with using the {@link SchoolUtil}
     * Creates subjects to teach with and a default day monday
     */
    @BeforeEach
    public void setUp() {
        teacher = createAllTeachers().get(FIRST_LIST_ELEMENT);
        subject = teacher.getModuleSchedule().requiredSubjects().get(FIRST_LIST_ELEMENT);
        dayOfWeek = DayOfWeek.MONDAY;
    }

    /**
     * Tests if a teacher is free within a certain time to teach a subject by using the isFreeToTeach method
     */
    @Test
    public void teacherIsFreeTest() {
        assertTrue(teacher.isFreeToTeach(dayOfWeek, new TimeSlot(SCHOOL_OPENING_HOURS), subject));
    }

    /**
     * Tests if a teacher is already occupied at a certain time and cannot teach the specified subject by using the isFreeToTeach method and checking if follow up timeslot is free
     */
    @Test
    public void teacherIsNotFreeTestAndCheckNextTimeSlot() {
        teacher.getTimetable().addToTimetable(dayOfWeek, new Lesson(subject, teacher, new TimeSlot(SCHOOL_OPENING_HOURS), null));
        assertFalse(teacher.isFreeToTeach(dayOfWeek, new TimeSlot(SCHOOL_OPENING_HOURS), subject));
        //check next TimeSlot
        assertTrue(teacher.isFreeToTeach(dayOfWeek, new TimeSlot(SCHOOL_OPENING_HOURS.plusMinutes(SHORT_BREAK).plusMinutes(LESSON_LENGTH)), subject));
    }
}
