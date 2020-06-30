package eu.zavadil.peopleCounter.ui;

import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;

public abstract class WindowControllerBase {

    @Autowired
    FXMLLoaderComponent fxmlLoaderComponent;

    public Stage stage;

    public Scene scene;

    public abstract String getTitle();

    public abstract String getFXMLPath();

    public void load() throws IOException {
        Parent root = this.fxmlLoaderComponent.loadFXML(getFXMLPath());
        this.scene = new Scene(root);
        this.stage.setTitle(getTitle());
        this.stage.setScene(scene);
    }

    public void onShow() throws Exception {

    }

    public void show(Modality modality) throws Exception {
        this.hide();
        if (this.stage == null) {
            this.stage = new Stage();
            this.stage.initModality(modality);
        }
        if (this.scene == null) {
            this.load();
        }
        this.stage.show();
        this.onShow();
    }

    public void show(Stage stage, Modality modality) throws Exception {
        this.stage = stage;
        this.show(modality);
    }

    public void show() throws Exception {
        this.show(Modality.NONE);
    }

    public void show(Stage stage) throws Exception {
        this.stage = stage;
        this.show();
    }

    protected void hide() {
        if (this.stage != null) {
            this.stage.close();
        }
    }
}
