import java.io.File;

/**
 * Created by Sophia on 2/20/2016.
 */
public class TestIndexer {
    public static void main(String[] args) {
        final File folder = new File("C:\\Users\\Sophia\\Desktop\\Sophia\\Winter 16\\INF141\\bbidyuk_html_files\\Html\\");
        Indexer i = new Indexer();


        for(final File fe: folder.listFiles()) {
           i.readFile(fe);
        }

        System.out.println(i.toString());

//        File fileName = new File("C:\\Users\\Sophia\\Desktop\\Sophia\\Winter 16\\INF141\\bbidyuk_html_files\\Html\\access.ics.uci.educontact.html");
//        i.readFile(fileName);
    }


}
