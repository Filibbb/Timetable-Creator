package ch.zhaw.pm2.napp.ui.fileloader;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

import java.io.File;

/**
 * FileLoaderComponentController
 * <p>
 * A controller holding logic for the file-loader-component that can be reused.
 * Files can be accessed via a getter. May be null.
 * In order to use the controller make sure  {@link FileLoaderComponentController#initialize(Stage, String, FileExtension)} is called to initialize component correctly.
 *
 * @author buechad1
 */
public class FileLoaderComponentController {

    @FXML
    private Button fileSelectorButton;

    @FXML
    private TextField filePath;

    private File selectedFile;

    private Stage primaryStage;
    private FileExtension fileExtension;

    /**
     * Initializes the component for using, makes sure all labels are set correctly and file-extension is defined
     *
     * @param primaryStage      - the primary stage or stage where the component should be attached to.
     * @param fileSelectorLabel - the label text displayed within the button
     * @param fileExtension     - the {@link FileExtension} defining a file extension type and a description for it
     */
    public void initialize(Stage primaryStage, String fileSelectorLabel, FileExtension fileExtension) {
        this.primaryStage = primaryStage;
        this.fileExtension = fileExtension;
        fileSelectorButton.setText(fileSelectorLabel);
        filePath.setPromptText(fileExtension.fileExtensionDescription());
    }

    @FXML
    private void selectFileAction() {
        final FileChooser fileChooser = new FileChooser();
        final ExtensionFilter extFilter = new ExtensionFilter(fileExtension.fileExtensionDescription(), fileExtension.fileExtensionName());
        fileChooser.getExtensionFilters().add(extFilter);
        final File file = fileChooser.showOpenDialog(primaryStage);
        if (file != null && file.exists()) {
            selectedFile = file;
            filePath.setText(selectedFile.getAbsolutePath());
        }
    }

    public File getSelectedFile() {
        if (selectedFile == null && !filePath.getText().isEmpty()) {
            final File file = new File(filePath.getText());
            if (file.exists()) {
                selectedFile = file;
            }
        }
        return selectedFile;
    }
}

