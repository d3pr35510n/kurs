package controller;

import collections.CollectionsUser;
import config.DialogManager;
import connection.Connection;
import connection.FXMLLoad;
import connection.ServerMessage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import model.User;

public class FiredController {
    public TableView<User> tableView;
    public TableColumn<User, Integer> idColumn;
    public TableColumn<User, String> loginColumn;
    public TextField id;
    public TextField login;
    public Label userName;

    private CollectionsUser collectionsUser = new CollectionsUser();
    private User selectedUser;

    @FXML
    public void initialize() {

        idColumn.setCellValueFactory(new PropertyValueFactory<User, Integer>("id"));
        loginColumn.setCellValueFactory(new PropertyValueFactory<User, String>("login"));
        collectionsUser.fillDataFired();
        tableView.setItems(collectionsUser.getUsersList());
        tableView.refresh();

    }

    public void ActionBack(ActionEvent actionEvent) {
        userName.getScene().getWindow().hide();
        FXMLLoad.getInstance().openWithParamAdmin("/view/admin.fxml");
    }

    public void ActionFired(ActionEvent actionEvent) {
        try {
            if (selectedUser == null) {
                selectedUser = new User();
            }
            selectedUser.setId(Integer.parseInt(id.getText()));
            selectedUser.setLogin(login.getText());


            Connection.getInstance().post("deleteFiredUser " + selectedUser.getId() + " " + selectedUser.getLogin());

            if ((Boolean) ServerMessage.get()) {
                DialogManager.showInfoDialog("Сотрудник уволен!");
                selectedUser = null;
                initialize();
            }
        } catch (Exception e) {
            DialogManager.showErrorDialog("Проверьте правильность введенных данных!");
        }
        userName.getScene().getWindow().hide();
        FXMLLoad.getInstance().openWithParamAdmin("/view/admin.fxml");
    }
}
