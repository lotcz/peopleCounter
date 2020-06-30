package eu.zavadil.peopleCounter.ui;

import javafx.stage.Stage;
import org.springframework.context.ApplicationEvent;

public class ApplicationReadyEvent extends ApplicationEvent {

    public Stage stage;

    public ApplicationReadyEvent(Object source, Stage stage) {
        super(source);
        this.stage = stage;
    }

}
