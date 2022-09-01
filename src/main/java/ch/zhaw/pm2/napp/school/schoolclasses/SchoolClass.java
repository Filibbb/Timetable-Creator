package ch.zhaw.pm2.napp.school.schoolclasses;

import ch.zhaw.pm2.napp.school.timetable.Subject;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is based on {@link AbstractSchoolEntity} and therefore is a Data class containing all information to a SchoolClass.
 * Other than Person and it's subclasses, the SchoolClass contains Lists of {@link Person} as students and teachers, but has no
 * information about the People in this List, other than their unique Identifier (matriculationNumber)
 *
 * @author buechad1, wartmnic
 * @version 1.0.0
 */
public class SchoolClass extends AbstractSchoolEntity {
    private final List<Person> students;
    private final List<Teacher> teachers;

    /**
     * Creates an Empty class.
     * Students and Teachers have to be added afterwards.
     *
     * @param schoolClassID Unique identifier of the schoolClass
     * @param subjects      subject assigned to this SchoolClass
     */
    public SchoolClass(String schoolClassID, List<Subject> subjects) {
        super(subjects, schoolClassID);
        this.teachers = new ArrayList<>();
        this.students = new ArrayList<>();
    }

    public List<Person> getStudents() {
        return students;
    }

    public void addStudent(Person student) {
        students.add(student);
    }

    public void addTeacher(Teacher teacher) {
        teachers.add(teacher);
    }

    /**
     * Calculates the school class size including teachers and students.
     *
     * @return a numeric value representing the class size.
     */
    public int size() {
        return students.size() + teachers.size();
    }
}