package ch.zhaw.pm2.napp.school;

import ch.zhaw.pm2.napp.school.building.Building;
import ch.zhaw.pm2.napp.school.schoolclasses.SchoolClass;
import ch.zhaw.pm2.napp.school.schoolclasses.Teacher;
import ch.zhaw.pm2.napp.school.timetable.TimetableGenerator;
import ch.zhaw.pm2.napp.school.timetable.exceptions.TimetableException;

import java.time.LocalTime;
import java.util.List;

/**
 * This class represents a school, though with the current prototype not all data is required.
 * The school object has the option to generate timetables for each school class
 */
public class School {
    public static final int WORKDAYS_PER_WEEK = 7;
    public static final int LESSON_LENGTH = 45;
    public static final int SHORT_BREAK = 5;
    public static final int LONG_BREAK = 25;
    public static final LocalTime SCHOOL_OPENING_HOURS = LocalTime.of(8, 0);
    public static final LocalTime SCHOOL_CLOSING_HOURS = LocalTime.of(17, 35);
    private final List<SchoolClass> schoolClasses;
    private final TimetableGenerator timetableGenerator;

    /**
     * Creates a school
     *
     * @param allTeachers     All teachers who work at this school
     * @param schoolBuildings All buildings belonging to the school
     * @param schoolClasses   All classes at this school
     */
    public School(List<Teacher> allTeachers, List<Building> schoolBuildings, List<SchoolClass> schoolClasses) {
        this.schoolClasses = schoolClasses;
        this.timetableGenerator = new TimetableGenerator(allTeachers, schoolBuildings);
    }

    /**
     * Generates a Timetable for all school classes.
     */
    public void generateTimetableForAllSchoolClasses() throws TimetableException {
        for (SchoolClass schoolClass : schoolClasses) {
            timetableGenerator.generateTimeTableForClass(schoolClass);
        }
    }
}
