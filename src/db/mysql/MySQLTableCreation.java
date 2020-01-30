package db.mysql;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;

public class MySQLTableCreation {
	// Run this as Java application to reset db schema.
	public static void main(String[] args) {
		try {
			// Step 1 Connect to MySQL.
			System.out.println("Connecting to " + MySQLDBUtil.URL);
			Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
			Connection conn = DriverManager.getConnection(MySQLDBUtil.URL);

			if (conn == null) {
				return;
			}

			// Step 2 Drop tables in case they exist.
			Statement statement = conn.createStatement();
			String sql = "DROP TABLE IF EXISTS owners";
			statement.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS posts";
			statement.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS reviews";
			statement.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS sitters";
			statement.executeUpdate(sql);

			// Step 3 Create new tables
			sql = "CREATE TABLE owners (" 
					+ "owner_id VARCHAR(255) NOT NULL," 
					+ "password VARCHAR(255) NOT NULL,"
					+ "first_name VARCHAR(255)," 
					+ "last_name VARCHAR(255)," 
					+ "pet_type VARCHAR(255)," 
					+ "PRIMARY KEY (owner_id)" + ")";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE sitters (" 
					+ "sitter_id VARCHAR(255) NOT NULL," 
					+ "password VARCHAR(255) NOT NULL,"
					+ "first_name VARCHAR(255)," 
					+ "last_name VARCHAR(255)," 
					+ "zipcode INTEGER," 
					+ "sitter_availability BOOLEAN,"
					+ "PRIMARY KEY (sitter_id)" + ")";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE reviews ("
					+ "owner_id VARCHAR(255) NOT NULL,"
					+ "sitter_id VARCHAR(255) NOT NULL,"
					+ "review_id VARCHAR(255) NOT NULL,"
					+ "owner_review VARCHAR(255),"
					+ "sitter_review VARCHAR(255),"
					+ "PRIMARY KEY (review_id, owner_id, sitter_id)," 
					+ "FOREIGN KEY (owner_id) REFERENCES owners(owner_id),"
					+ "FOREIGN KEY (sitter_id) REFERENCES sitters(sitter_id)"
					+ ")";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE posts (" 
					+ "owner_id VARCHAR(255) NOT NULL," 
					+ "sitter_id VARCHAR(255) NOT NULL,"
					+ "post_id VARCHAR(255) NOT NULL,"
					+ "home_image_url VARCHAR(255),"
					+ "pet_image_url VARCHAR(255),"
					+ "PRIMARY KEY (post_id, owner_id, sitter_id)," 
					+ "FOREIGN KEY (owner_id) REFERENCES owners(owner_id),"
					+ "FOREIGN KEY (sitter_id) REFERENCES sitters(sitter_id)"
					+ ")";
			statement.executeUpdate(sql);

			// Step 4: insert fake owner 1111/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO owners VALUES('1111', '3229c1097c00d497a0fd282d586be050', 'John',  'Smith', 'Cat')";
			statement.executeUpdate(sql);
			// Step 4: insert fake sitter 3333/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO sitters VALUES('3333', '3229c1097c00d497a0fd282d586be050', 'Jack', 'Chen', 12345, TRUE)";
			statement.executeUpdate(sql);

			conn.close();
			System.out.println("Import done successfully");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
