package eu.zavadil.prototype1.ui;

import com.sun.javafx.collections.ObservableListWrapper;
import eu.zavadil.prototype1.Controller;
import eu.zavadil.prototype1.ControllerSettings;
import eu.zavadil.prototype1.FaceDetectorProvidersEnum;
import eu.zavadil.prototype1.FaceMatcherProvidersEnum;
import eu.zavadil.prototype1.core.Logger;
import javafx.application.Platform;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ListView;
import javafx.scene.text.TextFlow;

import java.util.LinkedList;

public class MainWindowController implements Logger {

    @FXML
    private ListView console;

    private ObservableListWrapper consoleList = new ObservableListWrapper(new LinkedList());

    @FXML
    protected void initialize() {
        console.setItems(consoleList);

    }

    public void writeToConsole(String str) {
        consoleList.add(str);
    }

    @FXML
    protected void testConsole() {
        writeToConsole("The button was clicked!");
    }

    @FXML
    protected void start() {
        ControllerSettings settings = new ControllerSettings();

        /* Face++ providers */
        settings.getFaceDetectorSettings().face_detection_provider = FaceDetectorProvidersEnum.FACEPP;
        settings.getFaceMatcherSettings().face_matching_provider = FaceMatcherProvidersEnum.FACEPP;

        Controller controller = new Controller(settings, this);
        controller.processPicturesFolder("C:\\develop\\peopleCounter\\test\\pictures\\set1");
    }

    @Override
    public void log(String str) {
        Platform.runLater(() -> {
            writeToConsole(str);
        });
    }
}
