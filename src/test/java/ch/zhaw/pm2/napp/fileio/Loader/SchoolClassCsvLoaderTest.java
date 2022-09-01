package ch.zhaw.pm2.napp.fileio.Loader;

import ch.zhaw.pm2.napp.fileio.loader.PeopleCsvLoader;
import ch.zhaw.pm2.napp.fileio.loader.SchoolClassesCsvLoader;
import ch.zhaw.pm2.napp.fileio.loader.exception.BadCsvFormatException;
import ch.zhaw.pm2.napp.school.schoolclasses.Person;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Contains Unit Tests to check functionality of SchoolClassesCsvLoader Class
 *
 * @author wartmnic
 * @version 1.0.0
 */

class SchoolClassCsvLoaderTest {

    private List<Person> people;
    private Path fileDir;

    /**
     * Sets up a List of People. Classes can only be loaded if People are already created
     *
     * @throws BadCsvFormatException - if this Exception is thrown, check functionality of PeopleCsvLoader (with PeopleCsvLoaderTest)
     */
    @BeforeEach
    void setUp() throws BadCsvFormatException {
        fileDir = FileSystems.getDefault().getPath("", "src", "test", "resources", "schoolclasstest").toAbsolutePath();
        PeopleCsvLoader peopleCsvLoader = new PeopleCsvLoader(Paths.get(fileDir.toString(), "..", "peopletest", "PeoplePropertiesTestGood.csv").toFile());
        people = peopleCsvLoader.getResources();
    }

    /**
     * A csv File with 3 valid Lines is used to Test, if the Model is created according to the Data in the File.
     * A Series of Tests compares the properties of the created resource Objects with the expected Values.
     * <p>
     * following Values are expected from the csv File:
     * IT21ta_WIN-2;Analysis 1,2;THIN,4;TEACHER_24.1538.01;STUDENT_17.2212.12;STUDENT_65.2655.48
     * FA21ta_WIN-2;Analysis 2,2;THIN,4;TEACHER_24.1538.01;STUDENT_45.2385.75;STUDENT_65.2655.48
     * UV22ta_WIN-2;LinAlg,4;INCO,2;TEACHER_24.1538.01;STUDENT_35.5215.65;STUDENT_65.2655.48
     *
     * @throws BadCsvFormatException - Test is supposed to fail if this Exception is thrown
     */
    @Test
    void positiveTestWithValidValuesInFile() throws BadCsvFormatException {
        SchoolClassesCsvLoader schoolClassesCsvLoader = new SchoolClassesCsvLoader(Paths.get(fileDir.toString(), "SchoolClassPropertiesTestGood.csv").toFile(), people);
        assertEquals(3, schoolClassesCsvLoader.getResources().size());
        assertEquals(5, schoolClassesCsvLoader.getAllSubjects().size());
        assertEquals("FA21ta_WIN-2", schoolClassesCsvLoader.getResources().get(1).getId());
        assertEquals(2, schoolClassesCsvLoader.getResources().get(2).getSubjects().size());
        assertEquals(2, schoolClassesCsvLoader.getResources().get(2).getStudents().size());
    }

    /**
     * A Csv File with a missing Argument in the "Class Id" field of Line 1 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * following Values are expected from the csv File:
     * ;Test - Line 1,2;THIN,4;TEACHER_24.1538.01;STUDENT_17.2212.12;STUDENT_65.2655.48
     * ;Test - Line 2,2;THIN,4;TEACHER_24.1538.01;STUDENT_45.2385.75;STUDENT_65.2655.48
     * UV22ta_WIN-2;LinAlg,4;INCO,2;TEACHER_24.1538.01;STUDENT_35.5215.65;STUDENT_65.2655.48
     */
    @Test
    void destructiveTestMissingClassIdValueInFile() {
        File missingClassIdFile = Paths.get(fileDir.toString(), "missingargument", "SchoolClassPropertiesTestMissingId.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new SchoolClassesCsvLoader(missingClassIdFile, people));
        try {
            new SchoolClassesCsvLoader(missingClassIdFile, people);
        } catch (BadCsvFormatException e) {
            assertEquals("SchoolClass_ID Fields is Empty - in Line: 1", e.getMessage());
        }
    }

    /**
     * A Csv File with a bad Argument in the "Subject" field of Line 2 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * following Values are expected from the csv File:
     * IT21ta_WIN-2;Analysis 1,2;THIN,4;TEACHER_24.1538.01;STUDENT_17.2212.12;STUDENT_65.2655.48
     * FA21ta_WIN-2;123;123TEACHER_24.1538.01;STUDENT_45.2385.75;STUDENT_65.2655.48
     * UV22ta_WIN-2;LinAlg,4;INCO,2;TEACHER_24.1538.01;STUDENT_35.5215.65;STUDENT_65.2655.48
     */
    @Test
    void destructiveTestBadSubjectsValueInFile() {
        File badClassIdFile = Paths.get(fileDir.toString(), "badargument", "SchoolClassPropertiesTestBadSubjects.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new SchoolClassesCsvLoader(badClassIdFile, people));
        try {
            new SchoolClassesCsvLoader(badClassIdFile, people);
        } catch (BadCsvFormatException e) {
            assertEquals("Bad Subject Format - in Line: 2", e.getMessage());
        }
    }

    /**
     * A Csv File with a missing Argument in the "Teacher" field of Line 1 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * <p>
     * following Values are expected from the csv File:
     * IT21ta_WIN-2;Analysis 1,2;THIN,4;STUDENT_17.2212.12;STUDENT_65.2655.48
     * FA21ta_WIN-2;Analysis 2,2;THIN,4;TEACHER_24.1538.01;STUDENT_45.2385.75;STUDENT_65.2655.48
     * UV22ta_WIN-2;LinAlg,4;INCO,2;TEACHER_24.1538.01;STUDENT_35.5215.65;STUDENT_65.2655.48
     * <p>
     * A Csv File with a bad Argument in the "Teacher" field of Line 3 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * following Values are expected from the csv File:
     * IT21ta_WIN-2;Analysis 1,2;THIN,4;TEACHER_24.1538.01;STUDENT_17.2212.12;STUDENT_65.2655.48
     * FA21ta_WIN-2;Analysis 2,2;THIN,4;TEACHER_24.1538.01;STUDENT_45.2385.75;STUDENT_65.2655.48
     * UV22ta_WIN-2;LinAlg,4;INCO,2;TEACHER_Test in Line 3;STUDENT_35.5215.65;STUDENT_65.2655.48
     */
    @Test
    void destructiveTestBadAndMissingTeacherValueInFile() {
        //Tests with missing Teacher File
        File missingTeacherFile = Paths.get(fileDir.toString(), "missingargument", "SchoolClassPropertiesTestMissingTeacher.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new SchoolClassesCsvLoader(missingTeacherFile, people));
        try {
            new SchoolClassesCsvLoader(missingTeacherFile, people);
        } catch (BadCsvFormatException e) {
            assertEquals("No Teacher assigned to SchoolClass - in Line: 1", e.getMessage());
        }

        //Tests with bad Teacher File
        File badTeacherFile = Paths.get(fileDir.toString(), "badargument", "SchoolClassPropertiesTestBadTeacher.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new SchoolClassesCsvLoader(badTeacherFile, people));
        try {
            new SchoolClassesCsvLoader(badTeacherFile, people);
        } catch (BadCsvFormatException e) {
            assertEquals("Teacher with Matriculation Number: \"Test in Line 3\" does not exist - in Line: 3", e.getMessage());
        }
    }

    /**
     * A Csv File with a missing Argument in the "Student" field of Line 1 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * <p>
     * following Values are expected from the csv File:
     * IT21ta_WIN-2;Analysis 1,2;THIN,4;TEACHER_24.1538.01;
     * FA21ta_WIN-2;Analysis 2,2;THIN,4;TEACHER_24.1538.01;STUDENT_45.2385.75;STUDENT_65.2655.48
     * UV22ta_WIN-2;LinAlg,4;INCO,2;TEACHER_24.1538.01;STUDENT_35.5215.65;STUDENT_65.2655.48
     * <p>
     * A Csv File with a bad Argument in the "Student" field of Line 2 is tested
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * following Values are expected from the csv File:
     * IT21ta_WIN-2;Analysis 1,2;THIN,4;TEACHER_24.1538.01;STUDENT_17.2212.12;STUDENT_65.2655.48
     * FA21ta_WIN-2;Analysis 2,2;THIN,4;TEACHER_24.1538.01;STUDENT_Test in Line 2
     * UV22ta_WIN-2;LinAlg,4;INCO,2;TEACHER_24.1538.01;STUDENT_35.5215.65;STUDENT_65.2655.48
     */
    @Test
    void destructiveTestBadAndMissingStudentValueInFile() {
        //Tests with missing Student File
        File missingStudentFile = Paths.get(fileDir.toString(), "missingargument", "SchoolClassPropertiesTestMissingStudent.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new SchoolClassesCsvLoader(missingStudentFile, people));
        try {
            new SchoolClassesCsvLoader(missingStudentFile, people);
        } catch (BadCsvFormatException e) {
            assertEquals("No Student assigned to SchoolClass - in Line: 1", e.getMessage());
        }
        //Tests with bad Teacher File
        File badStudentFile = Paths.get(fileDir.toString(), "badargument", "SchoolClassPropertiesTestBadStudent.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new SchoolClassesCsvLoader(badStudentFile, people));
        try {
            new SchoolClassesCsvLoader(badStudentFile, people);
        } catch (BadCsvFormatException e) {
            assertEquals("Student with Matriculation Number: \"Test in Line 2\" does not exist - in Line: 2", e.getMessage());
        }
    }
}