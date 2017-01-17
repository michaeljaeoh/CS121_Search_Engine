// Mohammad Zahed, Samuel Thompson, Mayra Yareli Gamboa, Michael Oh 

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.sql.Time;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.apache.commons.io.FileUtils;

public class Indexer {
	public static HashMap<String, Integer> term2termid = new HashMap<String, Integer>();
	public static HashMap<Integer, String> termid2term = new HashMap<Integer, String>();

	public static HashMap<String, Integer> doc2docid = new HashMap<String, Integer>();
	public static HashMap<Integer, String> docid2doc = new HashMap<Integer, String>();

	public static HashMap<Integer, HashSet<Integer>> docid2termidlist = new HashMap<Integer, HashSet<Integer>>();
	public static HashMap<Integer, HashMap<Integer, Integer>> docid2termfreqlist = new HashMap<Integer, HashMap<Integer, Integer>>();
	public static HashMap<Integer, HashMap<Integer, Double>> termid2TFIDF = new HashMap<Integer, HashMap<Integer, Double>>();
	public static HashMap<Integer, HashSet<Integer>> termid2docidlist = new HashMap<Integer, HashSet<Integer>>();

	public static void main(String[] args) throws InterruptedException {
		final long startTime = System.currentTimeMillis();
		ArrayList<Page> pages = Utilities.parseIndex();
		ExecutorService es = Executors.newFixedThreadPool(25);
		for (int i = 0; i < pages.size(); i++) {
			final Page page = pages.get(i); // the final is important
			final int inde = i;
			es.execute(new Runnable() {
				Page p = page;
				int index = inde;

				public void run() {
					if (index % 100 == 0) {
						System.out.println(index + "/" + pages.size());
					}
					String fileName = p.file;
					int docId = addDoc(p.url);

					String document = loadDocument(fileName);

					addTerms(document, docId);
				};
			});
		}

		es.shutdown();
		boolean finshed = es.awaitTermination(1, TimeUnit.DAYS);
		final long endTime = System.currentTimeMillis();

		double numDocs = docid2doc.size();
		for (int i = 0; i < termid2term.size(); i++) {
			double DF = Math.log(numDocs / termid2docidlist.get(i).size());
			HashMap<Integer, Double> TFIDF = new HashMap<Integer, Double>();
			for (int x : termid2docidlist.get(i)) {
				double TF = 1 + Math.log(docid2termfreqlist.get(x).get(i));
				TFIDF.put(x, TF * DF);
			}
			termid2TFIDF.put(i, TFIDF);
		}

		System.gc();

		System.out.println(
				String.format("Total Time :%d min, %d sec", TimeUnit.MILLISECONDS.toMinutes(endTime - startTime),
						TimeUnit.MILLISECONDS.toSeconds(endTime - startTime)
								- TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(endTime - startTime))));


		System.out.println("terms"+termid2term.size());
		System.out.println("docs"+doc2docid.size());
		saveVariables("variables");
	}

	private static synchronized void addTerms(String document, int docId) {
		HashSet<Integer> termIds = new HashSet<Integer>();
		ArrayList<String> words = Utilities.tokenizeString(document);
		for (String word : words) {
			termIds.add(addTerm(word, docId));
		}
		ArrayList<Frequency> frequencies = WordFrequencyCounter.computeWordFrequencies(words);
		HashMap<Integer, Integer> termIdFreq = Utilities.freqToTuple(frequencies, term2termid);
		docid2termfreqlist.put(docId, termIdFreq);
		docid2termidlist.put(docId, termIds);
	}

	private static String loadDocument(String fileName) {
		String document;

		try {
			byte[] encoded = Files.readAllBytes(Paths.get("../Text/" + fileName));
			document = new String(encoded, "utf8");
		} catch (IOException e) {
			document = Utilities.parseFile(fileName);
		}
		return document;
	}

	private static synchronized int addTerm(String term, int docId) {
		int id = term2termid.size();

		if (term2termid.containsKey(term)) {
			id = term2termid.get(term);
			termid2docidlist.get(id).add(docId);
			return id;
		}

		term2termid.put(term, id);
		termid2term.put(id, term);

		HashSet<Integer> docIds = new HashSet<Integer>();
		docIds.add(docId);
		termid2docidlist.put(id, docIds);

		return id;
	}

	private static synchronized int addDoc(String doc) {
		int size = doc2docid.size();

		if (doc2docid.containsKey(doc))
			return doc2docid.get(doc);

		doc2docid.put(doc, size);
		docid2doc.put(size, doc);
		return size;
	}

	private static void saveVariables(String name) {
		Iterator<Integer> keySetIterator = termid2TFIDF.keySet().iterator();
		PrintWriter writer;
		try {
			writer = new PrintWriter("termid2TFIDF.txt", "UTF-8");
			while (keySetIterator.hasNext()) {
				Integer key = keySetIterator.next();
				writer.write(","+key + "=" + termid2TFIDF.get(key));
			}

			writer.close();
		} catch (FileNotFoundException | UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		
		saveVariable("termid2docidlist", termid2docidlist);
		saveVariable("docid2termfreqlist", docid2termfreqlist);
		saveVariable("docid2termidlist", docid2termidlist);
		saveVariable("docid2doc", docid2doc);
		saveVariable("doc2docid", doc2docid);
		saveVariable("termid2term", termid2term);
		saveVariable("term2termid", term2termid);
	}

	private static void saveVariable(String fileName, HashMap out) {
		try {
			FileUtils.writeStringToFile(new File(fileName + ".txt"), out.toString());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
