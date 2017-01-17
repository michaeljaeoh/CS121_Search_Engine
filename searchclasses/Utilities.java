// Mohammad Zahed, Samuel Thompson, Mayra Yareli Gamboa, Michael Oh 

package searchclasses;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;

/**
 * A collection of utility methods for text processing.
 */
public class Utilities {
	/**
	 * Splits the string it into alphanumeric tokens.
	 * Returns a HashMap of these tokens along with their frequencies within the string.
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
	 * ["an":1, "input":1, "string":1, "this":1, "is":2, "or":1, "it":1]
	 * 
	 * @param input the string to tokenize.
	 * @return The list of tokens (words) from the input string, along with their frequencies within the string.
	 */
	
	public static HashMap<String,Integer> tokenizeString(String text) {
        
		final HashSet<String> stopWords = new HashSet<String>(Arrays.asList("a", "about", "above", "after", "again", "against", "all", "am", "an", "and",
				"any", "are", "aren't", "as", "at", "be", "because", "been", "before", "being", "below", "between",
				"both", "but", "by", "can't", "cannot", "could", "couldn't", "did", "didn't", "do", "does", "doesn't",
				"doing", "don't", "down", "during", "each", "few", "for", "from", "further", "had", "hadn't", "has",
				"hasn't", "have", "haven't", "having", "he", "he'd", "he'll", "he's", "her", "here", "here's", "hers",
				"herself", "him", "himself", "his", "how", "how's", "i", "i'd", "i'll", "i'm", "i've", "if", "in",
				"into", "is", "isn't", "it", "it's", "its", "itself", "let's", "me", "more", "most", "mustn't", "my",
				"myself", "no", "nor", "not", "of", "off", "on", "once", "only", "or", "other", "ought", "our",
				"ours	ourselves", "out", "over", "own", "same", "shan't", "she", "she'd", "she'll", "she's", "should",
				"shouldn't", "so", "some", "such", "than", "that", "that's", "the", "their", "theirs", "them",
				"themselves", "then", "there", "there's", "these", "they", "they'd", "they'll", "they're", "they've",
				"this", "those", "through", "to", "too", "under", "until", "up", "very", "was", "wasn't", "we", "we'd",
				"we'll", "we're", "we've", "were", "weren't", "what", "what's", "when", "when's", "where", "where's",
				"which", "while", "who", "who's", "whom", "why", "why's", "with", "won't", "would", "wouldn't", "you",
				"you'd", "you'll", "you're", "you've", "your", "yours", "yourself", "yourselves")); // terms to be ignored
		
		HashMap<String,Integer> tokens = new HashMap<String,Integer>();
		
		String[] words = text.trim().split("[^a-zA-Z0-9]+");
        
		for(String word : words) {
			String w = word.toLowerCase();
			if(!w.isEmpty() && !stopWords.contains(w)) {
				if(tokens.putIfAbsent(w,1) != null){
					tokens.put(w, tokens.get(w)+1);
				}
            }
		}
		
        return tokens;
	}
}