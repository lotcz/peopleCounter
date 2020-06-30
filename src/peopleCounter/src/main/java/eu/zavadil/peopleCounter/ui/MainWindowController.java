package eu.zavadil.peopleCounter.ui;

import com.sun.javafx.collections.ObservableListWrapper;
import eu.zavadil.peopleCounter.core.Logger;
import eu.zavadil.peopleCounter.core.MessageBuilder;
import eu.zavadil.peopleCounter.face.FaceSessionProcessor;
import eu.zavadil.peopleCounter.persistence.dao.FaceDao;
import eu.zavadil.peopleCounter.persistence.dao.PictureDao;
import eu.zavadil.peopleCounter.persistence.dao.SessionDao;
import eu.zavadil.peopleCounter.persistence.model.Face;
import eu.zavadil.peopleCounter.persistence.model.Picture;
import eu.zavadil.peopleCounter.persistence.model.Session;
import javafx.application.Platform;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.StreamSupport;

@Component
public class MainWindowController extends WindowControllerBase {

    @Override
    public String getTitle() {
        return "People Counter prototype 1.0";
    }

    @Override
    public String getFXMLPath() {
        return "/ui/Main.fxml";
    }

    @Autowired
    SessionDao sessionDao;

    @Autowired
    PictureDao pictureDao;

    @Autowired
    FaceDao faceDao;

    @Autowired
    SessionFormWindowController sessionFormWindowController;

    @Autowired
    OpenSessionDialogController openSessionDialogController;

    @Autowired
    Logger logger;

    @Autowired
    FaceSessionProcessor sessionProcessor;


    @FXML
    protected void initialize() {
        nameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        processedTableColumn.setCellValueFactory(new PropertyValueFactory<>("processed"));
        console.setItems(consoleList);

        imageView.fitHeightProperty().bind(picturePane.heightProperty());
        imageView.fitWidthProperty().bind(picturePane.widthProperty());

        TableView.TableViewSelectionModel<Picture> selectionModel = picturesTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectedPictures = selectionModel.getSelectedItems();
        selectedPictures.addListener(new ListChangeListener<Picture>() {
            @Override
            public void onChanged(Change<? extends Picture> change) {
                if (selectedPictures.size() > 0) {
                    Picture picture = selectedPictures.get(0);
                    try {
                        imageView.setImage(new Image(new FileInputStream(picture.getPath())));
                    } catch (FileNotFoundException e) {
                        logger.log(MessageBuilder.buildExceptionMessage(e));
                    }
                } else {
                    imageView.setImage(null);
                }
            }
        });

        this.updateUI();
    }

    private void updateUI() {
        this.updateSessionUI();
        this.updateCaptureUI();
        this.updateProcessingUI();
    }


    // console

    @FXML
    private ListView console;

    private ObservableListWrapper consoleList = new ObservableListWrapper(new LinkedList());

    public void writeToConsole(String str) {
        Platform.runLater(() -> consoleList.add(str));
    }

    // session

    private int uniqueFacesCount = 0;

    private int sessionPictureCount = 0;

    private int sessionFaceCount = 0;

    @FXML
    Label uniqueFacesLabel;

    @FXML
    MenuItem closeSessionMenuItem;

    @FXML
    TitledPane sessionDetailPane;

    @FXML
    Label picturesCountLabel;

    @FXML
    Label facesCountLabel;

    protected Session getActiveSession() {
        return this.sessionProcessor.getCurrentSession();
    }

    protected boolean hasActiveSession() {
        return this.sessionProcessor.getCurrentSession() != null;
    }

    @FXML
    protected void createNewSession() throws Exception {
        List<Session> sessions = sessionDao.loadAll();
        Session session = new Session();
        session.setPath("c:\\develop\\peopleCounter\\test\\pictures\\set1\\");
        session.setName(String.format("Session %d", sessions.size() + 1));
        sessionFormWindowController.setSession(session);
        sessionFormWindowController.show(Modality.APPLICATION_MODAL);
    }

    @FXML
    protected void openSession() throws Exception {
        openSessionDialogController.show(Modality.APPLICATION_MODAL);
    }

    public void openSession(Session session) {
        this.sessionProcessor.openSession(session);
        if (this.hasActiveSession()) {
            logger.log("Session %s active.", session.getName());
            sessionPictureCount = pictureDao.getSessionPictureCount(this.getActiveSession().getId());
            sessionFaceCount = faceDao.getSessionFaceCount(this.getActiveSession().getId());
            uniqueFacesCount = faceDao.getUniqeFaceCount(this.getActiveSession().getId());
        } else {
            sessionPictureCount = 0;
            sessionFaceCount = 0;
            uniqueFacesCount = 0;
        }
        this.updateUI();
    }

    @FXML
    protected void closeSession() {
        this.openSession(null);
    }

    private void updateSessionUI() {
        Platform.runLater(() -> {
            this.closeSessionMenuItem.setDisable(!this.hasActiveSession());
            this.updateSessionDetailUI();
            this.updatePicturesUI();
        });
    }

    private void updateSessionDetailUI() {
        Platform.runLater(() -> {
            if (this.hasActiveSession()) {
                sessionDetailPane.setDisable(false);
                sessionDetailPane.setText(this.getActiveSession().getName());

            } else {
                sessionDetailPane.setDisable(true);
                sessionDetailPane.setText("-- no session active --");

            }
            uniqueFacesLabel.setText(String.valueOf(uniqueFacesCount));
            picturesCountLabel.setText(String.valueOf(sessionPictureCount));
            facesCountLabel.setText(String.valueOf(sessionFaceCount));
        });
    }

    // processing

    private int processedPictureCount = 0;

    private int processedFaceCount = 0;

    @FXML
    Button startProcessingButton;

    @FXML
    Button stopProcessingButton;

    @FXML
    Button resetProcessingButton;

    @FXML
    ProgressBar picturesProgressBar;

    @FXML
    ProgressBar facesProgressBar;

    @FXML
    protected void startProcessing() {
        this.sessionProcessor.startProcessing();
        this.updateProcessingUI();
    }

    @FXML
    protected void stopProcessing() {
        this.sessionProcessor.stopProcessing();
        this.updateProcessingUI();
    }

    @FXML
    protected void resetProcessing() {
        logger.log("TO DO...");
    }

    public void onPictureProcessed(Picture picture) {
        processedPictureCount = pictureDao.getSessionProcessedPictureCount(this.getActiveSession().getId());
        sessionFaceCount = faceDao.getSessionFaceCount(this.getActiveSession().getId());
        this.updateProcessingUI();
        this.updatePicturesUI();
    }

    public void onFaceProcessed(Face face) {
        processedFaceCount = faceDao.getSessionProcessedFaceCount(this.getActiveSession().getId());
        sessionFaceCount = faceDao.getSessionFaceCount(this.getActiveSession().getId());
        uniqueFacesCount = faceDao.getUniqeFaceCount(this.getActiveSession().getId());
        this.updateProcessingUI();
        this.updateSessionDetailUI();
    }

    private void updateProcessingUI() {
        Platform.runLater(() -> {
            startProcessingButton.setDisable(!this.hasActiveSession() || this.sessionProcessor.isProcessing());
            stopProcessingButton.setDisable(!this.hasActiveSession() || !this.sessionProcessor.isProcessing());
            resetProcessingButton.setDisable(!this.hasActiveSession() || this.sessionProcessor.isProcessing());

            if (sessionPictureCount > 0) {
                picturesProgressBar.setProgress((double) processedPictureCount / sessionPictureCount);
            } else {
                picturesProgressBar.setProgress(0.0);
            }

            if (sessionFaceCount > 0) {
                facesProgressBar.setProgress((double) processedFaceCount / sessionFaceCount);
            } else {
                facesProgressBar.setProgress(0.0);
            }
        });
    }

    // pictures

    ObservableList<Picture> selectedPictures;

    @FXML
    AnchorPane picturePane;

    @FXML
    TableView picturesTableView;

    @FXML
    private TableColumn nameTableColumn;

    @FXML
    private TableColumn processedTableColumn;

    private void updatePicturesUI() {
        Platform.runLater(() -> {
            if (this.hasActiveSession()) {
                List<Picture> pictures = pictureDao.loadBySessionId(this.getActiveSession().getId());
                picturesTableView.setItems(new ObservableListWrapper(pictures));
                picturesTableView.setDisable(false);
            } else {
                picturesTableView.setDisable(true);
                picturesTableView.setItems(null);
            }
        });
    }


    // capture

    @FXML
    Button recordingButton;

    private void updateCaptureUI() {
        Platform.runLater(() -> {
            this.recordingButton.setDisable(!this.hasActiveSession());
        });
    }


    // image

    @FXML
    ImageView imageView;

    @FXML
    protected void quit() {
        this.hide();
    }

}
