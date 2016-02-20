import java.io.File;
import java.util.Scanner;

import org.json.*;
/**
 * Created by Cassie, Kevin, Sam, Sophia on 2/20/2016.
 */
public class TestIndexer {
    public static void main(String[] args) {
        final File folder = new File("C:\\bidyuk files\\Html");
        Indexer i = new Indexer();

        File jsonFile = new File("C:\\bidyuk files\\html_files.json");
        String jsonStr = "";
        try
        {
            Scanner sc = new Scanner(jsonFile);
            while(sc.hasNext())
                jsonStr += sc.nextLine() + "\n";

        } catch (Exception e)
        {
            e.printStackTrace();
        }

        JSONObject json = new JSONObject(jsonStr);
        JSONArray arr = json.getJSONArray()



        for(final File fe: folder.listFiles()) {
           i.readFile(fe);
        }

        System.out.println(i.toString());

//        File fileName = new File("C:\\Users\\Sophia\\Desktop\\Sophia\\Winter 16\\INF141\\bbidyuk_html_files\\Html\\access.ics.uci.educontact.html");
//        i.readFile(fileName);
    }


}
