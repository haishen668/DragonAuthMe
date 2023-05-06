package ltd.dreamcraft.www.tools;

import ltd.dreamcraft.www.Manager.ConfigManager;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class JDBCUtil {
    private static Connection connection;
    private static String url = ConfigManager.getMySQLURL();
    private static String username = ConfigManager.getMySQLuserName();
    private static String password = ConfigManager.getMySQLpassWord();
    private static String database = ConfigManager.getMySQLdataBase();

    public static Connection getConnection() {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            String[] urls = url.split(":");
            String ip = "jdbc:mysql://" + urls[0] + ":" + urls[1] + "/" + database;
            return connection = DriverManager.getConnection(ip, username, password);
        } catch (Exception var2) {
            var2.printStackTrace();
            return null;
        }
    }

    public static void close(ResultSet rs, Statement stmt, Connection connection, PreparedStatement statement) {
        try {
            if (rs != null) {
                rs.close();
            }
        } catch (SQLException var8) {
            var8.printStackTrace();
        }

        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException var7) {
            var7.printStackTrace();
        }

        try {
            if (connection != null) {
                connection.close();
            }
        } catch (SQLException var6) {
            var6.printStackTrace();
        }

        try {
            if (statement != null) {
                statement.close();
            }
        } catch (SQLException var5) {
            var5.printStackTrace();
        }

    }
}
