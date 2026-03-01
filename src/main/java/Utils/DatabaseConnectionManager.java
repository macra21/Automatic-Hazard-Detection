package Utils;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {
    private static final String URL;
    private static final String USER;
    private static final String PASSWORD;

    static{
        Properties properties = new Properties();
        try (InputStream input = DatabaseConnectionManager.class.getClassLoader().getResourceAsStream("config.properties")){
            properties.load(input);
            URL = properties.getProperty("db.url");
            USER = properties.getProperty("db.user");
            PASSWORD = properties.getProperty("db.pass");
        } catch (Exception e){
            throw new RuntimeException("Failed to load config.properties:" + e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
