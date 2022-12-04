import config.Const;
import dao.SalaryDAO;
import dao.UserDAO;
import model.Salary;
import model.User;
import model.Profile;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

public class StartServer extends Thread {

    private ServerSocket serverSocket;
    private Socket clientSocket;
    private ArrayList<ClientThread> clientsConnected = new ArrayList<ClientThread>();
    private ResultSet result = null;

    public static void main(String[] arg) {
        StartServer server = new StartServer();
        server.start();
    }

    @Override
    public void run() {
        startServer();
    }

    private void startServer() {
        try {
            serverSocket = new ServerSocket(Const.PORT);
            System.out.println("start started in Thread - " + Thread.currentThread().getName() + "\n" +
                    "Waiting for connection....");

            while (!serverSocket.isClosed()) {
                clientSocket = serverSocket.accept();

                System.out.println("connection established...");

                ClientThread client = new ClientThread(clientSocket);
                clientsConnected.add(client);
                client.start();
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.err.println("IOException in StartServer.run()");
        }
    }

    class ClientThread extends Thread {
        Socket clientSocket;

        UserDAO userDAO = new UserDAO();
        SalaryDAO salaryDAO = new SalaryDAO();

        ObjectInputStream input = null;
        ObjectOutputStream output = null;

        ClientThread(Socket clientSocket) {
            this.clientSocket = clientSocket;
            try {
                output = new ObjectOutputStream(clientSocket.getOutputStream());
                input = new ObjectInputStream(clientSocket.getInputStream());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void run() {

            while (clientSocket.isConnected()) {
                try {
                    String clientMessage = (String) input.readObject();
                    System.out.println(clientMessage);
                    String[] message = clientMessage.split(" ", 2);
                    String commandNumberStr = message[0];
                    String command = message[1];

                    switch (commandNumberStr) {
                        case "usersTable":
                            post(userDAO.getAll());
                            break;
                        case "usersFiredTable":
                            post(userDAO.getAllFired());
                            break;
                        case "usersLogins":
                            post(userDAO.getAllLogins());
                            break;
                        case "salaryTable":
                            post(salaryDAO.getAll());
                            break;
                        case "salaryTableUser": {
                            post(salaryDAO.getAllUser(command));
                            break;
                        }
                        case "logIn": {
                            String[] values = command.split(" ", 2);
                            List<User> users = userDAO.getAll();
                            User user = null;
                            for (User temp : users) {
                                if (temp.getLogin().equals(values[0]) && temp.getPassword().equals(values[1])) {
                                    user = temp;
                                    break;
                                }
                            }
                            post(user);
                            break;
                        }

                        case "sighProfile": {
                            String[] valuesSighUp = command.split(" ", 6);
                            Profile profile = new Profile();
                            profile.setName(valuesSighUp[0]);
                            profile.setFam(valuesSighUp[2]);
                            profile.setOtch(valuesSighUp[1]);
                            profile.setPhone(Integer.valueOf(valuesSighUp[3]));
                            profile.setEmailUser(valuesSighUp[4]);
                            profile.setId_user(Integer.valueOf(valuesSighUp[5]));
                            userDAO.createProfile(profile);

                            break;
                        }

                        case "addUser":
                            String[] valuesSighUp = command.split(" ", 5);
                            User user = null;
                            Boolean test = null;

                            if (valuesSighUp[0].equals("null")) {
                                user = new User(0, valuesSighUp[1], valuesSighUp[2], valuesSighUp[3], valuesSighUp[4]);
                                test = userDAO.create(user);
                            } else {
                                user = new User(Integer.parseInt(valuesSighUp[0]), valuesSighUp[1], valuesSighUp[2], valuesSighUp[3], valuesSighUp[4]);
                                test = userDAO.update(user);
                            }

                            post(test);
                            break;
                        case "deleteFiredUser":
                             valuesSighUp = command.split(" ", 2);
                             user = null;
                             test = null;

                            if (!valuesSighUp[0].equals("null")) {
                                user = new User(Integer.parseInt(valuesSighUp[0]), valuesSighUp[1]);
                                test = userDAO.deleteFiredUser(user);
                            }

                            post(test);
                            break;

                        case "addSalary":
                            valuesSighUp = command.split(" ", 5);
                            Salary salary = null;
                            test = null;

                            if (valuesSighUp[0].equals("null")) {
                                salary = new Salary(0, valuesSighUp[1], Double.parseDouble(valuesSighUp[2]), Integer.parseInt(valuesSighUp[3]), Integer.parseInt(valuesSighUp[4]));
                                test = salaryDAO.create(salary);
                            } else {
                                salary = new Salary(0, valuesSighUp[1], Double.parseDouble(valuesSighUp[2]), Integer.parseInt(valuesSighUp[3]), Integer.parseInt(valuesSighUp[4]));
                                test = salaryDAO.create(salary);
                            }
                            post(test);
                            break;
                        case "addSalaryAward":
                            valuesSighUp = command.split(" ", 6);
                            salary = null;
                            test = null;

                            if (valuesSighUp[0].equals("null")) {
                                salary = new Salary(0, valuesSighUp[1], Double.parseDouble(valuesSighUp[2]), Integer.parseInt(valuesSighUp[3]), Integer.parseInt(valuesSighUp[4]),Double.parseDouble(valuesSighUp[5]));
                                test = salaryDAO.createAward(salary);
                            } else {
                                salary = new Salary(0, valuesSighUp[1], Double.parseDouble(valuesSighUp[2]), Integer.parseInt(valuesSighUp[3]), Integer.parseInt(valuesSighUp[4]),Double.parseDouble(valuesSighUp[5]));
                                test = salaryDAO.createAward(salary);
                            }
                            post(test);
                            break;

                        case "deleteUser": {
                            post(userDAO.delete(Integer.parseInt(command)));
                            break;
                        }
                        case "deleteFired": {
                            post(userDAO.deleteAll());
                            post(userDAO.delete(Integer.parseInt(command)));
                            break;
                        }
                        case "addFired": {
                            valuesSighUp = command.split(" ", 1);
                            user = null;
                            test = null;
                                user = new User(Integer.parseInt(valuesSighUp[0]));
                                test = userDAO.createFired(user);
                            post(test);
                            break;
                        }

                        case "allSalaryUser": {
                            post(salaryDAO.getAllByUserId(command));
                            break;
                        }

                    }
                } catch (IOException | ClassNotFoundException e) {
                    try {
                        clientSocket.close();
                    } catch (IOException ex) {
                        ex.printStackTrace();
                    }
                }
            }
        }

        void post(Object obj) {
            try {
                output.writeObject(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        Object get() {
            try {
                return input.readObject();
            } catch (IOException | ClassNotFoundException e) {
                e.printStackTrace();
            }
            return null;
        }
    }
}