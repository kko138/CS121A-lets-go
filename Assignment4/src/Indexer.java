/**
 * Indexer: Creates 4 different indexes into separate hashmaps
 * Created by Cassie Jeansonne 18923914, Kevin Ko 56956077, Sam Lin 52478518, Sophia Chan 33196560
 */
import org.jsoup.Jsoup;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Indexer {
    static HashMap<String, Integer> term2termid = new HashMap<>();
    static HashMap<Integer,ArrayList<Integer>> docid2termlist =  new HashMap<>();
    static HashMap<Integer,String> termid2term = new HashMap<>();
    static HashMap<Integer,ArrayList<Integer>> termid2doclist = new HashMap<>();
    static int counter = 0;

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

            //sample index term to term id
            if(!term2termid.containsKey(tokenTemp[i])) {
                term2termid.put(tokenTemp[i], ++counter);
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
            if (!termid2term.containsKey(counter)) {
                termid2term.put(counter, tokenTemp[i]);
            }

            //examples of more composite indexes: example 3, term id to doc list
            if(termid2doclist.containsKey(tokenTemp[i])) {
                ArrayList<Integer> templs2 = termid2doclist.get(counter);
                templs2.add(currentDoc);
                termid2doclist.put(counter, templs2);
            } else {
                ArrayList<Integer> templ2 = new ArrayList<Integer>();
                templ2.add(currentDoc);
                termid2doclist.put(counter, templ2);
            }
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
     * toString(): Returns content of the attribute map
     * @return String
     */
    @Override
    public String toString() {
        String toReturn = "";
        //prints map contents
        for(Map.Entry<Integer,String> entry: termid2term.entrySet()) {
            toReturn += entry.getKey() + ": " + entry.getValue() + "\n";
        }
        return toReturn;
    }

    /**
     * writeFile(): Prints contents to files
     */
    public void writeFile() {
        File term2termidF = new File("term2termid.txt");
        File docid2termlistF = new File("docid2termlist.txt");
        File termid2termF = new File("termid2term.txt");
        File termid2doclistF = new File("termid2doclist.txt");

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
        try {
            if(!termid2doclistF.exists())  {
                termid2doclistF.createNewFile();
            }
            PrintStream out = new PrintStream(new FileOutputStream(termid2doclistF));
            for(Map.Entry<Integer,ArrayList<Integer>> si: termid2doclist.entrySet()) {
                out.println(si.getKey() + ": " + si.getValue());
            }
            out.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}