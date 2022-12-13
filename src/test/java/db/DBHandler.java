package db;

import org.sqlite.JDBC;

import java.io.*;
import java.sql.*;
import java.util.concurrent.TimeUnit;

/**
 * Главное не забыть потом нормально сделать
 */
public class DBHandler {

    private static final String root = System.getProperty("user.dir");
    private static final String dbName = "QADB.db";
    private static final String dbDumpName = "QADB.sql";
    private static final String DB = "jdbc:sqlite:" + root + "/target/" + dbName;
    private static DBHandler instance = null;
    private Connection connection;

    public static synchronized DBHandler getInstance() {
        if (instance == null) {
            try {
                instance = new DBHandler();
            } catch (InterruptedException | IOException | SQLException e) {
                StringWriter sw = new StringWriter();
                PrintWriter pw = new PrintWriter(sw);
                e.printStackTrace(pw);
            }
        }
        return instance;
    }

    private DBHandler() throws SQLException, IOException, InterruptedException {
        if (!new File(root + "/target/" + dbName).isFile()) importSqlFileToDB();
        DriverManager.registerDriver(new JDBC());
        this.connection = DriverManager.getConnection(DB);
    }

    public void importSqlFileToDB() throws IOException, InterruptedException {
        Process p = null;
        OsCheck.OSType osType = OsCheck.getOperatingSystemType();
        String query = "sqlite3 target/" + dbName + " < " + dbDumpName;

        switch (osType) {
            case Windows:
                p = Runtime.getRuntime().exec("cmd /c start /b " + query);
                break;
            case Other:
                p = Runtime.getRuntime().exec(new String[]{"bash", "-c", query});
                break;
        }
        if (!p.waitFor(5, TimeUnit.SECONDS)) p.destroy();
    }

    public String getPhoneByName(String Name) {
        String phone = null;
        try (PreparedStatement statement = this.connection.prepareStatement
                ("SELECT phone FROM Users WHERE name = ?")) {
            statement.setObject(1, Name);
            ResultSet resultSet = statement.executeQuery();
            phone = resultSet.getString("phone");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phone;
    }

    public String getAbsIdByName(String Name) {
        String phone = null;
        try (PreparedStatement statement = this.connection.prepareStatement
                ("SELECT absId FROM Users WHERE name = ?")) {
            statement.setObject(1, Name);
            ResultSet resultSet = statement.executeQuery();
            phone = resultSet.getString("absId");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return phone;
    }

    public void updateAbsIdByName(String Name, String AbsId) {
        try (PreparedStatement statement = this.connection.prepareStatement(
                "UPDATE Users SET absId = ? where name = ?")) {
            statement.setObject(1, AbsId);
            statement.setObject(2, Name);
            statement.execute();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
