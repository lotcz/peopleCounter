package eu.zavadil.peopleCounter.ui;

import com.sun.javafx.collections.ObservableListWrapper;
import eu.zavadil.peopleCounter.persistence.dao.SessionDao;
import eu.zavadil.peopleCounter.persistence.model.Session;
import javafx.collections.ListChangeListener;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class OpenSessionDialogController extends WindowControllerBase {

    @Autowired
    SessionDao sessionDao;

    @Autowired
    MainWindowController mainWindowController;

    @Override
    public String getTitle() {
        return "Open session...";
    }

    @Override
    public String getFXMLPath() {
        return "/ui/OpenSessionDialog.fxml";
    }

    @FXML
    private TableView sessionsTableView;

    TableView.TableViewSelectionModel<Session> selectionModel;

    ObservableList<Session> selectedItems;

    @FXML
    private TableColumn nameColumn;

    @FXML
    private TableColumn dateColumn;

    @FXML
    private TableColumn pathColumn;

    @FXML
    private Button selectButton;

    Session session;

    private void setSession(Session session) {
        this.session = session;
        this.updateSelectButton();
    }

    private void updateSelectButton() {
        selectButton.setDisable(session == null);
    }

    private ObservableListWrapper sessions;

    @FXML
    protected void initialize() {
        nameColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
        dateColumn.setCellValueFactory(new PropertyValueFactory<>("createDate"));
        pathColumn.setCellValueFactory(new PropertyValueFactory<>("path"));
        selectionModel = sessionsTableView.getSelectionModel();
        selectionModel.setSelectionMode(SelectionMode.SINGLE);
        selectedItems = selectionModel.getSelectedItems();
        selectedItems.addListener(new ListChangeListener<Session>() {
            @Override
            public void onChanged(Change<? extends Session> change) {
                if (selectedItems.size() > 0) {
                    setSession(selectedItems.get(0));
                } else {
                    setSession(null);
                }
            }
        });
        sessionsTableView.setOnMouseClicked(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton().equals(MouseButton.PRIMARY)){
                    if(mouseEvent.getClickCount() == 2){
                        select();
                    }
                }
            }
        });
    }

    public void loadSessions() {
        sessions = new ObservableListWrapper(sessionDao.loadAll());
        sessionsTableView.setItems(sessions);
    }

    @Override
    public void onShow() throws Exception {
         session = null;
         this.loadSessions();
         this.updateSelectButton();
    }

    @FXML
    protected void select() {
        mainWindowController.openSession(session);
        this.hide();
    }

    @FXML
    protected void cancel() {
        this.hide();
    }

}
