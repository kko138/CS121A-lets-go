/**
 * authors: Cassie Jeansonne 18923914, Kevin Ko 56956077, Samuel Lin 52478518, Sophia Chan 33196560
 */
package ir.assignments.three;

import ir.assignments.three.helper.gt;

import java.io.File;
import java.io.FileWriter;
import java.sql.*;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Analyzes information coming from the database.
 */
public class Analyze {
    static Connection connection;

    /**
     * Opening connection to mysql database.
     * @param user
     * @param pw
     * @throws SQLException
     */
    public static Connection getSQLConnection(String user, String pw) throws SQLException {
        return DriverManager.getConnection("jdbc:mysql:///crawldb?useSSL=false", user, pw);
    }

    /**
     * Gets sql connection
     * @return connection
     */
    public Connection getConnection() {
        return this.connection;
    }

    /**
     * Calls getSQLConnection (modularization!)
     * @param user
     * @param password
     * @throws SQLException
     */
    public static void connect(String user, String password) throws SQLException {
        connection = getSQLConnection(user, password);
    }

    /**
     * Analyzes content from database
     * @param args
     */
    public static void main(String args[]) {
        ArrayList<String> all = new ArrayList<>();
        Map<String,Integer> mapOfURLs = new HashMap<>();
        ResultSet tempResult;
        String resultString = new String();
        int times;
        try {
            connect("root", "1234");
            Statement state = connection.createStatement();

            /////////////////////////////This portion answers question 3: How many subdomains did you find?//////////////
            tempResult = state.executeQuery("SELECT url FROM data");
            String regex = "\\/\\/(.*?)\\/";
            Pattern p = Pattern.compile(regex);
            Matcher m;
            while(tempResult.next())
            {
                resultString = tempResult.getString("url");
                m = p.matcher(resultString);
                if (m.find()) {
                    if (!mapOfURLs.containsKey(m.group(1)))
                        mapOfURLs.put(m.group(1), 1);
                    else
                        mapOfURLs.put(m.group(1), mapOfURLs.get(m.group(1)) + 1);
                }

            }

            System.out.println(mapOfURLs.size());
            for (Map.Entry<String, Integer> kv : mapOfURLs.entrySet()) { // String = domain, Integer = number of pages in domain
                System.out.println(kv.getKey() + ", " + kv.getValue());
            }
            ///////////////////////////////////////////Question 4/////////////////////////////////////////////////////

            Map<String, Integer> q4 = new HashMap<>();
            TreeMap<String,Integer> sorted = new TreeMap<String,Integer>(new gt(q4));

            times = 0;
            do {

                times++;

                tempResult = state.executeQuery("SELECT * FROM data limit 10000");
                while (tempResult.next()) {
                    resultString = tempResult.getString("url");
                    String resultText = tempResult.getString("textfile");
                    q4.put(resultString, Utilities.forQ4(resultText).size());

                }
                tempResult = state.executeQuery("SELECT * FROM data limit 10000 offset " + Integer.toString(10000*times));
            }while(tempResult.next());

            sorted.putAll(q4);
            FileWriter fout = new FileWriter("./WebsiteTextCount.txt", true);
            for(Map.Entry<String, Integer> kv : sorted.entrySet())
            {
                fout.write(kv.getKey() + ", " + kv.getValue() + "\r\n");
            }
            fout.close();
            sorted = null;
            q4 = null;

            /////////////////////////////////Question 5/////////////////////////////////////////////////////////////////

            Map<String, Integer> q5 = new HashMap<>();
            TreeMap<String,Integer> q5Sorted = new TreeMap<String,Integer>(new gt(q5));

            ArrayList<String> freq = new ArrayList<>();
            times = 0;
            do {

                times++;

                tempResult = state.executeQuery("SELECT textfile FROM data limit 10000");
                while (tempResult.next()) {
                    String resultText = tempResult.getString("textfile");

                    freq.addAll(Utilities.forQ4(resultText));

                }
                tempResult = state.executeQuery("SELECT * FROM data limit 10000 offset " + Integer.toString(10000*times));
            }while(tempResult.next());

            List<Frequency> frequencies = WordFrequencyCounter.computeWordFrequencies(freq);
            Utilities.storeFrequencies(frequencies);


        } catch (Exception e) {
            e.printStackTrace();
        }


    }

}
