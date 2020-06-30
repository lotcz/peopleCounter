package eu.zavadil.peopleCounter.ui;

import eu.zavadil.peopleCounter.core.Logger;
import eu.zavadil.peopleCounter.core.MessageBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class ApplicationReadyListener implements ApplicationListener<ApplicationReadyEvent> {

    @Autowired
    Logger logger;

    @Autowired
    MainWindowController mainWindowController;

    @Override
    public void onApplicationEvent(ApplicationReadyEvent applicationReadyEvent) {
        try {
            mainWindowController.show(applicationReadyEvent.stage);
        } catch (Exception e) {
            MessageBuilder builder = MessageBuilder.createBuilderFromException(e);
            builder.prepend("Exception occurred when creating main window:");
            logger.log(builder.toString());
        }
    }

}
