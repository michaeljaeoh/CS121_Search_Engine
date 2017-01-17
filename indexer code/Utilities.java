// Mohammad Zahed, Samuel Thompson, Mayra Yareli Gamboa, Michael Oh 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.HashSet;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.jsoup.Jsoup;

/**
 * A collection of utility methods for text processing.
 */
public class Utilities {

	public static ArrayList<String> tokenizeString(String input) {
		
		final HashSet<String> stopWords = new HashSet<String>(Arrays.asList("", "a", "about", "above", "after", "again", "against", "all", "am", "an", "and",
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
		
		ArrayList<String> list = new ArrayList<String>();
		
		for (String word : input.trim().split("[^a-zA-Z0-9]+")){
			String w = word.toLowerCase();
			if(!stopWords.contains(w)) {
				list.add(w);
			}
		}

		//test print
		//System.out.println(Arrays.toString(list.toArray()));
		return list;
	}

	public static void printFrequencies(List<Frequency> frequencies) {
		int total = 0;
		int unique = 0;
		try{
			for (Frequency f : frequencies){
				total += f.getFrequency();
				unique++;
			}

			System.out.format("Total item count: %d\n", total);
			System.out.format("Unique item count: %d\n\n", unique);
			
			for (Frequency f : frequencies){
				System.out.format("%-15s %5d\n", f.getText(), f.getFrequency());
			}
		}
		catch (Exception ex){
			ex.printStackTrace();
			System.exit(1);

		}

	}
	public static HashMap<Integer, Integer> freqToTuple(ArrayList<Frequency> frequencies, HashMap<String, Integer> term2termid) {
		HashMap<Integer, Integer> out = new HashMap<Integer, Integer>();
		for(Frequency f : frequencies){
			int id = term2termid.get(f.getText());
			out.put(id,f.getFrequency());
		}
		return out;
	}
	
	public static ArrayList<Page> parseIndex() {
		JSONParser parser = new JSONParser();
		JSONObject a;
		try {
			a = (JSONObject) parser.parse(new FileReader("../Html/html_files.json"));
		} catch (IOException | ParseException e) {
			e.printStackTrace();
			a= (JSONObject) new JSONObject();
		}

	    
		ArrayList <Page> pages = new ArrayList<Page>();
		
		Set<String> keys = a.keySet();
		for(String key: keys){
			pages.add(new Page(((JSONObject)a.get(key)).get("file").toString(),((JSONObject)a.get(key)).get("url").toString()));
		}
		
		return pages;
	}

	public static String parseFile(String fileName) {
		File file= new File("../Html/" + fileName);
		String out = "HELLO!!!";
		try {
			out = Jsoup.parse(file, null).text();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		try {
			FileUtils.writeStringToFile(new File("../Text/" + fileName), out);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return out;
	}

	
}
