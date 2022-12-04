package collections;
import connection.Connection;
import connection.ServerMessage;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import model.Salary;
import model.User;

import java.util.List;
public class CollectionsSalary {
    private ObservableList<Salary> salaryList = FXCollections.observableArrayList();

    public ObservableList<Salary> getSalaryList() {
        return salaryList;
    }

    public void fillData() {
        Connection.getInstance().post("salaryTable ");
        salaryList.clear();
        List<Salary> salary = (List<Salary>)ServerMessage.get();
        salaryList.addAll(salary);
    }

}
