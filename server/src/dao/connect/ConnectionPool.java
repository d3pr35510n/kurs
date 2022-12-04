package dao.connect;

import java.sql.Connection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

public class ConnectionPool extends BaseConnection {

    private static final ReentrantLock LOCK = new ReentrantLock();
    private static AtomicBoolean isInitialized = new AtomicBoolean(false);
    private static ConnectionPool instance;

    private ConnectionPool() {
        super(
                "com.mysql.cj.jdbc.Driver",
                "root",
                "1111",
                "jdbc:mysql://127.0.0.1:3306/system_account?useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC");
    }
    public static ConnectionPool getInstance() {
        if (!isInitialized.get()) {
            LOCK.lock();
            try {
                if (!isInitialized.get()) {
                    instance = new ConnectionPool();
                    isInitialized.set(true);
                }
            } finally {
                LOCK.unlock();
            }
        }
        return instance;
    }
    public Connection getConnection() {
        return connect();
    }
}
