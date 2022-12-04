package controller;

import config.DialogManager;
import connection.Connection;
import connection.FXMLLoad;
import javafx.event.ActionEvent;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import model.User;


public class FIOController {
    public TextField name;
    public TextField otch;
    public TextField fam;
    public TextField phone;
    public TextField emailUser;
    public Button cardAdd;
    private User user;

    public void setUser(User user){
        this.user = user;
    }

    public void CardRegister(ActionEvent actionEvent) {
        if (!name.getText().equals("") && !otch.getText().equals("") && !fam.getText().equals("") && !phone.getText().equals("") && !emailUser.getText().equals("")) {
            Connection.getInstance().post("sighProfile "  + name.getText().trim() + " " + otch.getText().trim() + " " + fam.getText().trim() + " " + phone.getText().trim()+" " + emailUser.getText().trim()+" " + user.getId().toString());
            name.getScene().getWindow().hide();
            FXMLLoad.getInstance().openWithParamUser("/view/client.fxml", user);

        } else DialogManager.showErrorDialog("Заполните все поля!");
    }

    public void ActionBack(ActionEvent actionEvent) {
        name.getScene().getWindow().hide();
        FXMLLoad.getInstance().openWithParamUser("/view/client.fxml", user);
    }

}
