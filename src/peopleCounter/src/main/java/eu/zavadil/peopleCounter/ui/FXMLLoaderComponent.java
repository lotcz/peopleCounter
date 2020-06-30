package eu.zavadil.peopleCounter.ui;

import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class FXMLLoaderComponent {

    @Autowired
    ApplicationContext context;

    public Parent loadFXML(String path) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
        fxmlLoader.setControllerFactory(context::getBean);
        return fxmlLoader.load();
    }

    public Parent loadFXML(String path, Object controller) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource(path));
        fxmlLoader.setController(controller);
        return fxmlLoader.load();
    }
}
