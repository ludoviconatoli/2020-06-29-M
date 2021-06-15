package it.polito.tdp.imdb.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.polito.tdp.imdb.model.Actor;
import it.polito.tdp.imdb.model.Adiacenza;
import it.polito.tdp.imdb.model.Director;
import it.polito.tdp.imdb.model.Movie;

public class ImdbDAO {
	
	public List<Actor> listAllActors(){
		String sql = "SELECT * FROM actors";
		List<Actor> result = new ArrayList<Actor>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Actor actor = new Actor(res.getInt("id"), res.getString("first_name"), res.getString("last_name"),
						res.getString("gender"));
				
				result.add(actor);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public List<Movie> listAllMovies(){
		String sql = "SELECT * FROM movies";
		List<Movie> result = new ArrayList<Movie>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Movie movie = new Movie(res.getInt("id"), res.getString("name"), 
						res.getInt("year"), res.getDouble("rank"));
				
				result.add(movie);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
	public List<Director> listAllDirectors(){
		String sql = "SELECT * FROM directors";
		List<Director> result = new ArrayList<Director>();
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			ResultSet res = st.executeQuery();
			while (res.next()) {

				Director director = new Director(res.getInt("id"), res.getString("first_name"), res.getString("last_name"));
				
				result.add(director);
			}
			conn.close();
			return result;
			
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public void getVertici(Map<Integer, Director> mappa, int y) {
		String sql = "SELECT DISTINCT d.id, d.first_name, d.last_name "
				+ "FROM directors d, movies m, movies_directors md "
				+ "WHERE m.year = ? AND m.id = md.movie_id AND md.director_id = d.id";
		
		Connection conn = DBConnect.getConnection();

		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, y);
			
			ResultSet res = st.executeQuery();
			while(res.next()) {
				if(!mappa.containsKey(res.getInt("d.id"))) {
					Director d = new Director(res.getInt("d.id"), res.getString("d.first_name"), res.getString("d.last_name"));
					mappa.put(d.getId(), d);
				}
				
			}
			
			conn.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Adiacenza> getArchi(int anno, Map<Integer, Director> mappa){
		String sql ="SELECT md1.director_id AS d1, md2.director_id AS d2, COUNT(*) AS peso "
				+ "FROM movies m1, movies m2, movies_directors md1, movies_directors md2, roles r1, roles r2 "
				+ "WHERE m1.year = ? AND m2.year = ? AND md1.director_id > md2.director_id AND "
				+ "		md1.movie_id = r1.movie_id AND md2.movie_id = r2.movie_id AND "
				+ "		r1.actor_id = r2.actor_id  AND m1.id = md1.movie_id AND m2.id = md2.movie_id "
				+ "		"
				+ "GROUP BY md1.director_id, md2.director_id";
		Connection conn = DBConnect.getConnection();
		
		List<Adiacenza> result = new ArrayList<>();
		try {
			PreparedStatement st = conn.prepareStatement(sql);
			st.setInt(1, anno);
			st.setInt(2, anno);
			
			ResultSet res = st.executeQuery();
			while(res.next()) {
				
				if(mappa.containsKey(res.getInt("d1")) && mappa.containsKey(res.getInt("d2"))) {
					Director d1 = mappa.get(res.getInt("d1"));
					Director d2 = mappa.get(res.getInt("d2"));
					Adiacenza a = new Adiacenza(d1, d2, res.getDouble("peso"));
					
					result.add(a);
				}
			}
			
			conn.close();
			return result;
		} catch (SQLException e) {
			e.printStackTrace();
			return null;
		}
	}
	
	
}
