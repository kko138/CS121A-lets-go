package ir.assignments.three;

import ir.assignments.three.helper.gt;

import java.util.Map;
import java.util.TreeMap;
import java.util.ArrayList;
import java.util.HashMap;

import java.util.List;

/**
 * Counts the total number of words and their frequencies in a text file.
 */
public final class WordFrequencyCounter {

	/**
	 * This class should not be instantiated.
	 */
	private WordFrequencyCounter() {}
	
	/**
	 * Takes the input list of words and processes it, returning a list
	 * of {@link Frequency}s.
	 * 
	 * This method expects a list of lowercase alphanumeric strings.
	 * If the input list is null, an empty list is returned.
	 * 
	 * There is one frequency in the output list for every 
	 * unique word in the original list. The frequency of each word
	 * is equal to the number of times that word occurs in the original list. 
	 * 
	 * The returned list is ordered by decreasing frequency, with tied words sorted
	 * alphabetically.
	 * 
	 * The original list is not modified.
	 * 
	 * Example:
	 * 
	 * Given the input list of strings 
	 * ["this", "sentence", "repeats", "the", "word", "sentence"]
	 * 
	 * The output list of frequencies should be 
	 * ["sentence:2", "the:1", "this:1", "repeats:1",  "word:1"]
	 *  
	 * @param words A list of words.
	 * @return A list of word frequencies, ordered by decreasing frequency.
	 */
	public static List<Frequency> computeWordFrequencies(List<String> words) {
		
		List<Frequency> returnMe = new ArrayList<Frequency>();
		HashMap<String, Integer> helperMap = new HashMap<String,Integer>();
		TreeMap<String,Integer> sorted = new TreeMap<String,Integer>(new gt(helperMap));
		
		// create a map of things
		for (String item : words)
		{
			if (helperMap.containsKey(item))
				helperMap.put(item, helperMap.get(item)+1);
			else
				helperMap.put(item, 1);
		}

		// place all <k,v> pairs into the treemap
		sorted.putAll(helperMap);
		
		
		// iterate through our sorted treemap (similar to ics46 priority queues) and create frequencies to add to our return variable
		for (Map.Entry<String, Integer> kv : sorted.entrySet())
		{
			returnMe.add(new Frequency(kv.getKey(), kv.getValue()));
		}
		return returnMe;
	}
}




