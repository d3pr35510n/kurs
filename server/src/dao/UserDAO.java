package dao;

import model.Profile;
import model.User;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

public class UserDAO extends EntityDAO<User, Integer> {

    private static final String SQL_UPDATE_USER_BY_ID =
            "UPDATE users SET roleID = ?, login = ?, password = ?, name = ?, account = ? WHERE id = ?";

    private static final String SQL_DELETE_FIREDUSER_BY_ID =
            "DELETE FROM fired_users WHERE user_id = ? AND login_user = ?";

    private static final String SQL_GET_USER_BY_ID =
            "SELECT users.id, users.name, users.login, users.password, role.name FROM users JOIN role ON users.roleID = role.id WHERE users.id = ?";

    private static final String SQL_CREATE_USER =
            "INSERT INTO users (roleID, login, password, name) VALUES (?,?,?,?)";

    private static final String SQL_CREATE_FIRED =
            "INSERT INTO fired_users (user_id) VALUES (?)";

    private static final String SQL_DELETE_USER_BY_ID =
            "DELETE FROM users WHERE id = ?";

    private static final String SQL_DELETE_ALL =
            "DELETE FROM fired_users ";

    private static final String SQL_DELETE_FIRED =
            "DELETE FROM fired_users WHERE user_id = ?";

    private static final String SQL_GET_ALL =
            "SELECT users.id, users.name, users.login, users.password, role.name FROM users JOIN role ON users.roleID = role.id;";

    private static final String SQL_GET_ALL_FIRED =
            "SELECT fired_users.id, fired_users.login_user FROM fired_users;";

    private static final String SQL_GET_ALL_LOGINS =
            "SELECT login FROM users ";

    private static final String SQL_CREATE_PROFILE =
            "INSERT INTO profile ( name ,fam , otch ,phone ,emailUser ,id_user) VALUES(?,?,?,?,?,?) ON DUPLICATE KEY UPDATE name= VALUES(name),fam =VALUES(fam), otch =VALUES(otch),phone =VALUES(phone),emailUser =VALUES(emailUser) ";

    @Override
    public List<User> getAll() {
        List<User> list = new LinkedList<>();
        PreparedStatement ps = getPrepareStatement(SQL_GET_ALL);
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setLogin(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setRole(rs.getString("role.name"));

                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePrepareStatement(ps);
        }

        return list;
    }
    public List<User> getAllFired() {
        List<User> list = new LinkedList<>();
        PreparedStatement ps = getPrepareStatement(SQL_GET_ALL_FIRED);
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                User user = new User();
                user.setId(rs.getInt(1));
                user.setLogin(rs.getString(2));
                list.add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePrepareStatement(ps);
        }

        return list;
    }

    public List<String> getAllLogins() {
        List<String> list = new LinkedList<>();
        PreparedStatement ps = getPrepareStatement(SQL_GET_ALL_LOGINS);
        try {
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String logins;
                logins = rs.getString(1);
                list.add(logins);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePrepareStatement(ps);
        }

        return list;
    }

    @Override
    public User getEntityById(Integer id) {
        User user = new User();

        PreparedStatement ps = getPrepareStatement(SQL_GET_USER_BY_ID);

        try {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                user = new User();
                user.setId(rs.getInt(1));
                user.setName(rs.getString(2));
                user.setLogin(rs.getString(3));
                user.setPassword(rs.getString(4));
                user.setRole(rs.getString("role.name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            closePrepareStatement(ps);
        }

        return user;
    }

    @Override
    public boolean delete(Integer id) {
        PreparedStatement st = getPrepareStatement(SQL_DELETE_USER_BY_ID);

        boolean isRemoved = false;

        try {
            st.setInt(1, id);

            deleteFired(id);

            int i = st.executeUpdate();
            isRemoved = i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isRemoved;
    }

    public boolean deleteAll() {
        PreparedStatement st = getPrepareStatement(SQL_DELETE_ALL);

        boolean isRemoved = false;

        try {
            int i = st.executeUpdate();
            isRemoved = i > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return isRemoved;
    }

    @Override
    public boolean update(User user) {
        PreparedStatement ps = getPrepareStatement(SQL_UPDATE_USER_BY_ID);
        try {
            choseRole(user, ps);
            ps.setInt(6, user.getId());

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }
    public boolean deleteFiredUser(User user) {
        PreparedStatement ps = getPrepareStatement(SQL_DELETE_FIREDUSER_BY_ID);
        try {
            ps.setInt(1,user.getId());
            ps.setString(2,user.getLogin());
            ps.executeUpdate();
            delete(user.getId());
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return true;
    }

    private void choseRole(User user, PreparedStatement ps) throws SQLException {
        int role = 0;
        switch (user.getRole()){
            case "user":
                role = 2;
                break;
            case "admin":
                role = 1;
                break;
            case "accountant":
                role = 3;
                break;
        }
        ps.setInt(1, role);
        ps.setString(2, user.getLogin());
        ps.setString(3, user.getPassword());
        ps.setString(4, user.getName());
    }

    @Override
    public boolean create(User user) {
        PreparedStatement ps = getPrepareStatement(SQL_CREATE_USER);
        try {
            choseRole(user, ps);

            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }
    public boolean createFired(User user) {
        PreparedStatement ps = getPrepareStatement(SQL_CREATE_FIRED);
        try {
            ps.setInt(1,user.getId());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    public boolean createProfile(Profile profile) {
        PreparedStatement ps = getPrepareStatement(SQL_CREATE_PROFILE);
        try {
            ps.setString(1, profile.getName());
            ps.setString(2, profile.getFam());
            ps.setString(3, profile.getOtch());
            ps.setInt(4, profile.getPhone());
            ps.setString(5, profile.getEmailUser());
            ps.setInt(6, profile.getId_user());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    private void deleteFired(Integer id) {
        PreparedStatement st = getPrepareStatement(SQL_DELETE_FIRED);

        try {
            st.setInt(1, id);
            int i = st.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
