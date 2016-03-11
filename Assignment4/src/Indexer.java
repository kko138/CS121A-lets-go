/**
 * Indexer: Creates 4 different indexes into separate hashmaps
 * Created by Cassie Jeansonne 18923914, Kevin Ko 56956077, Sam Lin 52478518, Sophia Chan 33196560
 */
import org.jsoup.Jsoup;
import java.io.*;
import java.util.*;
import helper.gt;

public class Indexer {
    static HashMap<String, Integer> term2termid = new HashMap<>();
    static HashMap<Integer,ArrayList<Integer>> docid2termlist =  new HashMap<>();
    static HashMap<Integer,String> termid2term = new HashMap<>();
    static HashMap<Integer,LinkedHashSet<Integer>> termid2doclist = new HashMap<>();
    static LinkedHashMap<Integer,Map<Integer,Double>> termid2docidTFIDF = new LinkedHashMap<>();
    static int termid = 0;
    static int storedID = 0;

    /**
     * readFile(): Reads from file, updates corresponding attribute
     * @param file
     */
    public void readFile(File file, HashMap<String,Integer> ssi) {
        String temp = "";

        String tempFile = file.toString();
        String[] newa = tempFile.split("\\\\");				//temporary array to hold the full file path of the document
        int currentDoc = ssi.get(newa[newa.length-1]) + 1;

        //try catch to catch IOException
        //grabs content from the file and strips html flags from it
        try {
            Scanner sc = new Scanner(file);
            while(sc.hasNext()) {
                temp += sc.nextLine() + "\n";
            }
            temp = stripFile(temp);

        } catch (Exception e) {
            e.printStackTrace();
        }

        temp = temp.trim();

        String[] tokenTemp = temp.split("\\W+");        //splits content from file by the whitespaces

        //checks if word is in map, if not, adds it
        for(int i = 0; i < tokenTemp.length; i++) {
            boolean flag = false;
            //sample index term to term id
            if(!term2termid.containsKey(tokenTemp[i])) {
                term2termid.put(tokenTemp[i], ++termid);

            } else {
                storedID = termid;
                termid = term2termid.get(tokenTemp[i]);
                flag = true;
            }

            //sample index doc id to term list
            if(docid2termlist.containsKey(currentDoc)) {
                ArrayList<Integer> templs = docid2termlist.get(currentDoc);
                templs.add(term2termid.get(tokenTemp[i]));
                docid2termlist.put(currentDoc,templs);
            } else {
                ArrayList<Integer> templ = new ArrayList<Integer>();
                templ.add(term2termid.get(tokenTemp[i]));
                docid2termlist.put(currentDoc, templ);
            }

            //sample index term id to term list
            if (!termid2term.containsKey(termid)) {
                termid2term.put(termid, tokenTemp[i]);
            }

            //examples of more composite indexes: example 3, term id -> doc list, tf-idf
            if(termid2doclist.containsKey(termid)) {
                LinkedHashSet<Integer> templs2 = termid2doclist.get(termid);
                templs2.add(currentDoc);
                termid2doclist.put(termid, templs2);
            } else {
                LinkedHashSet<Integer> templ2 = new LinkedHashSet<>();
                templ2.add(currentDoc);
                termid2doclist.put(termid, templ2);
            }
            if(flag) {
                termid = storedID;
            }

        }

    }

    /**
     * whichDocHasIDAndNumberOfTimes(): creates a hashmap which lists the termID : number of documents that term appears in
     * @return HashMap<Integer,Integer>
     */
    private HashMap<Integer,Integer> whichDocHasIDAndNumberOfTimes()
    {
        HashMap<Integer,Integer> to_return = new HashMap<>();
        for(Map.Entry<Integer,LinkedHashSet<Integer>> kv: termid2doclist.entrySet())
        {
            to_return.put(kv.getKey(), kv.getValue().size());
        }


        return to_return;
    }

    /**
     * countingFrequencyOfTerms(): Based on the termid, it counts how many times that term appears per document
     * @param: termList
     * @return: HashMap<Integer,Integer>
     */
    private HashMap<Integer,Integer> countingFrequencyOfTerms(ArrayList<Integer> termList)
    {
        HashMap<Integer,Integer> to_return = new HashMap<>();
        for (Integer item : termList)
        {
            if(to_return.containsKey(item))
            {
                to_return.put(item, to_return.get(item)+1);
            }
            else
                to_return.put(item, 1);
        }
        return to_return;
    }

    /**
     * makesomeTIFIDFS(): Calculates tf-idf value for each term
     */
    public void makeSomeTFIDFS() {
        HashMap<Integer, Integer> helper = whichDocHasIDAndNumberOfTimes();

        for (Map.Entry<Integer, String> is : termid2term.entrySet()) {
            int currentTerm = is.getKey();
            HashMap<Integer, Double> mapOfTermToTFIDF = new HashMap<>();
            Map<Integer, Double> sorted = new TreeMap<>(new gt(mapOfTermToTFIDF));
            double temptfidf = 0;
            LinkedHashSet<Integer> listOfDocsAssociatedWithTermID = termid2doclist.get(currentTerm);

            //loop to calculate tfidf for each term in a document
            for (Integer num : listOfDocsAssociatedWithTermID) {
                int sizeOfList = docid2termlist.get(num).size();
                ArrayList<Integer> listOfWords = docid2termlist.get(num);
                HashMap<Integer, Integer> times = countingFrequencyOfTerms(listOfWords);
                temptfidf = (1 + Math.log10(1+ (float)times.get(currentTerm) / sizeOfList)) * Math.log10((float)docid2termlist.size() / helper.get(currentTerm));
                mapOfTermToTFIDF.put(num, temptfidf);
            }

            sorted.putAll(mapOfTermToTFIDF);        //sorts tfidf for each term

//            LinkedHashMap<Integer, Double> top10 = new LinkedHashMap<>();
//
//            //for printing out top 10 tfidf for each term
//            int count = 0;
//            for(Map.Entry<Integer, Double> id : sorted.entrySet()) {
//                if(count++ == 10) {
//                    break;
//                }
//                top10.put(id.getKey(), id.getValue());
//            }

            termid2docidTFIDF.put(currentTerm, sorted);
//            termid2docidTFIDF.put(currentTerm, top10);
        }

    }

        /**
         * stripFile(): Strips html flags from the input and non-alphanumerics
         * @param html
         * @return String
         */
    private static String stripFile(String html) {
        String temp = Jsoup.parse(html).text();
        temp = temp.toLowerCase();
        temp = temp.replaceAll("[^A-Za-z0-9\\s]", "");
        return temp;
    }

    /**
     * writeFile(): Prints contents to files
     */
    public void writeFile() {
        File term2termidF = new File("term2termid.txt");
        File docid2termlistF = new File("docid2termlist.txt");
        File termid2termF = new File("termid2term.txt");
        File termid2doclistF = new File("termid2doclist.txt");
        File termid2docidTFIDFF = new File ("termid2docidTFIDF.txt");

        //writes to term2termid.txt
        try {
            if(!term2termidF.exists()) {
                term2termidF.createNewFile();
            }

            PrintStream out = new PrintStream(new FileOutputStream(term2termidF));
            for(Map.Entry<String,Integer> si: term2termid.entrySet()) {
                out.println(si.getKey() + ": " + si.getValue());
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //writes to docid2termlist.txt
        try {
            if(!docid2termlistF.exists()) {
                docid2termlistF.createNewFile();
            }

            PrintStream out = new PrintStream(new FileOutputStream(docid2termlistF));
            for(Map.Entry<Integer,ArrayList<Integer>> si: docid2termlist.entrySet()) {
                out.println(si.getKey() + ": " + si.getValue());
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //writes to termid2term.txt
        try {
            if(!termid2termF.exists()) {
                termid2termF.createNewFile();
            }

            PrintStream out = new PrintStream(new FileOutputStream(termid2termF));
            for(Map.Entry<Integer,String> si: termid2term.entrySet()) {
                out.println(si.getKey() + ": " + si.getValue());
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //writes to termid2doclist.txt
        try {
            if(!termid2doclistF.exists())  {
                termid2doclistF.createNewFile();
            }
            PrintStream out = new PrintStream(new FileOutputStream(termid2doclistF));
            for(Map.Entry<Integer,LinkedHashSet<Integer>> si: termid2doclist.entrySet()) {
                out.println(si.getKey() + ": " + si.getValue());
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //writes to termid2docidTFIDF.txt
        try {
            if (!termid2docidTFIDFF.exists()) {
                termid2docidTFIDFF.createNewFile();
            }

            PrintStream out = new PrintStream(new FileOutputStream(termid2docidTFIDFF));
            for (Map.Entry<Integer, Map<Integer, Double>> si : termid2docidTFIDF.entrySet()) {
                out.println(si.getKey() + ": " + si.getValue());
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}