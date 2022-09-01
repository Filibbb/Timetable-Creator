package ch.zhaw.pm2.napp.fileio.writer;

import ch.zhaw.pm2.napp.school.building.Building;
import ch.zhaw.pm2.napp.school.schoolclasses.SchoolClass;
import ch.zhaw.pm2.napp.school.schoolclasses.Teacher;
import ch.zhaw.pm2.napp.school.timetable.TimeTableConverterUtil;
import ch.zhaw.pm2.napp.school.timetable.Timetable;
import ch.zhaw.pm2.napp.school.timetable.TimetableGenerator;
import ch.zhaw.pm2.napp.school.timetable.exceptions.TimetableConversionException;
import ch.zhaw.pm2.napp.school.timetable.exceptions.TimetableException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static ch.zhaw.pm2.napp.fileio.writer.CsvWriter.CSV_FILE_ENDING;
import static ch.zhaw.pm2.napp.school.SchoolUtil.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * Tests whether csv writing into file works and files can be created and are at expected locations
 *
 * @author buechad1
 */
class CsvWriterTest {

    public static final String FILE_NAME = "xy";
    public static final String EXPECTED_CSV_CONTENT = "MONDAY;[08:00-08:45 Math 102 Herbert Apple];[08:50-09:35 Math 102 Herbert Apple];[10:00-10:45 German 102 Herbert Apple];[10:50-11:35 German 102 Herbert Apple];[12:00-12:45 German 102 Herbert Apple];[12:50-13:35 German 102 Herbert Apple];[14:00-14:45 Sport 102 Herbert Apple];";
    private final CsvWriter csvWriter = new CsvWriter();
    private static final String DIRECTORY_NAME = "TimeTableCreator";
    private static final String PATH = System.getProperty("user.home") + FileSystems.getDefault().getSeparator() + DIRECTORY_NAME;
    private static final int FIRST_LIST_INDEX = 0;

    private Timetable generatedTimetable;
    private File exportDirectory;

    @BeforeEach
    void setUp() throws TimetableException {
        exportDirectory = new File(PATH);
        SchoolClass schoolClass = createAllSchoolClasses().get(FIRST_LIST_INDEX);
        List<Teacher> allTeachers = createAllTeachers();
        List<Building> schoolBuildings = createAllBuildings();
        TimetableGenerator timetableGenerator = new TimetableGenerator(allTeachers, schoolBuildings);
        timetableGenerator.generateTimeTableForClass(schoolClass);
        generatedTimetable = schoolClass.getTimetable();
    }

    /**
     * Tests whether file is being placed in correct directory, can be created and has correct starting prefix and correct ending prefix (.csv)
     * Cannot test whether full name matches due to inaccessible long timestamp which is unique
     *
     * @throws IOException - should not be thrown!
     */
    @Test
    public void testFileCreationSuccessfulWithEmptyCsv() throws IOException {
        String[] csvLines = new String[]{};
        csvWriter.writeCsv(FILE_NAME, csvLines);
        assertTrue(exportDirectory.exists());
        assertTrue(exportDirectory.listFiles().length != 0);
        assertTrue(exportDirectory.listFiles()[0].getName().startsWith("xy"));
        assertTrue(exportDirectory.listFiles()[0].getName().endsWith(CSV_FILE_ENDING));
    }

    /**
     * Tests if written file content is the same as inputted file content in order to make sure csv input values are inputted as expected and converted string can be read the same way as written and no changes occur.
     * Also makes sure that file was created and has correct naming pattern specified by the test
     *
     * @throws IOException                  - should not be thrown!
     * @throws TimetableConversionException - should not be thrown!
     */
    @Test
    public void testsIfWritingCsvWorksCorrectly() throws IOException, TimetableConversionException {
        final String[] convertTimetablesToCsvString = TimeTableConverterUtil.convertTimetablesToCsvString(generatedTimetable);
        csvWriter.writeCsv(FILE_NAME, convertTimetablesToCsvString);
        assertTrue(exportDirectory.exists());
        assertTrue(exportDirectory.listFiles().length != 0);
        final File createdFile = exportDirectory.listFiles()[0];
        assertTrue(createdFile.getName().startsWith("xy"));
        assertTrue(createdFile.getName().endsWith(CSV_FILE_ENDING));
        assertEquals(EXPECTED_CSV_CONTENT, Files.readString(Path.of(createdFile.getPath())).trim());
    }

    /**
     * Make sure files are being deleted after testing
     */
    @AfterEach
    public void tearDown() {
        for (File file : exportDirectory.listFiles()) {
            file.delete();
        }
        exportDirectory.delete();
    }
}