package ch.zhaw.pm2.napp.ui.timetableview;

import ch.zhaw.pm2.napp.school.schoolclasses.AbstractSchoolEntity;
import ch.zhaw.pm2.napp.school.schoolclasses.Person;
import ch.zhaw.pm2.napp.school.schoolclasses.SchoolClass;
import ch.zhaw.pm2.napp.school.timetable.Lesson;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.text.Text;

import java.time.DayOfWeek;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;


/**
 * Tableview Component Controller
 * <p>
 * A Controller containing the logic for the reusable tableview component to show timetable data.
 *
 * @author weberph5
 */
public class TimetableViewController {

    private static final int SUNDAY_INDEX = 6;
    @FXML
    private GridPane timetableGrid;
    private List<Person> schoolAttendees = new ArrayList<>();
    private List<SchoolClass> schoolClasses = new ArrayList<>();

    /**
     * Initializes the Table View with the data read from the csv files
     *
     * @param schoolAttendees all loaded teachers and students
     * @param schoolClasses   all loaded school classes
     */
    public void initialize(List<Person> schoolAttendees, List<SchoolClass> schoolClasses) {
        this.schoolAttendees = schoolAttendees;
        this.schoolClasses = schoolClasses;
    }

    /**
     * Looks for the Object based on the selection in the dropdown
     *
     * @param selection The selection in the dropdown
     * @return the selected object. Null if not found.
     */
    public AbstractSchoolEntity getMatchingSelectionObject(String selection) {
        final Optional<Person> filteredSchoolAttendee = schoolAttendees.stream().filter(person -> person.getId().equals(selection)).findAny();
        if (filteredSchoolAttendee.isPresent()) {
            return filteredSchoolAttendee.get();
        } else {
            final Optional<SchoolClass> filteredSchoolClass = schoolClasses.stream().filter(schoolClass -> schoolClass.getId().equals(selection)).findAny();
            if (filteredSchoolClass.isPresent()) {
                return filteredSchoolClass.get();
            }
        }
        return null;
    }

    /**
     * Show the selected timetable on the GridPane
     *
     * @param selectedObject The Entity selected with the filters
     */
    public void showTimetable(AbstractSchoolEntity selectedObject) {
        final Map<DayOfWeek, List<Lesson>> timetableMap = selectedObject.getTimetable().getTimetable();
        this.timetableGrid.getChildren().clear();
        this.timetableGrid.getColumnConstraints().add(new ColumnConstraints(300));
        this.timetableGrid.getRowConstraints().add(new RowConstraints(90));
        fillGridPane(timetableMap);
    }

    private void fillGridPane(Map<DayOfWeek, List<Lesson>> timetableMap) {
        for (DayOfWeek dayOfWeek : timetableMap.keySet()) {
            int columnIndex = calculateColumnIndexByWeekDay(dayOfWeek.getValue());
            this.timetableGrid.addColumn(columnIndex, new Text(dayOfWeek.toString()));
            for (Lesson lesson : timetableMap.get(dayOfWeek)) {
                this.timetableGrid.add(new Text(convertLessonToString(lesson)), columnIndex, timetableMap.get(dayOfWeek).indexOf(lesson) + 1);
            }
        }
    }

    private int calculateColumnIndexByWeekDay(int indexOfWeekDay) {
        int indexMinusOne = indexOfWeekDay - 1;
        return indexMinusOne < 0 ? SUNDAY_INDEX : indexMinusOne;
    }

    private String convertLessonToString(Lesson lesson) {
        return new StringBuilder().append(lesson.timeSlot().toString()).append("\n").append(lesson.subject().name()).append("\n").append(lesson.room().getRoomIdentifier()).append("\n").append(lesson.teacher().getFirstName()).append(" ").append(lesson.teacher().getLastName()).toString();
    }
}
