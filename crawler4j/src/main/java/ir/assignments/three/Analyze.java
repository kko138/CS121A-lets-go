package ir.assignments.three;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Kevin on 2/1/2016.
 */
public class Analyze {
    static Connection connection;

    public static Connection getSQLConnection(String user, String pw) throws SQLException {
        return DriverManager.getConnection("jdbc:mysql:///crawldata?useSSL=false", user, pw);
    }

    public void execSql(String statement) throws SQLException{
        Statement select = this.getConnection().createStatement();

        select.execute(statement);
    }

    public Connection getConnection() {
        return this.connection;
    }

    public static void connect(String user, String password) throws SQLException {
        connection = getSQLConnection(user, password);
    }

    public static void main(String args[]) {
        ArrayList<String> all = new ArrayList<>();
        ResultSet tempResult;
        String resultString = new String();
        try {
            connect("root", "1234");
            Statement state = connection.createStatement();
            tempResult = state.executeQuery("SELECT textfile FROM data limit 100");
            while(tempResult.next()) {
                resultString = tempResult.getString("textfile");
                all.addAll(Utilities.tokenizeFile(resultString));
            }

            List<Frequency> test = WordFrequencyCounter.computeWordFrequencies(all);
            Utilities.printFrequencies(test);

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
