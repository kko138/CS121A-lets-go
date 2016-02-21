/**
 *
 * Created by Cassie, Kevin, Samuel, Sophia
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
    static int counter = 0;

    /**
     * readFile(): Reads from file, updates attribute map
     * @param file
     */
    public void readFile(File file, HashMap<String,Integer> ssi) {
        String temp = "";

        String tempFile = file.toString();
        String[] newa = tempFile.split("\\\\");
        int currentDoc = ssi.get(newa[newa.length-1]) + 1;

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
            if(!term2termid.containsKey(tokenTemp[i])) {
                term2termid.put(tokenTemp[i], ++counter);
            }
            if(docid2termlist.containsKey(currentDoc)) {
                ArrayList<Integer> templs = docid2termlist.get(currentDoc);
                templs.add(term2termid.get(tokenTemp[i]));
               docid2termlist.put(currentDoc,templs);
            } else {
                ArrayList<Integer> templ = new ArrayList<Integer>();
                templ.add(term2termid.get(tokenTemp[i]));
                docid2termlist.put(currentDoc, templ);
            }
            if (!termid2term.containsKey(counter)) {
                termid2term.put(counter, tokenTemp[i]);
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

    }
}
