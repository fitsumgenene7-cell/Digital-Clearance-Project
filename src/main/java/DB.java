import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DB {

    private static final String DB_URL = "jdbc:sqlite:clearance.db";
    private static Connection connection;

    private DB() {}

    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.sqlite.JDBC"); // explicit driver load
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        if (connection == null) {
            connection = DriverManager.getConnection(DB_URL);
        }
        return connection;
    }

    public static void initializeDatabase() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {
            // users table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS users (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_id TEXT UNIQUE,
                    full_name TEXT,
                    username TEXT,
                    password TEXT,
                    role TEXT,
                    office TEXT
                );
            """);

            // offices table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS offices (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    name TEXT UNIQUE
                );
            """);

            // penalties table
            stmt.execute("""
                CREATE TABLE IF NOT EXISTS penalties (
                    id INTEGER PRIMARY KEY AUTOINCREMENT,
                    student_id TEXT,
                    office TEXT,
                    status TEXT,
                    reason TEXT,
                    admin_id INTEGER,
                    FOREIGN KEY(student_id) REFERENCES users(student_id),
                    FOREIGN KEY(admin_id) REFERENCES users(id)
                );
            """);

            System.out.println("Database initialized successfully!");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void closeConnection() {
        try {
            if (connection != null) connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}