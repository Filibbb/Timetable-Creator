package ch.zhaw.pm2.napp.fileio.loader;

import ch.zhaw.pm2.napp.fileio.loader.exception.BadCsvFormatException;
import ch.zhaw.pm2.napp.school.building.Building;
import ch.zhaw.pm2.napp.school.building.Room;

import java.io.File;

import static java.lang.Integer.parseInt;

/**
 * BuildingsCsvLoader
 * <p>
 * this class is based on {@link AbstractCsvLoader} and therefore provides the functionality to retrieve a List of Objects
 * created from the values of a csv File.
 * </p>
 * <p>
 * This class specifically creates a List of buildings
 * </p>
 *
 * @author wartminc
 * @version 1.0.0
 */
public class BuildingsCsvLoader extends AbstractCsvLoader<Building> {
    private static final int INDEX_OF_BUILDING_ID = 0;
    private static final int INDEX_OF_FLOOR_ID = 1;
    private static final int INDEX_OF_ROOM_ID = 2;
    private static final int INDEX_OF_CAPACITY = 3;
    private static final int NUMBER_OF_PROPERTY_VALUES = 4;

    /**
     * Initializing csv Loader with File to be processed
     *
     * @param file File to be processed
     * @throws BadCsvFormatException with message and Line of csv, is thrown if the csv Values do not match the requirements
     */
    public BuildingsCsvLoader(File file) throws BadCsvFormatException {
        populateResources(readLinesOfFile(file));
    }

    /**
     * A String array representing a Line of the csv File is converted to a Building Object.
     *
     * @param properties String Array representing the Comma (actually ;) separated values of one line of the csv File
     * @return a Building Object
     * @throws BadCsvFormatException if there are more than 4 comma seperated Values
     */
    protected Building createResource(String[] properties) throws BadCsvFormatException {
        Building building;
        if (properties.length > NUMBER_OF_PROPERTY_VALUES) {
            throw new BadCsvFormatException("Too many Arguments");
        }
        try {
            Room room = new Room(properties[INDEX_OF_ROOM_ID], parseInt(properties[INDEX_OF_CAPACITY]));
            building = createOrReturnBuilding(properties[INDEX_OF_BUILDING_ID]);
            building.addRoomToFloor(room, properties[INDEX_OF_FLOOR_ID]);
        } catch (ArrayIndexOutOfBoundsException e) {
            throw new BadCsvFormatException("Input Arguments are missing");
        } catch ( NumberFormatException e) {
            throw new BadCsvFormatException("Capacity can not be parsed");
        }
        return building;
    }

    /**
     * Checks whether a Building with te according Identifies exists already and if so, returns it.
     * If there is no matching Building in the resources List already, a new one is created and returned.
     *
     * @param buildingIdentifier Identifier String that has to be compared to the preexisting Buildings' Identifiers
     * @return either the matching preexisting Building object, or a new one
     */
    private Building createOrReturnBuilding(String buildingIdentifier) {
        for (Building building : getResources()) {
            if (building.getBuildingIdentifier().equals(buildingIdentifier)) {
                return building;
            }
        }
        return new Building(buildingIdentifier);
    }
}