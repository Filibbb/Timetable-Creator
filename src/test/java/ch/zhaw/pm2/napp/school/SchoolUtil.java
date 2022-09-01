package ch.zhaw.pm2.napp.school;

import ch.zhaw.pm2.napp.school.building.Building;
import ch.zhaw.pm2.napp.school.building.Room;
import ch.zhaw.pm2.napp.school.schoolclasses.Person;
import ch.zhaw.pm2.napp.school.schoolclasses.SchoolClass;
import ch.zhaw.pm2.napp.school.schoolclasses.SchoolVisitorRole;
import ch.zhaw.pm2.napp.school.schoolclasses.Teacher;
import ch.zhaw.pm2.napp.school.timetable.Subject;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;

import static ch.zhaw.pm2.napp.school.schoolclasses.SchoolVisitorRole.STUDENT;
import static ch.zhaw.pm2.napp.school.schoolclasses.SchoolVisitorRole.TEACHER;

/**
 * School utility class for helping setups within tests by creating required objects such as students, subjects etc
 */
public class SchoolUtil {
    private static final int FIRST_LIST_ELEMENT = 0;
    private static final int SECOND_LIST_ELEMENT = 1;
    private static final int THIRD_LIST_ELEMENT = 2;
    private static final int FOURTH_LIST_ELEMENT = 3;

    private static final List<Subject> subjectList = createSubjectList();
    private static final List<Person> allStudents = createAllStudents();
    private static final List<Teacher> allTeachers = createAllTeachers();

    /**
     * Creates a subject list for the {@link ModuleSchedule}
     *
     * @return a list with predefined subjects.
     */
    public static List<Subject> createSubjectList() {
        List<Subject> requiredSubjects = new ArrayList<>();
        requiredSubjects.add(new Subject("Math", 2));
        requiredSubjects.add(new Subject("German", 4));
        requiredSubjects.add(new Subject("Sport", 1));
        return requiredSubjects;
    }

    /**
     * Creates all students for the school.
     *
     * @return a list with predefined students.
     */
    public static List<Person> createAllStudents() {
        List<Person> allStudents = new ArrayList<>();
        List<SchoolVisitorRole> schoolVisitorRoles = new ArrayList<>();
        schoolVisitorRoles.add(STUDENT);

        allStudents.add(new Person("1", "Muster", "Max", schoolVisitorRoles, subjectList));
        allStudents.add(new Person("2", "Bet", "Friz", schoolVisitorRoles, subjectList));
        allStudents.add(new Person("3", "Wasa", "Peter", schoolVisitorRoles, subjectList));
        allStudents.add(new Person("4", "Acer", "Mai", schoolVisitorRoles, subjectList));
        return allStudents;
    }

    /**
     * Creates all teachers for the school.
     *
     * @return a list with predefined teachers.
     */
    public static List<Teacher> createAllTeachers() {
        List<Teacher> allTeachers = new ArrayList<>();
        List<SchoolVisitorRole> schoolVisitorRoles = new ArrayList<>();
        schoolVisitorRoles.add(TEACHER);
        List<DayOfWeek> availableWeekDays = new ArrayList<>();
        for (int i = 1; i <= 5; i++) {
            availableWeekDays.add(DayOfWeek.of(i));
        }

        allTeachers.add(new Teacher("901", "Apple", "Herbert", schoolVisitorRoles, 1.0, availableWeekDays, subjectList));
        allTeachers.add(new Teacher("902", "Batterie", "Jana", schoolVisitorRoles, 1.0, availableWeekDays, subjectList));
        allTeachers.add(new Teacher("903", "Cable", "Ueli", schoolVisitorRoles, 1.0, availableWeekDays, subjectList));
        return allTeachers;
    }

    /**
     * Creates all buildings for the school.
     *
     * @return a list with predefined buildings.
     */
    public static List<Building> createAllBuildings() {
        List<Building> schoolBuildings = new ArrayList<>();

        Room firstFloorRoom1 = new Room("101", 2);
        Room firstFloorRoom2 = new Room("102", 10);

        Room secondFloorRoom1 = new Room("201", 15);
        Room secondFloorRoom2 = new Room("202", 15);

        schoolBuildings.add(new Building("South"));
        schoolBuildings.add(new Building("West"));

        schoolBuildings.get(FIRST_LIST_ELEMENT).addRoomToFloor(firstFloorRoom1, "1");
        schoolBuildings.get(FIRST_LIST_ELEMENT).addRoomToFloor(firstFloorRoom2, "1");
        schoolBuildings.get(SECOND_LIST_ELEMENT).addRoomToFloor(secondFloorRoom1, "2");
        schoolBuildings.get(SECOND_LIST_ELEMENT).addRoomToFloor(secondFloorRoom2, "2");
        return schoolBuildings;
    }

    /**
     * Creates all school classes with the teachers and students of the school.
     *
     * @return a list with predefined school classes.
     */
    public static List<SchoolClass> createAllSchoolClasses() {
        List<SchoolClass> schoolClasses = new ArrayList<>();

        SchoolClass schoolClass1 = new SchoolClass("1A", subjectList);
        schoolClass1.addTeacher(allTeachers.get(FIRST_LIST_ELEMENT));
        schoolClass1.addStudent(allStudents.get(FIRST_LIST_ELEMENT));
        schoolClass1.addStudent(allStudents.get(SECOND_LIST_ELEMENT));

        SchoolClass schoolClass2 = new SchoolClass("2B", subjectList);
        schoolClass2.addTeacher(allTeachers.get(SECOND_LIST_ELEMENT));
        schoolClass2.addStudent(allStudents.get(THIRD_LIST_ELEMENT));
        schoolClass2.addStudent(allStudents.get(FOURTH_LIST_ELEMENT));

        schoolClasses.add(schoolClass1);
        schoolClasses.add(schoolClass2);

        return schoolClasses;
    }
}
