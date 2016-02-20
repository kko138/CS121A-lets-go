/**
 *
 * Created by Cassie, Kevin, Samuel, Sophia
 */
import org.jsoup.Jsoup;
import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Indexer {
    static HashMap<String, Integer> map = new HashMap<>();
    static int counter = 0;

    /**
     * readFile(): Reads from file, updates attribute map
     * @param file
     */
    public void readFile(File file) {
        String temp = "";

        //try catch to catch IOException
        try {
            Scanner sc = new Scanner(file);
            while(sc.hasNext()) {
                temp += sc.nextLine() + "\n";
            }
            temp = stripFile(temp);

        } catch (Exception e) {
            System.out.println("blah");
        }

        temp = temp.trim();

        String[] tokenTemp = temp.split("\\W+");

        //checks if word is in map, if not adds it
        for(int i = 0; i < tokenTemp.length; i++) {
            if(!map.containsKey(tokenTemp[i])) {
                map.put(tokenTemp[i], ++counter);
            }
        }

    }

    /**
     * stripFile(): Strips html flags from the input and non-alphanumerics
     * @param html
     * @return String
     */
    private String stripFile(String html) {
        String temp = Jsoup.parse(html).text();
        temp = temp.toLowerCase();
        temp = temp.replaceAll("[^A-Za-z0-9\\s]", "");
        return temp;
    }

    /**
     * toString(): Returns content of the attribute map
     * @return String
     */
    @Override
    public String toString() {
        String toReturn = "";
        //prints map contents
        for(Map.Entry<String,Integer> entry: map.entrySet()) {
            toReturn += entry.getKey() + ": " + entry.getValue() + "\n";
        }
        return toReturn;
    }
}
