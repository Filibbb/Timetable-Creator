package ch.zhaw.pm2.napp.ui;

import ch.zhaw.pm2.napp.fileio.loader.BuildingsCsvLoader;
import ch.zhaw.pm2.napp.fileio.loader.PeopleCsvLoader;
import ch.zhaw.pm2.napp.fileio.loader.SchoolClassesCsvLoader;
import ch.zhaw.pm2.napp.fileio.loader.exception.BadCsvFormatException;
import ch.zhaw.pm2.napp.logger.LogConfiguration;
import ch.zhaw.pm2.napp.school.School;
import ch.zhaw.pm2.napp.school.building.Building;
import ch.zhaw.pm2.napp.school.schoolclasses.AbstractSchoolEntity;
import ch.zhaw.pm2.napp.school.schoolclasses.Person;
import ch.zhaw.pm2.napp.school.schoolclasses.SchoolClass;
import ch.zhaw.pm2.napp.school.schoolclasses.Teacher;
import ch.zhaw.pm2.napp.school.timetable.exceptions.TimetableConversionException;
import ch.zhaw.pm2.napp.school.timetable.exceptions.TimetableException;
import ch.zhaw.pm2.napp.school.timetable.exceptions.TimetableExportException;
import ch.zhaw.pm2.napp.school.timetable.export.TimeTableExporter;
import ch.zhaw.pm2.napp.ui.dropdown.FilterComponentController;
import ch.zhaw.pm2.napp.ui.fileloader.FileExtension;
import ch.zhaw.pm2.napp.ui.fileloader.FileLoaderComponentController;
import ch.zhaw.pm2.napp.ui.timetableview.TimetableViewController;
import javafx.beans.value.ChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import static ch.zhaw.pm2.napp.school.schoolclasses.SchoolVisitorRole.TEACHER;
import static java.util.logging.Logger.getLogger;
import static javafx.scene.control.Alert.AlertType.ERROR;

/**
 * TimeTableCreatorController
 * <p>
 * The controller class containing logic for initializing the ui and main screen as well as loading submodules and reusable controllers such as {@link FileLoaderComponentController}
 * Controllers are not used but need to be injected therefore fields must consist within class
 *
 * @author buechad1
 */
public class TimeTableCreatorController {
    private static final String CSV_FILE_EXTENSION_NAME = "*.csv";
    private static final Logger LOGGER = getLogger(LogConfiguration.class.getCanonicalName());

    @FXML
    private FileLoaderComponentController buildingSelectorController;
    @FXML
    private FileLoaderComponentController peopleSelectorController;
    @FXML
    private FileLoaderComponentController schoolClassesController;
    @FXML
    private FilterComponentController roomsDropDownController;
    @FXML
    private FilterComponentController personDropDownController;
    @FXML
    private FilterComponentController classesDropDownController;

    @FXML
    private TimetableViewController timetableViewController;

    @FXML
    private Button exportTimeTableBtn;

    private List<Person> schoolAttendees;

    private List<SchoolClass> schoolClasses;
    private List<Building> buildings;

    /**
     * In order to make sure that all ui-elements are initialized correctly, this method was created to make sure file-selector components on ui are being initialized.
     * It calls the {@link FileLoaderComponentController#initialize(Stage, String, FileExtension)} method in order to make sure stage is attached and create the correctly labeled buttons and textfield
     * The controller can never be null as it is injected upon loading the main screen.
     *
     * @param primaryStage - the primary window stage of the application
     */
    public void initializeFileSelectors(Stage primaryStage) {
        initializeBuildingSelectorController(primaryStage);
        initializeTeachersSelectorController(primaryStage);
        initializeStudentsSelectorController(primaryStage);
    }

    private void initializeBuildingSelectorController(Stage primaryStage) {
        final FileExtension csvBuildingSheet = new FileExtension(CSV_FILE_EXTENSION_NAME, "CSV building sheet");
        buildingSelectorController.initialize(primaryStage, "Building", csvBuildingSheet);
    }

    private void initializeTeachersSelectorController(Stage primaryStage) {
        final FileExtension csvPeopleSheet = new FileExtension(CSV_FILE_EXTENSION_NAME, "People CSV sheet");
        peopleSelectorController.initialize(primaryStage, "People", csvPeopleSheet);
    }

    private void initializeStudentsSelectorController(Stage primaryStage) {
        final FileExtension csvSchoolClassSheet = new FileExtension(CSV_FILE_EXTENSION_NAME, "CSV school classes sheet");
        schoolClassesController.initialize(primaryStage, "School Classes", csvSchoolClassSheet);
    }

    @FXML
    private void generateTimeTable(ActionEvent actionEvent) {
        try {
            loadSelectedFiles();
            generateTimeTables();
            initializeFilters(schoolAttendees, schoolClasses);
            initializeTimetableView(schoolAttendees, schoolClasses);
        } catch (BadCsvFormatException | TimetableException e) {
            LOGGER.severe(e.getMessage());
            showAlertDialog("Please select a file!", e.getMessage());
        }
    }

    private void initializeTimetableView(List<Person> schoolAttendees, List<SchoolClass> schoolClasses) {
        timetableViewController.initialize(schoolAttendees, schoolClasses);
    }

    private void generateTimeTables() throws TimetableException {
        final List<Teacher> allTeachers = getAllTeachers(schoolAttendees);
        School school = new School(allTeachers, buildings, schoolClasses);
        school.generateTimetableForAllSchoolClasses();
    }

    private void loadSelectedFiles() throws BadCsvFormatException {
        final BuildingsCsvLoader buildingsCsvLoader = new BuildingsCsvLoader(buildingSelectorController.getSelectedFile());
        buildings = buildingsCsvLoader.getResources();
        final PeopleCsvLoader peopleCsvLoader = new PeopleCsvLoader(peopleSelectorController.getSelectedFile());
        schoolAttendees = peopleCsvLoader.getResources();
        final SchoolClassesCsvLoader schoolClassesCsvLoader = new SchoolClassesCsvLoader(schoolClassesController.getSelectedFile(), schoolAttendees);
        schoolClasses = schoolClassesCsvLoader.getResources();
    }

    @FXML
    private void exportTimeTable(ActionEvent actionEvent) {
        try {
            if (schoolClasses == null || schoolAttendees == null) {
                showAlertDialog("Timetable export error!", "No timetables can be created with no given school attendees.");
            } else {
                final List<AbstractSchoolEntity> allEntities = new ArrayList<>();
                allEntities.addAll(schoolClasses);
                allEntities.addAll(schoolAttendees);
                final TimeTableExporter timeTableExporter = new TimeTableExporter();
                timeTableExporter.exportTimeTables(allEntities);
                showAlertDialog("Success!", "Timetable exported successfully");
            }
        } catch (TimetableConversionException | TimetableExportException e) {
            LOGGER.severe(e.getMessage());
            showAlertDialog("Error exporting csv!", e.getMessage());
        }
    }

    private List<Teacher> getAllTeachers(List<Person> schoolAttendees) {
        List<Teacher> allTeachers = new ArrayList<>();
        for (Person schoolAttendee : schoolAttendees) {
            if (schoolAttendee.getSchoolVisitorRole().contains(TEACHER)) {
                allTeachers.add((Teacher) schoolAttendee);
            }
        }
        return allTeachers;
    }

    private void initializeFilters(List<Person> schoolAttendees, List<SchoolClass> schoolClasses) {
        initializePersonFilter(schoolAttendees);
        initializeSchoolClassFilter(schoolClasses);
    }

    private void initializeSchoolClassFilter(List<SchoolClass> schoolClasses) {
        List<String> allClasses = new ArrayList<>();
        for (SchoolClass schoolClass : schoolClasses) {
            String id = schoolClass.getId();
            allClasses.add(id);
        }
        classesDropDownController.initialize("Select Class", allClasses);
        classesDropDownController.addFilterPropertyChangeListener(getFilterSelectionChangeListener());
    }

    private ChangeListener<String> getFilterSelectionChangeListener() {
        return (observable, oldValue, newValue) -> {
            AbstractSchoolEntity selectedEntity = timetableViewController.getMatchingSelectionObject(newValue);
            if (selectedEntity != null) {
                timetableViewController.showTimetable(selectedEntity);
            } else {
                showAlertDialog("Error filtering entities", "No entity found to filter by. Please make sure user exist.");
            }
        };
    }

    private void initializePersonFilter(List<Person> schoolAttendees) {
        List<String> allPersons = new ArrayList<>();
        for (Person person : schoolAttendees) {
            String id = person.getId();
            allPersons.add(id);
        }
        personDropDownController.initialize("Select Person", allPersons);
        personDropDownController.addFilterPropertyChangeListener(getFilterSelectionChangeListener());
    }

    private void showAlertDialog(String dialogTitle, String message) {
        Alert alert = new Alert(ERROR);
        alert.setTitle(dialogTitle);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
