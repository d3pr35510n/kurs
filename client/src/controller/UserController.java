package controller;

import config.DialogManager;
import connection.Connection;
import connection.FXMLLoad;
import connection.ServerMessage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Salary;
import model.User;

import java.util.List;

public class UserController {
    public TextField account;
    public Label accountValue;
    public Label helloLabel;
    public TableView<Salary> tableSalary;
    public TableColumn<Salary, Integer> taxColumn;
    public TableColumn<Salary, Integer> monthColumn;
    public TableColumn<Salary, Double> salaryColumn;
    public TableColumn<Salary, Double> totalColumn;
    public TextArea textArea;
    public Button btnFired;

    private Salary salary;
    private User user;

    @FXML
    public void initialize(){
    }

    public void ActionLogout(ActionEvent actionEvent) {
        helloLabel.getScene().getWindow().hide();
        FXMLLoad.getInstance().open("/view/login.fxml");
    }


    public void ActionButtonPressed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (!(source instanceof Button)) {
            return;
        }
        Button clickedButton = (Button) source;

        switch (clickedButton.getId()) {
            case "btnFired":
                    try {
                        Connection.getInstance().post("addFired " + user.getId() );
                        if ((Boolean) ServerMessage.get()) {
                            DialogManager.showInfoDialog("Отправлено заявление на увольнение!");
                            initialize();
                        }
                    } catch (Exception e) {
                        DialogManager.showErrorDialog("Проверьте правильность введенных данных!");
                    }
        }
    }

    public void setInformation(User user){
        salaryColumn.setCellValueFactory(new PropertyValueFactory<Salary, Double>("salary"));
        monthColumn.setCellValueFactory(new PropertyValueFactory<Salary, Integer>("month"));
        taxColumn.setCellValueFactory(new PropertyValueFactory<Salary, Integer>("tax"));
        totalColumn.setCellValueFactory(new PropertyValueFactory<Salary, Double>("total"));

        Connection.getInstance().post("allSalaryUser " + user.getLogin() );

        tableSalary.setItems(FXCollections.observableArrayList((List<Salary>) ServerMessage.get()));
        tableSalary.refresh();

        this.user = user;
    }

    public void ActionCard(ActionEvent actionEvent) {
        helloLabel.getScene().getWindow().hide();
        FXMLLoad.getInstance().openWithProfile("/view/profile.fxml", user);
    }
    public void CompanyInfo(ActionEvent actionEvent) {
        helloLabel.getScene().getWindow().hide();
        FXMLLoad.getInstance().openWithInfo("/view/companyinfo.fxml",user);
    }

    public void ActionAward(ActionEvent actionEvent) {
        helloLabel.getScene().getWindow().hide();
        FXMLLoad.getInstance().openWithPayslip("/view/payslip.fxml",user);
    }

}
