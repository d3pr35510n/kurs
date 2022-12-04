package controller;

import collections.CollectionsSalary;
import connection.Connection;
import connection.FXMLLoad;
import connection.ServerMessage;
import javafx.collections.FXCollections;
import javafx.event.ActionEvent;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import model.Salary;
import model.User;
import java.util.List;

public class PayslipController {
    public Label helloLabel;
    public TableView<Salary> salaryTableView;
    public TableColumn<Salary, Double> columnSalary;
    public TableColumn<Salary, Integer> columnMonth;
    public TableColumn<Salary, Integer> columnTax;
    public TableColumn<Salary, Double> columnTotal;
    public TableColumn<Salary, Double> columnAward;

    private CollectionsSalary collectionsSalary = new CollectionsSalary();
    private User user;

    public void setUser(User user){
        this.user = user;
    }
    public void setInformation(User user){
        columnSalary.setCellValueFactory(new PropertyValueFactory<Salary, Double>("salary"));
        columnMonth.setCellValueFactory(new PropertyValueFactory<Salary, Integer>("month"));
        columnTax.setCellValueFactory(new PropertyValueFactory<Salary, Integer>("tax"));
        columnAward.setCellValueFactory(new PropertyValueFactory<Salary, Double>("award"));
        columnTotal.setCellValueFactory(new PropertyValueFactory<Salary, Double>("total"));


        Connection.getInstance().post("salaryTableUser " + user.getLogin() );

        salaryTableView.setItems(FXCollections.observableArrayList((List<Salary>) ServerMessage.get()));
        salaryTableView.refresh();

        this.user = user;
    }
    public void ActionBack(ActionEvent actionEvent) {
        helloLabel.getScene().getWindow().hide();
        FXMLLoad.getInstance().openWithParamUser("/view/client.fxml", user);
    }

}
