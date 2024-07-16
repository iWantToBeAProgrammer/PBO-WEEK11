import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import com.mysql.cj.x.protobuf.MysqlxDatatypes.Array;

public class Koneksi {
    static final String DB_URL = "jdbc:mysql://localhost:3306/game";
    static final String USER = "root";
    static final String PASS = "";
    static final String DRIVER_CLASS_NAME = "com.mysql.cj.jdbc.Driver";

    public static Connection getKoneksi() {
        Connection connection = null;
        try {
            Class.forName(DRIVER_CLASS_NAME);
            connection = DriverManager.getConnection(DB_URL, USER, PASS);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static boolean insertRow(String name, java.sql.Timestamp datetime, int score) {
        String query = "INSERT INTO user (nama, tgl_main, skor) VALUES (?, ?, ?)";
        try (Connection connection = getKoneksi();
                PreparedStatement preparedStatement = connection.prepareStatement(query)) {
            preparedStatement.setString(1, name);
            preparedStatement.setTimestamp(2, datetime);
            preparedStatement.setInt(3, score);

            int rowsInserted = preparedStatement.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return false;
    }

    public static ArrayList<ScoreRecord> topScores() {
        String query = "SELECT * FROM user ORDER BY skor DESC LIMIT 10";
        ArrayList<ScoreRecord> topScores = new ArrayList<>();

        try (Connection connection = getKoneksi();
                Statement stmt = connection.createStatement();) {
            ResultSet rs = stmt.executeQuery(query);

            while (rs.next()) {
                topScores.add(new ScoreRecord(rs.getString("nama"), rs.getTimestamp("tgl_main"), rs.getInt("skor")));
            }

        } catch (

        SQLException e) {
            e.printStackTrace();
        }
        return topScores;

    }
}
