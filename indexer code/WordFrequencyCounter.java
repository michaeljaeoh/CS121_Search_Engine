// Mohammad Zahed, Samuel Thompson, Mayra Yareli Gamboa, Michael Oh 


import java.io.File;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;

/**
 * Counts the total number of words and their frequencies in a text file.
 */
public final class WordFrequencyCounter {
	
	// Implements a custom Comparator for Frequency. Compares by frequency and then alphabet
	public static class FrequencyComparator implements Comparator<Frequency>{
		public int compare(Frequency a, Frequency b) {
			Integer aFreq = new Integer(a.getFrequency());
			int freqComparison = aFreq.compareTo(b.getFrequency());
			return freqComparison == 0? b.getText().compareTo(a.getText()):freqComparison;
		}		
	}
	/**
	 * This class should not be instantiated.
	 */
	private WordFrequencyCounter() {}
	
	public static ArrayList<Frequency> computeWordFrequencies(ArrayList<String> words) {
		ArrayList<Frequency> list = new ArrayList<Frequency>();
		Map<String, Integer> hm = new HashMap<String, Integer>();
		
		// Creates a map where the keys are unique words and the values are the frequency of each word.
		for (String s : words){
			if (hm.containsKey(s)){
				hm.put(s, hm.get(s)+1);
			}
			else{
				hm.put(s, 1);
			}
		}
		
		// Adds all map entries into a list of Frequencies.
		for (Map.Entry<String, Integer> entry : hm.entrySet()){
			Frequency freq = new Frequency(entry.getKey(), entry.getValue());
			list.add(freq);
		}
		return list;
	}
}
