package ch.zhaw.pm2.napp.fileio.loader;

import ch.zhaw.pm2.napp.fileio.loader.exception.BadCsvFormatException;
import ch.zhaw.pm2.napp.school.schoolclasses.Person;
import ch.zhaw.pm2.napp.school.schoolclasses.SchoolVisitorRole;
import ch.zhaw.pm2.napp.school.schoolclasses.Teacher;
import ch.zhaw.pm2.napp.school.timetable.Subject;

import java.io.File;
import java.time.DayOfWeek;
import java.util.*;

import static ch.zhaw.pm2.napp.school.schoolclasses.SchoolVisitorRole.STUDENT;
import static ch.zhaw.pm2.napp.school.schoolclasses.SchoolVisitorRole.TEACHER;
import static java.time.DayOfWeek.*;

/**
 * PeopleCsvLoader
 * <p>
 * this class is based on {@link AbstractCsvLoader} and therefore provides the functionality to retrieve a List of Objects
 * created from the values of a csv File.
 * </p>
 * <p>
 * This class specifically creates a List of Person Objects.
 * </p>
 * <p>
 *
 * @author wartminc
 * @version 1.0.0
 * </p>
 */
public class PeopleCsvLoader extends AbstractCsvLoader<Person> {

    private static final int PERSON_ID_INDEX = 0;
    private static final int VISITOR_ROLE_INDEX = 1;
    private static final int LAST_NAME_INDEX = 2;
    private static final int FIRST_NAME_INDEX = 3;
    private static final int WORKLOAD_INDEX = 4;
    private static final int WEEKDAYS_INDEX = 5;
    private static final int FIRST_SUBJECT_INDEX = 6;
    private final Set<Subject> allSubjects = new HashSet<>();

    /**
     * Initializing csv Loader with File to be processed
     *
     * @param file File to be processed
     * @throws BadCsvFormatException with message and Line of csv, is thrown if the csv Values do not match the requirements
     */
    public PeopleCsvLoader(File file) throws BadCsvFormatException {
        populateResources(readLinesOfFile(file));
    }

    /**
     * A String array representing a Line of the csv File is converted to a Person Object.
     *
     * @param personProperties String Array representing the Comma (actually ;) separated values of one line of the csv File
     * @return a Person Object
     * @throws BadCsvFormatException is thrown if there is n Illegal Input in one of the csv fields, or if a required field is Empty.
     */
    @Override
    protected Person createResource(String[] personProperties) throws BadCsvFormatException {
        Person person;
        List<SchoolVisitorRole> visitorRoles = extractVisitorRoles(personProperties);
        List<Subject> personSubjectList = createOrGetSubjects(Arrays.copyOfRange(personProperties, FIRST_SUBJECT_INDEX, personProperties.length), allSubjects);
        allSubjects.addAll(personSubjectList);
        try {
            if (visitorRoles.contains(TEACHER)) {
                person = createTeacher(personProperties, visitorRoles, personSubjectList);
            } else if (visitorRoles.contains(STUDENT)) {
                person = createStudent(personProperties, visitorRoles, personSubjectList);
            } else {
                throw new BadCsvFormatException("No SchoolVisitorRole declared");
            }
        } catch (IndexOutOfBoundsException e) {
            throw new BadCsvFormatException("Illegal csv Format - Fields are missing");
        }
        return person;
    }

    private Person createStudent(String[] personProperties, List<SchoolVisitorRole> visitorRoles, List<Subject> subjects) throws BadCsvFormatException {
        try{
            return new Person(personProperties[PERSON_ID_INDEX], personProperties[LAST_NAME_INDEX], personProperties[FIRST_NAME_INDEX], visitorRoles, subjects);
        } catch (IllegalArgumentException e) {
            throw new BadCsvFormatException("Missing or Bad argument");
        }
    }

    private Teacher createTeacher(String[] personProperties, List<SchoolVisitorRole> visitorRoles, List<Subject> teachableModules) throws BadCsvFormatException {
        try{
            List<DayOfWeek> workingDays = extractWorkingDays(personProperties);
            return new Teacher(personProperties[PERSON_ID_INDEX], personProperties[LAST_NAME_INDEX], personProperties[FIRST_NAME_INDEX], visitorRoles, Double.parseDouble(personProperties[WORKLOAD_INDEX]), workingDays, teachableModules);
        } catch (IllegalArgumentException e) {
            throw new BadCsvFormatException("Missing or Bad argument");
        }
    }

    /**
     * Splits the String at the given Index of personProperties at Commas and iterates over the resulting Strings to
     * determine the schoolVisitorRoles of the Person corresponding to the personProperties String array.
     *
     * @param personProperties String Array representing the Comma (actually ;) separated values of one line of the csv File
     * @return List of SchoolVisitorRole
     * @throws BadCsvFormatException if there is an Argument other than STUDENT or TEACHER in the corresponding String
     */
    private List<SchoolVisitorRole> extractVisitorRoles(String[] personProperties) throws BadCsvFormatException {
        List<SchoolVisitorRole> visitorRoles = new ArrayList<>();
        String[] visitorRolesString = personProperties[VISITOR_ROLE_INDEX].split(",");
        for (String visitorRoleString : visitorRolesString) {
            switch (visitorRoleString) {
                case "TEACHER" -> visitorRoles.add(TEACHER);
                case "STUDENT" -> visitorRoles.add(STUDENT);
                default -> throw new BadCsvFormatException("Illegal Input in \"Visitor Role\" field");
            }
        }
        return visitorRoles;
    }

    /**
     * Splits the String at the given Index of Weekdays at Commas and iterates over the resulting Strings to
     * determine the workingDays of the Person corresponding to the personProperties String array.
     *
     * @param personProperties String Array representing the Comma (actually ;) separated values of one line of the csv File
     * @return List of DayOfWeek
     */
    private List<DayOfWeek> extractWorkingDays(String[] personProperties) {
        List<DayOfWeek> workingDays = new ArrayList<>();
        String[] daysOfWeekString = personProperties[WEEKDAYS_INDEX].split(",");
        for (String dayOfWeekString : daysOfWeekString) {
            switch (dayOfWeekString) {
                case "MO" -> workingDays.add(MONDAY);
                case "TU" -> workingDays.add(TUESDAY);
                case "WE" -> workingDays.add(WEDNESDAY);
                case "TH" -> workingDays.add(THURSDAY);
                case "FR" -> workingDays.add(FRIDAY);
                case "SA" -> workingDays.add(SATURDAY);
                case "SU" -> workingDays.add(SUNDAY);
                default -> throw new IllegalArgumentException("Illegal Input in \"working Days\" field: " + dayOfWeekString);
            }
        }
        return workingDays;
    }

    public Set<Subject> getAllSubjects() {
        return allSubjects;
    }
}