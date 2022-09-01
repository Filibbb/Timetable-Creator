package ch.zhaw.pm2.napp.school.schoolclasses;

import ch.zhaw.pm2.napp.school.timetable.Subject;

import java.util.List;

import static java.util.Collections.unmodifiableList;

/**
 * This class is based on {@link AbstractSchoolEntity} and therefore is a Data class containing all information to a Person.
 * A person can be a student whereas the {@link SchoolVisitorRole} contains information whether the person is a teacher or a student.
 * Though there are these roles, a separate {@link Teacher} class exists inheriting from a person and having additional information
 *
 * @author buechad1, wartmnic
 * @version 1.0.0
 */
public class Person extends AbstractSchoolEntity {
    private final String matriculationNumber;
    private final String lastName;
    private final String firstName;
    private final List<SchoolVisitorRole> schoolVisitorRole;

    /**
     * Instances a Person object which contains all required data.
     *
     * @param matriculationNumber Unique Identifier of a Person
     * @param lastName            Last Name
     * @param firstName           First Name
     * @param schoolVisitorRole   List of SchoolVisitorRole (Teachers can also be Students)
     * @param subjects            List of subjects person has to visit
     */
    public Person(String matriculationNumber, String lastName, String firstName, List<SchoolVisitorRole> schoolVisitorRole, List<Subject> subjects) throws IllegalArgumentException {
        super(subjects, matriculationNumber);
        validateInputValues(lastName ,firstName, schoolVisitorRole);
            this.matriculationNumber = matriculationNumber;
            this.lastName = lastName;
            this.firstName = firstName;
            this.schoolVisitorRole = unmodifiableList(schoolVisitorRole);
    }

    /**
     * Throws IllegalArgumentException with according Message if one of the Values is empty
     * @param lastName lastName Value
     * @param firstName firstName Value
     * @param schoolVisitorRole schoolVisitorRole Value
     */
    private void validateInputValues(String lastName, String firstName, List<SchoolVisitorRole> schoolVisitorRole) {
        if (lastName.isEmpty()) {
            throw new IllegalArgumentException("Missing Argument, last Name");
        }
        if (firstName.isEmpty()) {
            throw new IllegalArgumentException("Missing Argument, first Name");
        }
        if (schoolVisitorRole.isEmpty()) {
            throw new IllegalArgumentException("Missing Argument, SchoolVisitorRole");
        }
    }

    public String getMatriculationNumber() {
        return matriculationNumber;
    }

    public List<SchoolVisitorRole> getSchoolVisitorRole() {
        return schoolVisitorRole;
    }

    public String getLastName() {
        return lastName;
    }

    public String getFirstName() {
        return firstName;
    }
}