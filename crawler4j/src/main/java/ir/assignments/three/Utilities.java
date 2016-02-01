package ir.assignments.three;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import java.util.StringTokenizer;
import java.util.Iterator;

/**
 * A collection of utility methods for text processing.
 */
public class Utilities {
	/**
	 * Reads the input text file and splits it into alphanumeric tokens.
	 * Returns an ArrayList of these tokens, ordered according to their
	 * occurrence in the original text file.
	 *
	 * Non-alphanumeric characters delineate tokens, and are discarded.
	 *
	 * Words are also normalized to lower case.
	 *
	 * Example:
	 *
	 * Given this input string
	 * "An input string, this is! (or is it?)"
	 *
	 * The output list of strings should be
	 * ["an", "input", "string", "this", "is", "or", "is", "it"]
	 *
	 * @param input The file to read in and tokenize.
	 * @return The list of tokens (words) from the input file, ordered by occurrence.
	 */
	public static ArrayList<String> tokenizeFile(String input)
	{
		Scanner scan = new Scanner(input);
		String s;
		ArrayList<String> returnMe = new ArrayList<String>();

		// iterates through the input file and deletes everything that isn't alphanumeric
		while (scan.hasNext())
		{
			s = scan.next();
			s = s.replaceAll("[^A-Za-z0-9 ]", "");
			if(!s.equals(""))
				returnMe.add(s.toLowerCase());
		}

		return returnMe;
	}


	/**
	 * Takes a list of {@link Frequency}s and prints it to standard out. It also
	 * prints out the total number of items, and the total number of unique items.
	 *
	 * Example one:
	 *
	 * Given the input list of word frequencies
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 *
	 * The following should be printed to standard out
	 *
	 * Total item count: 6
	 * Unique item count: 5
	 *
	 * sentence	2
	 * the		1
	 * this		1
	 * repeats	1
	 * word		1
	 *
	 *
	 * Example two:
	 *
	 * Given the input list of 2-gram frequencies
	 * ["you think:2", "how you:1", "know how:1", "think you:1", "you know:1"]
	 *
	 * The following should be printed to standard out
	 *
	 * Total 2-gram count: 6
	 * Unique 2-gram count: 5
	 *
	 * you think	2
	 * how you		1
	 * know how		1
	 * think you	1
	 * you know		1
	 *
	 * @param frequencies A list of frequencies.
	 */
	public static void printFrequencies(List<Frequency> frequencies) {
		int total = 0;
		int unique = 0;
		int highestCharCount = 0;
		int numGrams = 0;

		// Figure out how many grams. If frequency is not iterable gracefully catch exception.
		try{
			Iterator<Frequency> itr = frequencies.iterator();
			String s = itr.next().toString();
			StringTokenizer st = new StringTokenizer(s);
			numGrams = st.countTokens();
		} catch (Exception e)
		{
			System.out.println("No elements in the Frequency!");
		}


		// Calculate total, unique, and highestCharCount
		for (Frequency temp : frequencies)
		{
			total += temp.getFrequency();
			unique += 1;
			if (temp.getText().length() > highestCharCount)
				highestCharCount = temp.getText().length();
		}

		// output depends on number of grams
		if (numGrams == 1)
		{
			System.out.println("Total item count: " + total);
			System.out.println("Unique item count: " + unique + "\n");
		}
		else if (numGrams == 2)
		{
			System.out.println("Total 2-gram count: " + total);
			System.out.println("Unique 2-gram count: " + unique + "\n");
		}
		else
		{
			System.out.println("Total palindrome count: " + total);
			System.out.println("Unique palindrome count: " + unique + "\n");
		}

		// print regardless
		for (Frequency temp : frequencies)
		{
			System.out.print(String.format("%-" + Integer.toString(highestCharCount) + "s\t", temp.getText()));
			System.out.println(temp.getFrequency());
		}
	}
}
