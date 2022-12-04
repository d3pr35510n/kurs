package connection;

import collections.CollectionsUser;
import controller.*;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import model.User;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class FXMLLoad {
    private static FXMLLoad instance;
    private static ReentrantLock lock = new ReentrantLock();
    private static AtomicBoolean atomicBoolean = new AtomicBoolean(false);
    CollectionsUser collectionsUser = new CollectionsUser();

    private FXMLLoad() {
    }

    public static FXMLLoad getInstance() {
        lock.lock();
        if (!atomicBoolean.get()) {
            instance = new FXMLLoad();
            atomicBoolean.set(true);
        }
        lock.unlock();
        return instance;
    }


    public void open(String url) {
        FXMLLoader load = new FXMLLoader();
        load.setLocation(getClass().getResource(url));
        try {
            load.load();
        } catch (IOException e) {
            e.printStackTrace();
        }

        Parent root = load.getRoot();
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        stage.show();
    }

    public void openWithParamUser(String url, User user) {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource(url));
            Parent root = load.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            UserController userController = (UserController) load.getController();
            userController.setInformation(user);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openWithPayslip(String url, User user) {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource(url));
            Parent root = load.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            PayslipController payslipController = (PayslipController) load.getController();
            payslipController.setInformation(user);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openWithParamAdmin(String url) {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource(url));
            Parent root = load.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            AdminController adminController = (AdminController) load.getController();
            collectionsUser.fillData();
            adminController.tableUserView.setItems(collectionsUser.getUsersList());
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openWithProfile(String url, User user) {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource(url));
            Parent root = load.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            FIOController fioController = (FIOController) load.getController();
            fioController.setUser(user);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void openWithInfo(String url, User user) {
        try {
            FXMLLoader load = new FXMLLoader();
            load.setLocation(getClass().getResource(url));
            Parent root = load.load();
            Stage stage = new Stage();
            stage.setScene(new Scene(root));

            CompanyController companyController = (CompanyController) load.getController();
            companyController.setUser(user);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
