package ch.zhaw.pm2.napp.ui.dropdown;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;

import java.util.List;

/**
 * Filter Component Controller
 * <p>
 * A Controller containing the logic for the reusable filter components with Combo Boxes.
 * The Combo Boxes are disabled until they are initialized when the respective data is available.
 *
 * @author weberph5
 */
public class FilterComponentController {
    private final SimpleStringProperty selectionProperty = new SimpleStringProperty();
    @FXML
    private ComboBox<Object> dropdown;

    /**
     * Initializes the Combo Box component for usage with the respective name and data.
     * Only after initialization, the Combo Box will become enabled.
     *
     * @param dropDownLabel The prompt text of the respective combo box. Should give a hint about the data it contains.
     * @param list          A list of the data which can be selected.
     */
    public void initialize(String dropDownLabel, List<String> list) {
        dropdown.setPromptText(dropDownLabel);
        dropdown.getItems().addAll(list);
        dropdown.setDisable(false);
    }

    /**
     * Reads the selection from the ComboBox and passes the selection to the Table View Controller.
     */
    @FXML
    private void applyFilter() {
        selectionProperty.set(dropdown.getValue().toString());
    }

    /**
     * Adds a change listener to the filter property to listen to filter updates
     *
     * @param selectionChangeListener a {@link ChangeListener<String>} to execute certain actions upon receiving new input
     */
    public void addFilterPropertyChangeListener(ChangeListener<String> selectionChangeListener) {
        this.selectionProperty.addListener(selectionChangeListener);
    }

}