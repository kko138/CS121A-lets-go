/**
 * authors: Cassie Jeansonne 18923914, Kevin Ko 56956077, Samuel Lin 52478518, Sophia Chan 33196560
 */
package ir.assignments.three;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.util.*;

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

	public static ArrayList<String> forQ4(String input)
	{
		Scanner scan = new Scanner(input);
		String s;
		ArrayList<String> returnMe = new ArrayList<String>();

		try {
			Scanner fl = new Scanner(new File("./src/main/StopWords.txt"));
			Set<String> bannedWords = new LinkedHashSet<>();

			while(fl.hasNextLine())
			{
				bannedWords.add(fl.nextLine());
			}


			// iterates through the input file and deletes everything that isn't alphanumeric
			while (scan.hasNext()) {
				s = scan.next();
				s = s.replaceAll("[^A-Za-z0-9 ]", "");
				if (!s.equals("") && !bannedWords.contains(s))
					returnMe.add(s.toLowerCase());
			}

			return returnMe;
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return returnMe;
	}


	/**
	 * Takes a list of {@link Frequency}s and outputs into a file. It also
	 * outputs the total number of items, and the total number of unique items.
	 *
	 * Example:
	 *
	 * Given the input list of word frequencies
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 *
	 * The following should be outputted to the file:
	 *
	 * sentence	2
	 * the		1
	 * this		1
	 * repeats	1
	 * word		1
	 *
	 * @param frequencies A list of frequencies.
	 */
	public static void storeFrequencies(List<Frequency> frequencies) {
		try {


			FileWriter fout = new FileWriter("./CommonWords.txt", true);
			int total = 0;
			int unique = 0;
			int highestCharCount = 0;
			int numGrams = 0;

			// Figure out how many grams. If frequency is not iterable gracefully catch exception.
			try {
				Iterator<Frequency> itr = frequencies.iterator();
				String s = itr.next().toString();
				StringTokenizer st = new StringTokenizer(s);
				numGrams = st.countTokens();
			} catch (Exception e) {
				fout.write("No elements in the Frequency!\r\n");
			}


			int countOfWords = 1;
			// print top 500 words
			for (Frequency temp : frequencies) {
				if(countOfWords == 500)
					break;
				fout.write(temp.getText() + ", " + temp.getFrequency() + "\r\n");
				countOfWords++;

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
