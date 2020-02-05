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

import db.DBConnection;
import entity.Sitter;


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
