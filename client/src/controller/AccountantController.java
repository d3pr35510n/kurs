package controller;

import collections.CollectionsSalary;
import collections.CollectionsUser;
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

public class AccountantController {
    public TableView<Salary> salaryTableView;
    public TableColumn<Salary, String> columnLogin;
    public TableColumn<Salary, Double> columnSalary;
    public TableColumn<Salary, Integer> columnMonth;
    public TableColumn<Salary, Integer> columnTax;
    public TableColumn<Salary, Double> columnTotal;
    public TableColumn<Salary, Double> columnAward;
    public ChoiceBox<String> loginChoose;
    public TextField login;
    public TextField month;
    public TextField tax;
    public TextField salary;
    public CheckBox award;
    public Button btnAdd;
    public Button loginButton;

    private CollectionsSalary collectionsSalary = new CollectionsSalary();
    private CollectionsUser collectionsUser = new CollectionsUser();
    private Salary selectedSalary;

    @FXML
    public void initialize() {
        loginChoose.setItems(FXCollections.observableArrayList(collectionsUser.getUsersLogins()));
        columnLogin.setCellValueFactory(new PropertyValueFactory<Salary,String>("login"));
        columnMonth.setCellValueFactory(new PropertyValueFactory<Salary, Integer>("month"));
        columnSalary.setCellValueFactory(new PropertyValueFactory<Salary, Double>("salary"));
        columnTax.setCellValueFactory(new PropertyValueFactory<Salary, Integer>("tax"));
        columnTotal.setCellValueFactory(new PropertyValueFactory<Salary, Double>("total"));
        columnAward.setCellValueFactory(new PropertyValueFactory<Salary, Double>("award"));

        collectionsSalary.fillData();
        salaryTableView.setItems(collectionsSalary.getSalaryList());
        salaryTableView.refresh();
    }

    public void ActionLogout(ActionEvent actionEvent) {
        btnAdd.getScene().getWindow().hide();
        FXMLLoad.getInstance().open("/view/login.fxml");
    }
    public void ActionAward(ActionEvent actionEvent) {
        DialogManager.showInfoDialog("Рассчетные листы отправлены успешно!");
    }

    public void ActionButtonPressed(ActionEvent actionEvent) {
        Object source = actionEvent.getSource();

        if (!(source instanceof Button)) {
            return;
        }
        Button clickedButton = (Button) source;

        switch (clickedButton.getId()) {
            case "btnAdd":
                if (loginChoose.getValue()!= null
                        && !salary.getText().equals("")
                        && !month.getText().equals("")
                        && !tax.getText().equals("")) {
                    try {
                        if (selectedSalary == null) {
                            selectedSalary = new Salary();
                        }
                        selectedSalary.setLogin(loginChoose.getSelectionModel().getSelectedItem());
                        selectedSalary.setSalary(Double.parseDouble(salary.getText()));
                        selectedSalary.setMonth(Integer.parseInt(month.getText()));
                        selectedSalary.setTax(Integer.parseInt(tax.getText()));
                        if(award.isSelected()) {
                            selectedSalary.setAward(Double.parseDouble(salary.getText())*0.2);
                            Connection.getInstance().post("addSalaryAward " + selectedSalary.getId() + " " + selectedSalary.getLogin() + " " + selectedSalary.getSalary() + " " + selectedSalary.getMonth() + " " + selectedSalary.getTax() + " " + selectedSalary.getAward());
                        }else
                            Connection.getInstance().post("addSalary " + selectedSalary.getId() + " " + selectedSalary.getLogin() + " " + selectedSalary.getSalary() + " " + selectedSalary.getMonth() + " " + selectedSalary.getTax());

                        if ((Boolean) ServerMessage.get()) {
                            DialogManager.showInfoDialog("Рассчет произошел успешно!");
                            month.clear();
                            tax.clear();
                            salary.clear();
                            selectedSalary = null;
                            initialize();
                        }
                    } catch (Exception e) {
                        DialogManager.showErrorDialog("Проверьте правильность введенных данных!");
                    }

                } else {
                    DialogManager.showErrorDialog("Заполните поля!");

                }
                break;

        }
    }
}
