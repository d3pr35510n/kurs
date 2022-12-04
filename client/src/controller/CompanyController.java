package controller;

import connection.FXMLLoad;
import javafx.event.ActionEvent;
import javafx.scene.control.Label;
import model.User;

public class CompanyController {
    public Label hellolabel1;
    private User user;

    public void setUser(User user){
        this.user = user;
    }

    public void ActionBack(ActionEvent actionEvent) {
        hellolabel1.getScene().getWindow().hide();
        FXMLLoad.getInstance().openWithParamUser("/view/client.fxml", user);
    }
}
