// Mohammad Zahed, Samuel Thompson, Mayra Yareli Gamboa, Michael Oh 

package searchclasses;

/**
 * Basic class for pairing a document with its similarity score in regards to the query
 * 
 * DO NOT MODIFY THIS CLASS
 */
public final class Score {
	private final int id;
	private double score;
	
	public Score(int id) {
		this.id = id;
		this.score = 0;
	}
	
	public Score(int id, double score) {
		this.id = id;
		this.score = score;
	}
	
	public int getID() {
		return id;
	}
	
	public double getScore() {
		return score;
	}
	
	public void setScore(double score) {
		this.score = score;
	}
	
	@Override
	public String toString() {
		return id + ":" + score;
	}
}

