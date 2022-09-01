package ch.zhaw.pm2.napp.fileio.Loader;

import ch.zhaw.pm2.napp.fileio.loader.PeopleCsvLoader;
import ch.zhaw.pm2.napp.fileio.loader.exception.BadCsvFormatException;
import ch.zhaw.pm2.napp.school.schoolclasses.Person;
import ch.zhaw.pm2.napp.school.timetable.Subject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static ch.zhaw.pm2.napp.school.schoolclasses.SchoolVisitorRole.TEACHER;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Contains Unit Tests to check functionality of PeopleCsvLoader Class
 *
 * @author wartmnic
 * @version 1.0.0
 */

class PeopleCsvLoaderTest {

    private Path fileDir;

    @BeforeEach
    void setUp() {
        fileDir = FileSystems.getDefault().getPath("", "src", "test", "resources", "peopletest").toAbsolutePath();
    }

    /**
     * A csv File with 5 valid Lines is used to Test, if the Model is created according to the Data in the File.
     * A Series of Tests compares the properties of the created resource Objects with the expected Values.
     * <p>
     * 17.2212.12;STUDENT;Wartmann;Nico;;;Analysis 1,2;THIN,4
     * 65.2655.48;STUDENT;Büchi;Adrian;;;Analysis 1,2;THIN,4
     * 35.5215.65;STUDENT;Weber;Philippe;;;Analysis 2,2;Prog_2,4
     * 45.2385.75;STUDENT;Fuchs;Patric;;;Analysis 2,2;Prog_2,4
     * 24.1538.01;TEACHER;Stern;Olaf;40;MO,TU,TH;Analysis 1,2;Analysis 2,2;Prog_2,4;THIN,4;LinAlg,4;INCO,2
     *
     * @throws BadCsvFormatException - Test is supposed to fail if this Exception is thrown
     */
    @Test
    void positiveTestWithValidValuesInFile() throws BadCsvFormatException {
        PeopleCsvLoader peopleCsvLoader = new PeopleCsvLoader(Paths.get(fileDir.toString(), "PeoplePropertiesTestGood.csv").toFile());
        List<Person> people = peopleCsvLoader.getResources();
        List<Person> teachers = getTeachersFromList(people);
        Set<Subject> subjects = peopleCsvLoader.getAllSubjects();
        assertEquals(5, people.size());
        assertEquals(6, subjects.size());
        assertEquals(2, people.get(1).getModuleSchedule().requiredSubjects().size());
        assertEquals("24.1538.01", teachers.get(0).getId());
    }

    /**
     * A Csv File with a missing Argument in the "Matriculation Number" field of Line 2 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * following Values are expected from the csv File:
     * 17.2212.12;STUDENT;Wartmann;Nico;;;Analysis 1,2;THIN,4;
     * ;STUDENT;Büchi;Adrian;;;Analysis 1,2;THIN,4;
     * 35.5215.65;STUDENT;Weber;Philippe;;;Analysis 2,2;Prog_2,4;
     * 45.2385.75;STUDENT;Fuchs;Patric;;;Analysis 2,2;Prog_2,4;
     * 24.1538.01;TEACHER;Stern;Olaf;40;MO,TU,TH;Analysis 1,4
     */
    @Test
    void destructiveTestMissingMatriculationNumberValueInFile() {
        File missingMatriculationNumberFile = Paths.get(fileDir.toString(), "missingarguments", "PeoplePropertiesTestMissingMatriculationNumber.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new PeopleCsvLoader(missingMatriculationNumberFile));
        try {
            new PeopleCsvLoader(missingMatriculationNumberFile);
        } catch (BadCsvFormatException e) {
            assertEquals("Missing or Bad argument - in Line: 2", e.getMessage());
        }
    }

    /**
     * A Csv File with a missing Argument in the "School Visitor Role" field of Line 1 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * following Values are expected from the csv File:
     * 17.2212.12;;Wartmann;Nico;;;Analysis 1,2;THIN,4;
     * 65.2655.48;STUDENT;Büchi;Adrian;;;Analysis 1,2;THIN,4;
     * 35.5215.65;STUDENT;Weber;Philippe;;;Analysis 2,2;Prog_2,4;
     * 45.2385.75;STUDENT;Fuchs;Patric;;;Analysis 2,2;Prog_2,4;
     * 24.1538.01;TEACHER;Stern;Olaf;40;MO,TU,TH;Analysis 1,4
     * <p>
     * A Csv File with a bad Argument in the "School Visitor Role" field of Line 1 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * following Values are expected from the csv File:
     * 17.2212.12;Test in Line 1;Wartmann;Nico;;;Analysis 1,2;THIN,4;
     * 65.2655.48;STUDENT;Büchi;Adrian;;;Analysis 1,2;THIN,4;
     * 35.5215.65;STUDENT;Weber;Philippe;;;Analysis 2,2;Prog_2,4;
     * 45.2385.75;STUDENT;Fuchs;Patric;;;Analysis 2,2;Prog_2,4;
     * 24.1538.01;TEACHER;Stern;Olaf;40;MO,TU,TH;Analysis 1,4
     */
    @Test
    void destructiveTestMissingAndBadSchoolVisitorRoleValueInFile() {
        //Test with missing VisitorRole
        File missingSchoolVisitorRoleFile = Paths.get(fileDir.toString(), "missingarguments", "PeoplePropertiesTestMissingSchoolVisitorRole.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new PeopleCsvLoader(missingSchoolVisitorRoleFile));
        try {
            new PeopleCsvLoader(missingSchoolVisitorRoleFile);
        } catch (BadCsvFormatException e) {
            assertEquals("Illegal Input in \"Visitor Role\" field - in Line: 1", e.getMessage());
        }
        //Tests with bad VisitorRole
        File BadSchoolVisitorRoleFile = Paths.get(fileDir.toString(), "badarguments", "PeoplePropertiesTestBadVisitorRole.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new PeopleCsvLoader(BadSchoolVisitorRoleFile));
        try {
            new PeopleCsvLoader(BadSchoolVisitorRoleFile);
        } catch (BadCsvFormatException e) {
            assertEquals("Illegal Input in \"Visitor Role\" field - in Line: 1", e.getMessage());
        }
    }

    /**
     * A Csv File with a missing Argument in the "Last Name" field of Line 3 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * 17.2212.12;STUDENT;Wartmann;Nico;;;Analysis 1,2;THIN,4;
     * 65.2655.48;STUDENT;Büchi;Adrian;;;Analysis 1,2;THIN,4;
     * 35.5215.65;STUDENT;;Philippe;;;Analysis 2,2;Prog_2,4;
     * 45.2385.75;STUDENT;Fuchs;Patric;;;Analysis 2,2;Prog_2,4;
     * 24.1538.01;TEACHER;Stern;Olaf;40;MO,TU,TH;Analysis 1,4
     */
    @Test
    void destructiveTestMissingLastNameValueInFile() {
        File missingLastNameFile = Paths.get(fileDir.toString(), "missingarguments", "PeoplePropertiesTestMissingLastName.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new PeopleCsvLoader(missingLastNameFile));
        try {
            new PeopleCsvLoader(missingLastNameFile);
        } catch (BadCsvFormatException e) {
            assertEquals("Missing or Bad argument - in Line: 3", e.getMessage());
        }
    }

    /**
     * A Csv File with a missing Argument in the "First Name" field of Line 4 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * 17.2212.12;STUDENT;Wartmann;Nico;;;Analysis 1,2;THIN,4;
     * 65.2655.48;STUDENT;Büchi;Adrian;;;Analysis 1,2;THIN,4;
     * 35.5215.65;STUDENT;Weber;Philippe;;;Analysis 2,2;Prog_2,4;
     * 45.2385.75;STUDENT;;Patric;;;Analysis 2,2;Prog_2,4;
     * 24.1538.01;TEACHER;Stern;Olaf;40;MO,TU,TH;Analysis 1,4
     */
    @Test
    void destructiveTestMissingFirstNameValueInFile() {
        File missingFirstNameFile = Paths.get(fileDir.toString(), "missingarguments", "PeoplePropertiesTestMissingFirstName.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new PeopleCsvLoader(missingFirstNameFile));
        try {
            new PeopleCsvLoader(missingFirstNameFile);
        } catch (BadCsvFormatException e) {
            assertEquals("Missing or Bad argument - in Line: 4", e.getMessage());
        }
    }

    /**
     * A Csv File with a missing Argument in the "Workload" field of Line 5 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * 17.2212.12;STUDENT;Wartmann;Nico;;;Analysis 1,2;THIN,4;
     * 65.2655.48;STUDENT;Büchi;Adrian;;;Analysis 1,2;THIN,4;
     * 35.5215.65;STUDENT;Weber;Philippe;;;Analysis 2,2;Prog_2,4;
     * 45.2385.75;STUDENT;Fuchs;Patric;;;Analysis 2,2;Prog_2,4;
     * 24.1538.01;TEACHER;Stern;Olaf;;MO,TU,TH;Analysis 1,4
     */
    @Test
    void destructiveTestMissingWorkLoadValueInFile() {
        File missingWorkLoadFile = Paths.get(fileDir.toString(), "missingarguments", "PeoplePropertiesTestMissingWorkload.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new PeopleCsvLoader(missingWorkLoadFile));
        try {
            new PeopleCsvLoader(missingWorkLoadFile);
        } catch (BadCsvFormatException e) {
            assertEquals("Missing or Bad argument - in Line: 5", e.getMessage());
        }
    }

    /**
     * A Csv File with a missing Argument in the "Weekdays" field of Line 5 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * following Values are expected from the csv File:
     * 17.2212.12;STUDENT;Wartmann;Nico;;;Analysis 1,2;THIN,4;
     * 65.2655.48;STUDENT;Büchi;Adrian;;;Analysis 1,2;THIN,4;
     * 35.5215.65;STUDENT;Weber;Philippe;;;Analysis 2,2;Prog_2,4;
     * 45.2385.75;STUDENT;Fuchs;Patric;;;Analysis 2,2;Prog_2,4;
     * 24.1538.01;TEACHER;Stern;Olaf;40;;Analysis 1,4
     * <p>
     * A Csv File with a bad Argument in the "Weekdays" field of Line 5 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * following Values are expected from the csv File:
     * 17.2212.12;STUDENT;Wartmann;Nico;;;Analysis 1,2;THIN,4;
     * 65.2655.48;STUDENT;Büchi;Adrian;;;Analysis 1,2;THIN,4;
     * 35.5215.65;STUDENT;Weber;Philippe;;;Analysis 2,2;Prog_2,4;
     * 45.2385.75;STUDENT;Fuchs;Patric;;;Analysis 2,2;Prog_2,4;
     * 24.1538.01;TEACHER;Stern;Olaf;40;Test in Line 5;Analysis 1,4
     */
    @Test
    void destructiveTestMissingAndBadWeekdaysValueInFile() {
        //Tests with missing Weekdays File
        File missingWeekDaysFile = Paths.get(fileDir.toString(), "missingarguments", "PeoplePropertiesTestMissingWeekdays.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new PeopleCsvLoader(missingWeekDaysFile));
        try {
            new PeopleCsvLoader(missingWeekDaysFile);
        } catch (BadCsvFormatException e) {
            assertEquals("Missing or Bad argument - in Line: 5", e.getMessage());
        }
        //Tests with bad Weekdays File
        File badWeekdaysFile = Paths.get(fileDir.toString(), "badarguments", "PeoplePropertiesTestBadWeekdays.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new PeopleCsvLoader(badWeekdaysFile));
        try {
            new PeopleCsvLoader(badWeekdaysFile);
        } catch (BadCsvFormatException e) {
            assertEquals("Missing or Bad argument - in Line: 5", e.getMessage());
        }
    }

    /**
     * A Csv File with a missing Argument in the "Module Hours" field of Line 5 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * 17.2212.12;STUDENT;Wartmann;Nico;;;Analysis 1,2;THIN,4;
     * 65.2655.48;STUDENT;Büchi;Adrian;;;Analysis 1,2;THIN,4;
     * 35.5215.65;STUDENT;Weber;Philippe;;;Analysis 2,2;Prog_2,Test in Line 3;
     * 45.2385.75;STUDENT;Fuchs;Patric;;;Analysis 2,2;Prog_2,4;
     * 24.1538.01;TEACHER;Stern;Olaf;40;MO,TU,TH;Analysis 1,4
     */
    @Test
    void destructiveTestBadsubjectHoursValueInFile() {
        File badModuleHoursFile = Paths.get(fileDir.toString(), "badarguments", "PeoplePropertiesTestBadModuleHours.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new PeopleCsvLoader(badModuleHoursFile));
        try {
            new PeopleCsvLoader(badModuleHoursFile);
        } catch (BadCsvFormatException e) {
            assertEquals("Cannot parse subject hours - in Line: 3", e.getMessage());
        }
    }

    /**
     * Extracts only Teachers from a List<Person>
     *
     * @param people List of Persons
     * @return List of Teachers
     */
    private List<Person> getTeachersFromList(List<Person> people) {
        List<Person> teachers = new ArrayList<>();
        for (Person person : people) {
            if (person.getSchoolVisitorRole().contains(TEACHER)) {
                teachers.add(person);
            }
        }
        return teachers;
    }
}