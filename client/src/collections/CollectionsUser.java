package collections;
import connection.Connection;
import connection.ServerMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.User;

import java.util.List;
public class CollectionsUser {
    private ObservableList<User> usersList = FXCollections.observableArrayList();
    private ObservableList<String> usersLogins = FXCollections.observableArrayList();

    public ObservableList<User> getUsersList() {
        return usersList;
    }

    public ObservableList<String> getUsersLogins() {
        Connection.getInstance().post("usersLogins ");
        usersLogins.clear();
        List<String> users = (List<String>)ServerMessage.get();
        usersLogins.addAll(users);
        return usersLogins;
    }

    public void fillData() {
        Connection.getInstance().post("usersTable ");
        usersList.clear();
        List<User> users = (List<User>)ServerMessage.get();
        usersList.addAll(users);

    }
    public void fillDataFired() {
        Connection.getInstance().post("usersFiredTable ");
        usersList.clear();
        List<User> users = (List<User>)ServerMessage.get();
        usersList.addAll(users);

    }

}
