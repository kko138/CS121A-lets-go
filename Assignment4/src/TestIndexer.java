import java.io.File;
import java.util.*;
import org.json.*;
/**
 * Created by Cassie 18923914, Kevin 56956077, Sam 52478518, Sophia 33196560
 */
public class TestIndexer {
    public static void main(String[] args) {
        long start = System.currentTimeMillis();
        final File folder = new File("C:\\Users\\Sophia\\Desktop\\Sophia\\Winter 16\\INF141\\bbidyuk_html_files\\Html");
        Indexer indexer = new Indexer();

        File jsonFile = new File("C:\\Users\\Sophia\\Desktop\\Sophia\\Winter 16\\INF141\\bbidyuk_html_files\\html_files.json");
        String jsonStr = "";

        //grabbing lines from jsonFile
        try {
            Scanner sc = new Scanner(jsonFile);
            while(sc.hasNext())
                jsonStr += sc.nextLine() + "\n";

        } catch (Exception e) {
            e.printStackTrace();
        }


        HashMap<String,Integer> json = new HashMap<>();
        //creating new hashmap based on json file contents
        try {
            json = jsonToMap(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //iterating through a directory and reading from the files
        for(final File fe: folder.listFiles()) {
           indexer.readFile(fe, json);
        }
        indexer.makeSomeTFIDFS();
        indexer.writeFile();
        System.out.println("Runtime: " + (System.currentTimeMillis()-start) + " ms");
    }

    /**
     * jsonToMap(): Given a string taken from a json file, turns contents into a hashmap
     * @param jsonContents
     * @return
     * @throws JSONException
     */
    private static HashMap jsonToMap(String jsonContents) throws JSONException {
        HashMap<String, Integer> map = new HashMap<>();
        JSONObject jObject = new JSONObject(jsonContents);
        Iterator<?> keys = jObject.keys();

        //temporary map to insert into the REAL map


        while( keys.hasNext() ){
            int k = Integer.parseInt((String)keys.next());  //docID
            Object ghetto = jObject.get(Integer.toString(k));   //temporary variable to convert into a String
            String v = ghetto.toString();   //temporary variable holding new json file contents to parse

            //ghetto recursion leap of faith
            JSONObject jObject2 = new JSONObject(v);
            Iterator<?> secondKey = jObject2.keys();

            String fil = "";
            while( secondKey.hasNext())
            {
                String str = (String)secondKey.next();
                String strKey = jObject2.getString(str);
                fil = strKey;
                break;
            }

            //end of ghetto recursion


            map.put(fil, k);
        }
        return map;
    }


}
