package ch.zhaw.pm2.napp.fileio.Loader;

import ch.zhaw.pm2.napp.fileio.loader.BuildingsCsvLoader;
import ch.zhaw.pm2.napp.fileio.loader.exception.BadCsvFormatException;
import ch.zhaw.pm2.napp.school.building.Building;
import ch.zhaw.pm2.napp.school.building.exception.NoSuchRoomException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Contains Unit Tests to check functionality of BuildingCsvLoader Class
 *
 * @author wartmnic
 * @version 1.0.0
 */
class BuildingsCsvLoaderTest {

    private Path fileDir;

    @BeforeEach
    void setUp() {
        fileDir = FileSystems.getDefault().getPath("src", "test", "resources", "buildingtest").toAbsolutePath();
    }

    /**
     * A csv File with 5 valid Lines is used to Test, if the Model is created according to the Data in the File.
     * A Series of Tests compares the properties of the created resource Objects with the expected Values.
     * <p>
     * following Values are expected from the csv File:
     * Hauptgebaeude;1;Gruppenraum 100;20
     * Hauptgebaeude;2;Grosser Hoersaal;250
     * Hauptgebaeude;3;Gruppenraum 333;10
     * Nebengebaeude1;1;Schulzimmer 121;20
     * Nebengebaeude2;1;Pausenraum;50
     *
     * @throws BadCsvFormatException - Test is supposed to fail if this Exception is thrown
     * @throws NoSuchRoomException   - Test is supposed to fail if this Exception is thrown
     */
    @Test
    void positiveTestWithValidValuesInFile() throws BadCsvFormatException, NoSuchRoomException {
        BuildingsCsvLoader buildingsCsvLoader = new BuildingsCsvLoader(Paths.get(fileDir.toString(), "BuildingsTestGood.csv").toFile());
        List<Building> buildingList = buildingsCsvLoader.getResources();

        Building nebengebaeude2 = getBuildingWithIdentifier("Nebengebaeude2", buildingList);

        assertEquals("Hauptgebaeude", buildingList.get(0).getBuildingIdentifier());
        assertEquals(250, buildingList.get(0).getRoomWithIdentifier("Grosser Hoersaal").getCapacity());
        assertEquals(3, buildingList.get(0).getFloors().size());
        assertNotNull(nebengebaeude2);
        assertEquals(1, nebengebaeude2.getAllRooms().size());
    }

    /**
     * A csv File where there are too few Arguments in Line 3 and 4 is used to Test.
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * following Values are expected from the csv File:
     * Hauptgebaeude;2;Grosser Hoersaal;250
     * Test Line3; Should fail
     * Test Line4; already failed - no exception
     * Nebengebaeude2;1;Pausenraum;50
     */
    @Test
    void destructiveTestMissingArgumentsInFile() {
        File missingArgumentsFile = Paths.get(fileDir.toString(),"missingarguments", "BuildingsTestMissingArguments.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new BuildingsCsvLoader(missingArgumentsFile));
        try {
            new BuildingsCsvLoader(missingArgumentsFile);
        } catch (BadCsvFormatException e) {
            assertEquals("Input Arguments are missing - in Line: 3", e.getMessage());
        }
    }

    /**
     * A csv File where there are too Many Arguments in Line 2 and 3 is used to Test.
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * Hauptgebaeude;1;Gruppenraum 100;20
     * T;3;S;7; Zu viele Argumente
     * T;3;S;7;2; already failed - no exception expected
     * Nebengebaeude1;1;Schulzimmer 121;20
     * Nebengebaeude2;1;Pausenraum;50
     */
    @Test
    void destructiveTestTooManyArgumentsInFile() {
        File tooManyArgumentsFile = Paths.get(fileDir.toString(),"badarguments", "BuildingsTestTooManyArguments.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new BuildingsCsvLoader(tooManyArgumentsFile));
        try {
            new BuildingsCsvLoader(tooManyArgumentsFile);
        } catch (BadCsvFormatException e) {
            assertEquals("Too many Arguments - in Line: 2", e.getMessage());
        }
    }

    /**
     * A csv File where there are too bad Arguments in Line 2 and 3 is used to Test.
     * It is tested, if the generation of the Model creates the expected Exception.
     * <p>
     * Hauptgebaeude;1;Gruppenraum 100;20
     * Hauptgebaeude;2;Grosser Hoersaal;Test in Line 2
     * Hauptgebaeude;3;Gruppenraum 333; already failed - no exception expected
     * Nebengebaeude1;1;Schulzimmer 121;20
     * Nebengebaeude2;1;Pausenraum;50
     */
    @Test
    void destructiveTestBadArgumentsInFile() {
        File tooBadArgumentsFile = Paths.get(fileDir.toString(), "badarguments", "BuildingsTestBadArguments.csv").toFile();
        assertThrows(BadCsvFormatException.class, () -> new BuildingsCsvLoader(tooBadArgumentsFile));
        try {
            new BuildingsCsvLoader(tooBadArgumentsFile);
        } catch (BadCsvFormatException e) {
            assertEquals("Capacity can not be parsed - in Line: 2", e.getMessage());
        }
    }

    private Building getBuildingWithIdentifier(String buildingIdentifier, List<Building> buildingList) {
        for (Building building : buildingList) {
            if (building.getBuildingIdentifier().equals(buildingIdentifier)) {
                return building;
            }
        }
        return null;
    }
}