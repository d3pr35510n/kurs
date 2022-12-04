package controller;

import collections.CollectionsUser;
import config.DialogManager;
import connection.Connection;
import connection.FXMLLoad;
import connection.ServerMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import model.User;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;

public class AdminController {
    public TableView<User> tableUserView;
    public TableColumn<User, String> columnName;
    public TableColumn<User, String> columnRole;
    public TableColumn<User, String> columnEmail;
    public ChoiceBox<String> role;
    public TextField name;
    public TextField email;
    public TextField password;
    public TextField account;
    public Button btnAdd;
    public Button btnFired;
    public Button btnDelete;
    public Button loginButton;

    private CollectionsUser collectionsUser = new CollectionsUser();
    private User selectedUser;

    @FXML
    public void initialize() {

        tableUserView.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.isPrimaryButtonDown()) {
                    selectedUser = tableUserView.getSelectionModel().getSelectedItem();

                    if (event.getClickCount() == 2) {
                        name.setText(selectedUser.getName());
                        email.setText(selectedUser.getLogin());
                        password.setText(selectedUser.getPassword());
                        role.setValue(selectedUser.getRole());
                    }
                }
            }
        });

        role.setItems(FXCollections.observableArrayList("admin", "user", "accountant"));

        columnName.setCellValueFactory(new PropertyValueFactory<User, String>("name"));
        columnRole.setCellValueFactory(new PropertyValueFactory<User, String>("role"));
        columnEmail.setCellValueFactory(new PropertyValueFactory<User, String>("login"));

        collectionsUser.fillData();

        tableUserView.setItems(collectionsUser.getUsersList());
        tableUserView.refresh();
    }


    public void ActionLogout(ActionEvent actionEvent) {
        btnAdd.getScene().getWindow().hide();
        FXMLLoad.getInstance().open("/view/login.fxml");
    }
    public void ActionFired(ActionEvent actionEvent) {
        btnAdd.getScene().getWindow().hide();
        FXMLLoad.getInstance().open("/view/firedusers.fxml");
    }

    public void ActionButtonPressed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (!(source instanceof Button)) {
            return;
        }
        Button clickedButton = (Button) source;

        switch (clickedButton.getId()) {
            case "btnAdd":
                if (!name.getText().equals("")
                        && role.getValue() != null
                        && !email.getText().equals("")
                        && !password.getText().equals("")){
                    try {
                        if (selectedUser == null) {
                            selectedUser = new User();
                        }
                        selectedUser.setName(name.getText());
                        selectedUser.setRole(role.getSelectionModel().getSelectedItem());
                        selectedUser.setLogin(email.getText());
                        selectedUser.setPassword(password.getText());

                        Connection.getInstance().post("addUser " + selectedUser.getId() + " " + selectedUser.getName() + " " + selectedUser.getRole() + " " + selectedUser.getLogin() + " " + selectedUser.getPassword());

                        if ((Boolean) ServerMessage.get()) {
                            DialogManager.showInfoDialog("Добавление произошло успешно!");
                            name.clear();
                            email.clear();
                            password.clear();
                            account.clear();
                            selectedUser = null;
                            initialize();
                        }
                    } catch (Exception e) {
                        DialogManager.showErrorDialog("Проверьте правильность введенных данных!");
                    }

                } else {
                    DialogManager.showErrorDialog("Заполните поля!");

                }
                break;
            case "btnDelete":

                if (!(selectedUser == null)) {
                    Connection.getInstance().post("deleteUser " + selectedUser.getId());
                    if ((Boolean) ServerMessage.get()) {
                        initialize();
                        DialogManager.showInfoDialog("Удалено!");
                    }
                } else DialogManager.showErrorDialog("Выберите пользователя!");
                selectedUser = null;
                break;
        }
    }

    public void ActionGraff(ActionEvent actionEvent) {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource("/view/pie_chart.fxml"));
            Parent root = load.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            PieChartController pieChartController = (PieChartController) load.getController();
            pieChartController.setInformation(tableUserView.getItems());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void ActionFile(ActionEvent actionEvent) {
        saveInformationInFile(tableUserView.getItems());
        DialogManager.showInfoDialog("Файл успешно создан!");
    }

    private void saveInformationInFile(ObservableList<User> items) {
        int count = 0;

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("InformationClient.txt", true))) {
            for (User client : items) {
                count++;
                String out = count + "id:" + client.getLogin()  + ",\nлогин: " + client.getLogin() + " \n";
                writer.append(out);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
