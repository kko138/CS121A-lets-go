/**
 * authors: Cassie Jeansonne 18923914, Kevin Ko 56956077, Samuel Lin 52478518, Sophia Chan 33196560
 */
package ir.assignments.three.helper;

import java.util.Comparator;
import java.util.Map;

/**
 * The "gt" class -- named after my favorite UCI data structure professor.
 * 
 * It implements Comparator and redefines the compare function to
 * sort a map by value FIRST, and if two key,value pairs have the same value, sort alphabetically.
 * Used and constructed in TreeMaps where kv pairs are <String,Integer>
 *
 *
 */
public class gt implements Comparator<String>
{
	Map<String,Integer> theMap;
	
	// Only constructor -- pointless to create a default constructor
	public gt(Map<String,Integer> start)
	{
		this.theMap = start;
	}

	/**
	 * overrides compare() in Comparator to sort by value and then alphabetically
	 * @param x
	 * @param y
     * @return int
     */
	@Override
	public int compare(String x, String y)
	{
		return this.theMap.get(x) == this.theMap.get(y) ?
				(x.compareTo(y) > 0 ? 1 : -1) :
				(((Integer)this.theMap.get(x)).intValue() >= ((Integer)this.theMap.get(y)).intValue() ? -1 : 1);

		// non comprehensioned
//		if (this.theMap.get(x) == this.theMap.get(y))
//		{
//			if(x.compareTo(y) > 0)
//				return 1;
//			else
//				return -1;
//		}
//		else
//		{
//			if(this.theMap.get(x) >= this.theMap.get(y))
//				return -1;
//			else
//				return 1;
//		}
        
	}
}
