package db.mysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.sql.Date;

import db.DBConnection;
import entity.Sitter;
import entity.Post;
import entity.Sitter.SitterBuilder;
import entity.Post.PostBuilder;
import external.ZipCodeClient;


public class MySQLConnection implements DBConnection {
	 private Connection conn;
	   
	   public MySQLConnection() {
	  	 try {
	  		 Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
	  		 conn = DriverManager.getConnection(MySQLDBUtil.URL);
	  		
	  	 } catch (Exception e) {
	  		 e.printStackTrace();
	  	 }
	   }



	@Override
	public void close() {
		if (conn != null) {
	  		 try {
	  			 conn.close();
	  		 } catch (Exception e) {
	  			 e.printStackTrace();
	  		 }
	  	 }
	}

	public void setUnavailableTime(Date startDay, Date endDay, String ID) {
	}



	public List<Sitter> searchByZipcode(String zipCode, Integer radius, String unit) {
		ZipCodeClient zipCodeClient = new ZipCodeClient();
		List<Sitter> sitters = new ArrayList<>();
		List<String> zipcodes = zipCodeClient.searchZipCodeByRadius(zipCode, radius, unit);
		try {
			String sql = "SELECT * FROM sitters WHERE zipcode = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			for (String zc : zipcodes) {
				stmt.setString(1, zc);
				ResultSet rs = stmt.executeQuery();
				while (rs.next()) {
					SitterBuilder builder = new SitterBuilder();
					String sitterId = rs.getString("sitter_id");
					builder.setSitterId(sitterId);
					builder.setFirstName(rs.getString("first_name"));
					builder.setLastName(rs.getString("last_name"));
					builder.setTel(rs.getString("tel"));
					builder.setEmail(rs.getString("email"));
					builder.setZipcode(rs.getInt("zipcode"));
					builder.setAddress(rs.getString("address"));
					builder.setCity(rs.getString("city"));
					builder.setReviewScore(rs.getDouble("reviewScore"));
					builder.setImageUrl(GetImagesBySitterId(sitterId));
					sitters.add(builder.build());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sitters;
	}

	public Set<Post> GetImagesBySitterId(String sitterId) {
		if (conn == null) {
			return null;
		}
		Set<Post> images = new HashSet<>();
		try {
			String sql = "SELECT * FROM sitter_posts WHERE sitter_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1,sitterId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				PostBuilder builder = new PostBuilder();
				String url = rs.getString("url");
				String caption = rs.getString("caption");
				builder.setUrl(url);
				builder.setCaption(caption);
				images.add(builder.build());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return images;
	}


	@Override
	public String getOwnerFullname(String userId) {
		if (conn == null) {
			return "";
		}		
		String name = "";
		try {
			String sql = "SELECT first_name, last_name FROM owners WHERE owner_id = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				name = rs.getString("first_name") + " " + rs.getString("last_name");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return name;
	}
	
	@Override
	public String getSitterFullname(String userId) {
		if (conn == null) {
			return "";
		}		
		String name = "";
		try {
			String sql = "SELECT first_name, last_name FROM sitters WHERE sitter_id = ? ";
			PreparedStatement statement = conn.prepareStatement(sql);
			statement.setString(1, userId);
			ResultSet rs = statement.executeQuery();
			while (rs.next()) {
				name = rs.getString("first_name") + " " + rs.getString("last_name");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return name;
	}


	@Override
	public boolean verifyOwnerLogin(String userId, String password) {
		if (conn == null) {
			return false;
		}
		try {
			String sql = "SELECT owner_id FROM owners WHERE owner_id = ? AND password = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;

	}

	@Override
	public boolean verifySitterLogin(String userId, String password) {
		if (conn == null) {
			return false;
		}
		try {
			String sql = "SELECT sitter_id FROM sitters WHERE sitter_id = ? AND password = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			stmt.setString(2, password);
			ResultSet rs = stmt.executeQuery();
			if (rs.next()) {
				return true;
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
		}
		return false;

	}
	
	
	@Override
	public List<Sitter> searchSitters(int zipcode, String cityName) {
		// TODO Auto-generated method stub
		return null;
	}
	
	@Override
	public Set<String> viewSitterIds(String userId) {
		if (conn == null) {
			return new HashSet<>();
		}
		
		Set<String> sitterIds = new HashSet<>();
		
		try {
			String sql = "SELECT sitter_id FROM history WHERE zipcode = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, userId);
			
			ResultSet rs = stmt.executeQuery();
			
			while (rs.next()) {
				String sitterId = rs.getString("sitter_id");
				sitterIds.add(sitterId);
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		return sitterIds;
	}


	@Override
	public Set<Sitter> viewSitters(String userId) {
		// TODO Auto-generated method stub
		return null;
	}

}
