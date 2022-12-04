package controller;

import config.DialogManager;
import connection.Connection;
import connection.FXMLLoad;
import connection.ServerMessage;
import javafx.event.ActionEvent;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import model.User;

public class LoginController {
    public TextField emailField;
    public PasswordField passwordField;

    public void ActionLogin(ActionEvent actionEvent) {
        if (!emailField.getText().equals("") && !passwordField.getText().equals("")) {
            Connection.getInstance().post("logIn " + emailField.getText().trim() + " " + passwordField.getText().trim());
            User user = (User) ServerMessage.get();
            if(user == null){
                emailField.clear();
                passwordField.clear();
                DialogManager.showErrorDialog( "Такого пользователя нет!");
            } else{
                emailField.getScene().getWindow().hide();

                switch (user.getRole()){
                    case "user":
                        FXMLLoad.getInstance().openWithParamUser("/view/client.fxml", user);
                        break;
                    case "admin":
                        FXMLLoad.getInstance().open("/view/admin.fxml");
                        break;
                    case "accountant":
                        FXMLLoad.getInstance().open("/view/accountant.fxml");
                        break;
                }
            }
        } else DialogManager.showErrorDialog( "Заполните все поля!");
    }

}
