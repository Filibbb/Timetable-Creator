package ch.zhaw.pm2.napp;

import ch.zhaw.pm2.napp.logger.LogConfiguration;
import ch.zhaw.pm2.napp.ui.TimeTableCreatorController;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.logging.Logger;

import static java.util.logging.Logger.getLogger;

/**
 * TimeTableCreatorApplication
 * <p>
 * Contains the java-fx main application class where the main window is being loaded and applied to the primary stage of the java fx tool
 * Additionally all controller that are automatically injected are initialized there.
 *
 * @author buechad1
 */
public class TimeTableCreatorApplication extends Application {

    private static final Logger LOGGER = getLogger(LogConfiguration.class.getCanonicalName());
    private static final int MIN_HEIGHT = 1080;
    private static final int MIN_WIDTH = 1920;

    /**
     * @param primaryStage the primary stage for this application, onto which
     *                     the application scene can be set.
     *                     Applications may create other stages, if needed, but they will not be
     *                     primary stages.
     */
    @Override
    public void start(Stage primaryStage) {
        try {
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("ui/time_table_creator_window.fxml"));
            BorderPane rootNode = fxmlLoader.load();
            final TimeTableCreatorController controller = fxmlLoader.getController();
            controller.initializeFileSelectors(primaryStage);
            Scene scene = new Scene(rootNode);
            primaryStage.setTitle("Time Table Creator");
            primaryStage.setMinWidth(MIN_WIDTH);
            primaryStage.setMinHeight(MIN_HEIGHT);
            primaryStage.setResizable(false);
            primaryStage.setScene(scene);
            primaryStage.show();
            LOGGER.info("Application started");
        } catch (IOException e) {
            LOGGER.severe("There was an error starting the application. Please check message: " + e.getMessage());
        }
    }
}
