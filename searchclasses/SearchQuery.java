// Mohammad Zahed, Samuel Thompson, Mayra Yareli Gamboa, Michael Oh 

package searchclasses;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SearchQuery {
	public static List<Score> search(String query) {		
		
		List<Score> ranking = null;
		
		Connection conn = null;

		HashMap<String,Integer> queryTerms = Utilities.tokenizeString(query); 
		
		double qMag = 0;
		
		HashMap <Integer,Score> scores = new HashMap<Integer,Score>();
		
		try {
			Class.forName("com.mysql.jdbc.Driver");		
			conn = DriverManager.getConnection("jdbc:mysql://localhost/search_index","user","user123");		
			String dbQuery = null;
			PreparedStatement ps = null;			
			ResultSet rs = null;
			
			for(String term : queryTerms.keySet()) {
				int q_tf = queryTerms.get(term);
				double q_ntf = 1 + Math.log(q_tf);
				dbQuery = "SELECT tid,idf FROM Term WHERE term = ?";
				ps = conn.prepareStatement(dbQuery);
				ps.setString(1, term);
				rs = ps.executeQuery();

				int tid = 0;
				double q_idf = 0;
				while(rs.next()) {
					tid = rs.getInt("tid");
					q_idf = rs.getDouble("idf");
				}
				
				rs.close();
				double q_tfidf = q_ntf * q_idf;
				qMag += Math.pow(q_tfidf, 2);
				
				dbQuery = "SELECT did,tfidf FROM PartOf WHERE tid = " + tid;
				
				ps = conn.prepareStatement(dbQuery);
				
				rs = ps.executeQuery();
				
				while(rs.next()) {
					int did = rs.getInt("did");
					double d_tfidf = rs.getDouble("tfidf");
					double dotProd = q_tfidf * d_tfidf;
					if(scores.putIfAbsent(did, new Score(did,dotProd)) != null) {
						double currScore = scores.get(did).getScore();
						currScore += dotProd;
						scores.get(did).setScore(currScore);						
					}
				}
				rs.close();
			}
			
			qMag = Math.sqrt(qMag);
			
			for(Map.Entry<Integer, Score> e : scores.entrySet()) {
				dbQuery = "SELECT magnitude FROM Document WHERE did = " + e.getKey();
				ps = conn.prepareStatement(dbQuery);
				rs = ps.executeQuery();

				while(rs.next()) {
					double dMag = rs.getDouble("magnitude");
					double qdMag = qMag * dMag;
					e.getValue().setScore(e.getValue().getScore()/qdMag);
				}
				rs.close();
			}
			
			conn.close();
			
			ranking = new ArrayList<Score>(scores.values());
            
            Collections.sort(ranking, new ScoreComparator());
			
		} catch(SQLException e) {
		System.out.println("SQLException: " + e.getMessage());
		System.out.println("Error Code: " + e.getErrorCode());
		} catch(ClassNotFoundException e) {
			e.printStackTrace();
		}
		
		return ranking;
	}
}

class ScoreComparator implements Comparator<Score> {
    @Override
    public int compare(Score f1, Score f2) {
        if(f1.getScore() < f2.getScore()) {
            return 1;
        }
        else if(f1.getScore() > f2.getScore()) {
            return -1;
        }
        else if(f1.getID() < f2.getID()) {
            return -1;
        }
        return 1;
    }
}

