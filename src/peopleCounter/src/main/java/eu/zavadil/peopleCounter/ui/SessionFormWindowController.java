package eu.zavadil.peopleCounter.ui;

import eu.zavadil.peopleCounter.core.Logger;
import eu.zavadil.peopleCounter.core.MessageBuilder;
import eu.zavadil.peopleCounter.persistence.dao.PictureDao;
import eu.zavadil.peopleCounter.persistence.dao.SessionDao;
import eu.zavadil.peopleCounter.persistence.model.Picture;
import eu.zavadil.peopleCounter.persistence.model.Session;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.DirectoryChooser;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.validation.ConstraintViolation;
import javax.validation.Validator;
import java.io.File;
import java.util.Set;

@Component
public class SessionFormWindowController extends WindowControllerBase {

    @Autowired
    MainWindowController mainWindowController;

    @Autowired
    Validator validator;

    @Getter
    @Setter
    private Session session;

    @Autowired
    SessionDao sessionDao;

    @Autowired
    PictureDao pictureDao;

    @Autowired
    Logger logger;

    @Override
    public String getTitle() {
        return session.getId() == null ? "New session" : session.getName();
    }

    @Override
    public String getFXMLPath() {
        return "/ui/SessionForm.fxml";
    }

    @FXML
    TextField nameTextField;

    @FXML
    TextField pathTextField;

    @FXML
    Label validationLabel;

    @FXML
    protected void initialize() {

    }

    @Override
    public void onShow() throws Exception {
        nameTextField.setText(session.getName());
        pathTextField.setText(session.getPath());
    }

    @FXML
    protected void save() {
        session.setName(nameTextField.getText());
        session.setPath(pathTextField.getText());

        Set<ConstraintViolation<Session>> result = validator.validate(session);
        if (result.isEmpty()) {
            sessionDao.save(session);

            File folder = new File(session.getPath());
            if (folder.exists()) {
                File[] files = folder.listFiles();

                for (File file : files) {
                    if (file.isFile()) {
                        Picture picture = new Picture();
                        picture.setPath(file.getPath());
                        picture.setName(file.getName());
                        picture.setSession(session);
                        pictureDao.save(picture);
                    } else if (file.isDirectory()) {
                        // TO DO: process subdirectories?
                        //System.out.println("Directory " + file.getName());
                    }
                }
            }

            logger.log("Session %s saved.", session.getName());
            mainWindowController.openSession(session);
            this.hide();
        } else {
            MessageBuilder builder = new MessageBuilder();
            result.forEach((violation) ->
            {
                builder.appendLine("%s: %s", violation.getPropertyPath(), violation.getMessage());
            });
            validationLabel.setText(builder.toString());
        }

    }

    @FXML
    protected void cancel() {
        this.hide();
    }

    @FXML
    protected void selectPath() {
        DirectoryChooser chooser = new DirectoryChooser();
        chooser.setInitialDirectory(new File(session.getPath()));
        chooser.setTitle("Select directory");
        File file = chooser.showDialog(this.stage);
        pathTextField.setText(file.getPath());
    }

}
