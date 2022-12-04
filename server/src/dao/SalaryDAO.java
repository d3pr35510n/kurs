package dao;

import model.Salary;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class SalaryDAO extends EntityDAO<Salary, Integer> {

    private UserDAO userDAO = new UserDAO();

    private static final String SQL_GET_ALL =
            "SELECT * FROM salarytable;";

    private static final String SQL_GET_ALL_USER =
            "SELECT salarytable.salary, salarytable.month,salarytable.tax,salarytable.award,salarytable.total FROM salarytable WHERE salarytable.login = ? ";

    private static final String SQL_CREATE_SALARY =
            "INSERT INTO salarytable (login, month, salary, tax,total)  VALUES (?,?,?,?,?)";

    private static final String SQL_UPDATE_SALARY =
            "UPDATE  salarytable SET salary = ? , tax= ?, total = ? WHERE login = ? AND month = ? ";

    private static final String SQL_CHECK_SALARY =
            "SELECT salarytable.id, salarytable.login, salarytable.salary, salarytable.month,  salarytable.tax, salarytable.total, salarytable.award FROM salarytable WHERE login = ? AND month = ? ";

    private static final String SQL_CREATE_SALARY_AWARD =
            "INSERT INTO salarytable (login, month, salary, tax,total,award)  VALUES (?,?,?,?,?,?)";

    private static final String SQL_UPDATE_SALARY_AWARD =
            "UPDATE  salarytable SET salary = ? , tax= ?, total = ?,award = ? WHERE login = ? AND month = ? ";


    private static final String SQL_GET_ALL_BY_USER_ID =
            "SELECT  salarytable.id, salarytable.login, salarytable.salary, salarytable.month,  salarytable.tax, salarytable.total  FROM salarytable JOIN users ON salarytable.login = users.login WHERE users.login = ?;";

    @Override
    public List<Salary> getAll() {
        List<Salary> list = new LinkedList<>();
        PreparedStatement ps = getPrepareStatement(SQL_GET_ALL);
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Salary salary = new Salary();
                salary.setId(rs.getInt(1));
                salary.setLogin(rs.getString(2));
                salary.setMonth(rs.getInt(3));
                salary.setSalary(rs.getDouble(4));
                salary.setTax(rs.getInt(5));
                salary.setTotal(rs.getDouble(6));
                salary.setAward(rs.getDouble(7));
                list.add(salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePrepareStatement(ps);
        }

        return list;
    }

    public List<Salary> getAllUser(String login) {
        List<Salary> list = new LinkedList<>();
        PreparedStatement ps = getPrepareStatement(SQL_GET_ALL_USER);
        try {
            ps.setString(1, login);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Salary salary = new Salary();
                salary.setMonth(rs.getInt(2));
                salary.setSalary(rs.getDouble(1));
                salary.setTax(rs.getInt(3));
                salary.setTotal(rs.getDouble(5));
                salary.setAward(rs.getDouble(4));
                list.add(salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePrepareStatement(ps);
        }

        return list;
    }


    @Override
    public Salary getEntityById(Integer id) {
        return null;
    }

    @Override
    public boolean delete(Integer id) {
        return false;
    }

    private void createSalary(Salary salary, PreparedStatement ps) throws SQLException {
        ps.setString(1, salary.getLogin());
        ps.setInt(2, salary.getMonth());
        ps.setDouble(3, salary.getSalary());
        ps.setInt(4, salary.getTax());
        Double itog = salary.getSalary() - salary.getSalary() * salary.getTax() / 100;
        ps.setDouble(5, itog);

    }

    private void createSalaryAward(Salary salary, PreparedStatement ps) throws SQLException {
        ps.setString(1, salary.getLogin());
        ps.setInt(2, salary.getMonth());
        ps.setDouble(3, salary.getSalary());
        ps.setInt(4, salary.getTax());
        Double award = salary.getSalary() * 0.2;
        Double itog = salary.getSalary() - salary.getSalary() * salary.getTax() / 100 + award;
        ps.setDouble(5, itog);
        ps.setDouble(6, award);

    }

    private void updateSalary(Salary salary, PreparedStatement ps) throws SQLException {
        ps.setDouble(1, salary.getSalary());
        ps.setInt(2, salary.getTax());
        Double itog = salary.getSalary() - salary.getSalary() * salary.getTax() / 100;
        ps.setDouble(3, itog);
        ps.setString(4, salary.getLogin());
        ps.setInt(5, salary.getMonth());
    }


    private void updateSalaryAward(Salary salary, PreparedStatement ps) throws SQLException {
        ps.setDouble(1, salary.getSalary());
        ps.setInt(2, salary.getTax());
        Double award = salary.getSalary() * 0.2;
        Double itog = salary.getSalary() - salary.getSalary() * salary.getTax() / 100 + award;
        ps.setDouble(3, itog);
        ps.setDouble(4, award);
        ps.setString(5, salary.getLogin());
        ps.setInt(6, salary.getMonth());


    }

    @Override
    public boolean create(Salary entity) {
        PreparedStatement ps = getPrepareStatement(SQL_CREATE_SALARY);
        try {
            if (update(entity) && check(entity).getLogin()!=null)  {

            } else {
                createSalary(entity, ps);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    public boolean update(Salary entity) {
        PreparedStatement ps = getPrepareStatement(SQL_UPDATE_SALARY);
        try {
            updateSalary(entity, ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }


    public boolean createAward(Salary entity) {
        PreparedStatement ps = getPrepareStatement(SQL_CREATE_SALARY_AWARD);
        try {
            if (updateAward(entity) && check(entity).getLogin()!=null)  {

            } else {
                createSalaryAward(entity, ps);
                ps.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean updateAward(Salary entity) {
        PreparedStatement ps = getPrepareStatement(SQL_UPDATE_SALARY_AWARD);
        try {
            updateSalaryAward(entity, ps);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public Salary check(Salary entity) {
        Salary salary = new Salary();
        PreparedStatement ps = getPrepareStatement(SQL_CHECK_SALARY);
        try {

            ps.setString(1, (entity.getLogin()));
            ps.setInt(2, (entity.getMonth()));
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                salary.setId(rs.getInt(1));
                salary.setLogin(rs.getString(2));
                salary.setMonth(rs.getInt(4));
                salary.setSalary(rs.getDouble(3));
                salary.setTax(rs.getInt(5));
                salary.setTotal(rs.getDouble(6));
                salary.setAward(rs.getDouble(7));

            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePrepareStatement(ps);
        }

        return salary;
    }

    public List<Salary> getAllByUserId(String command) {
        List<Salary> listSalary = new ArrayList<>();

        PreparedStatement ps = getPrepareStatement(SQL_GET_ALL_BY_USER_ID);
        try {
            ps.setString(1, (command));

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                Salary salary = new Salary();

                salary.setId(rs.getInt(1));
                salary.setLogin(rs.getString(2));
                salary.setMonth(rs.getInt(4));
                salary.setSalary(rs.getDouble(3));
                salary.setTax(rs.getInt(5));
                salary.setTotal(rs.getDouble(6));
                listSalary.add(salary);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePrepareStatement(ps);
        }

        return listSalary;
    }

}
