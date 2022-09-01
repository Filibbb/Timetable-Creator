package ch.zhaw.pm2.napp.school.timetable;

import ch.zhaw.pm2.napp.school.building.Building;
import ch.zhaw.pm2.napp.school.schoolclasses.SchoolClass;
import ch.zhaw.pm2.napp.school.schoolclasses.Teacher;
import ch.zhaw.pm2.napp.school.timetable.exceptions.TimetableConversionException;
import ch.zhaw.pm2.napp.school.timetable.exceptions.TimetableException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static ch.zhaw.pm2.napp.school.SchoolUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

/**
 * Test Class to support TimeTableConverterUtils and make sure conversion is done correctly and formatting works as excepted.
 *
 * @author buechad1
 */
class TimeTableConverterUtilTest {

    private static final int FIRST_LIST_INDEX = 0;
    private static final int MONDAY_INDEX = 0;
    private static final String EXPECTED_CONVERTED_STRING = "MONDAY;[08:00-08:45 Math 102 Herbert Apple];[08:50-09:35 Math 102 Herbert Apple];[10:00-10:45 German 102 Herbert Apple];[10:50-11:35 German 102 Herbert Apple];[12:00-12:45 German 102 Herbert Apple];[12:50-13:35 German 102 Herbert Apple];[14:00-14:45 Sport 102 Herbert Apple];";
    private Timetable generatedTimetable;

    /**
     * Setups test cases by creating simple school classes and teachers as well as generating a timetable for it to make sure conversion works correctly with a timetable
     *
     * @throws TimetableException - should not be thrown in setup
     */
    @BeforeEach
    public void setUp() throws TimetableException {
        SchoolClass schoolClass = createAllSchoolClasses().get(FIRST_LIST_INDEX);
        List<Teacher> allTeachers = createAllTeachers();
        List<Building> schoolBuildings = createAllBuildings();
        TimetableGenerator timetableGenerator = new TimetableGenerator(allTeachers, schoolBuildings);
        timetableGenerator.generateTimeTableForClass(schoolClass);
        generatedTimetable = schoolClass.getTimetable();
    }

    /**
     * Tests whether the converting method works correctly and creates a correct csv string with the expected length, formatting and with a ; at the end.
     *
     * @throws TimetableConversionException - should not be thrown therefore no need to catch
     */
    @Test
    public void testConvertingTimeTableStringSuccessful() throws TimetableConversionException {
        final String[] convertTimetablesToCsvString = TimeTableConverterUtil.convertTimetablesToCsvString(generatedTimetable);
        assertEquals(1, convertTimetablesToCsvString.length);
        assertEquals(EXPECTED_CONVERTED_STRING, convertTimetablesToCsvString[MONDAY_INDEX]);
        assertEquals(';', convertTimetablesToCsvString[MONDAY_INDEX].charAt(convertTimetablesToCsvString[MONDAY_INDEX].toCharArray().length - 1));
    }

    /**
     * Tests if conversion works correctly and gives an excpection if input is null. This can occur if timetable was not generated and therefore is still null and its important to assert that the exception is thrown as error handling depends on it.
     */
    @Test
    public void testConversionIfNoTimeTableIsDefined() {
        assertThrows(TimetableConversionException.class, () -> TimeTableConverterUtil.convertTimetablesToCsvString(null));
    }

}