package db.mysql;

import java.sql.DriverManager;
import java.sql.Statement;
import java.sql.Connection;
import java.sql.Timestamp;

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
			String sql = "DROP TABLE IF EXISTS unavailable_slot";
			statement.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS sitter_posts";
			statement.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS owner_posts";
			statement.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS reviews";
			statement.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS orders";
			statement.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS requests";
			statement.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS owners";
			statement.executeUpdate(sql);

			sql = "DROP TABLE IF EXISTS sitters";
			statement.executeUpdate(sql);

			// Step 3 Create new tables
			sql = "CREATE TABLE owners ("
					+ "owner_id VARCHAR(255) NOT NULL," 
					+ "password VARCHAR(255) NOT NULL,"
					+ "first_name VARCHAR(255),"
					+ "last_name VARCHAR(255),"
					+ "tel VARCHAR(255),"
					+ "email VARCHAR(255),"
					+ "zipcode VARCHAR(255),"
					+ "city VARCHAR(255),"
					+ "address VARCHAR(255),"
					+ "pet_type VARCHAR(255),"
					+ "pet_description VARCHAR(255),"
					+ "PRIMARY KEY (owner_id)" + ")";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE sitters (" 
					+ "sitter_id VARCHAR(255) NOT NULL," 
					+ "password VARCHAR(255) NOT NULL,"
					+ "first_name VARCHAR(255)," 
					+ "last_name VARCHAR(255),"
					+ "tel VARCHAR(255),"
					+ "email VARCHAR(255),"
					+ "zipcode VARCHAR(255),"
					+ "city VARCHAR(255),"
					+ "address VARCHAR(255),"
					+ "price FLOAT,"
					+ "review_score FLOAT,"
					+ "PRIMARY KEY (sitter_id)" + ")";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE requests ("
					+ "request_id VARCHAR(255) NOT NULL,"
					+ "owner_id VARCHAR(255) NOT NULL,"
					+ "sitter_id VARCHAR(255) NOT NULL,"
					+ "status BOOL,"
					+ "message VARCHAR(255),"
					+ "start_day DATE,"
					+ "end_day DATE,"
					+ "time DATETIME,"
					+ "PRIMARY KEY (request_id),"
					+ "FOREIGN KEY (owner_id) REFERENCES owners(owner_id),"
					+ "FOREIGN KEY (sitter_id) REFERENCES sitters(sitter_id)"
					+ ")";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE orders ("
					+ "order_id VARCHAR(255) NOT NULL,"
					+ "owner_id VARCHAR(255) NOT NULL,"
					+ "sitter_id VARCHAR(255) NOT NULL,"
					+ "request_id VARCHAR(255) NOT NULL,"
					+ "status BOOL,"
					+ "start_day DATE,"
					+ "end_day DATE,"
					+ "PRIMARY KEY (order_id),"
					+ "FOREIGN KEY (request_id) REFERENCES requests(request_id),"
					+ "FOREIGN KEY (owner_id) REFERENCES owners(owner_id),"
					+ "FOREIGN KEY (sitter_id) REFERENCES sitters(sitter_id)"
					+ ")";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE reviews ("
					+ "review_id VARCHAR(255) NOT NULL,"
					+ "order_id VARCHAR(255) NOT NULL,"
					+ "owner_id VARCHAR(255) NOT NULL,"
					+ "sitter_id VARCHAR(255) NOT NULL,"
					+ "score INTEGER, "
					+ "comment VARCHAR(255),"
					+ "comment_time DATETIME,"
					+ "PRIMARY KEY (review_id, order_id),"
					+ "FOREIGN KEY (owner_id) REFERENCES owners(owner_id),"
					+ "FOREIGN KEY (sitter_id) REFERENCES sitters(sitter_id),"
					+ "FOREIGN KEY (order_id) REFERENCES orders(order_id)"
					+ ")";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE sitter_posts ("
					+ "post_id VARCHAR(255) NOT NULL,"
					+ "sitter_id VARCHAR(255) NOT NULL,"
					+ "url VARCHAR(255) NOT NULL,"
					+ "caption VARCHAR(255),"
					+ "PRIMARY KEY (post_id, sitter_id),"
					+ "FOREIGN KEY (sitter_id) REFERENCES sitters(sitter_id)"
					+ ")";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE owner_posts ("
					+ "post_id VARCHAR(255) NOT NULL,"
					+ "owner_id VARCHAR(255) NOT NULL,"
					+ "url VARCHAR(255) NOT NULL,"
					+ "caption VARCHAR(255),"
					+ "PRIMARY KEY (post_id, owner_id),"
					+ "FOREIGN KEY (owner_id) REFERENCES owners(owner_id)"
					+ ")";
			statement.executeUpdate(sql);

			sql = "CREATE TABLE unavailable_slot ("
					+ "sitter_id VARCHAR(255) NOT NULL,"
					+ "start_time DATE ,"
					+ "end_time DATE ,"
					+ "PRIMARY KEY (sitter_id,start_time,end_time),"
					+ "FOREIGN KEY (sitter_id) REFERENCES sitters(sitter_id)"
					+ ")";
			statement.executeUpdate(sql);

			// Step 4: insert fake owner 1111/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO owners VALUES('1111', '3229c1097c00d497a0fd282d586be050', 'John', 'Smith', '1234567890', 'sun@laioffer.com', '10001', 'New York', 'Fifth Avenue', 'orange cat', 'The most lovely cat in the world')";
			statement.executeUpdate(sql);
			// Step 5: insert fake owner 3333/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO owners VALUES('3333', '3229c1097c00d497a0fd282d586be050', 'Jack', 'Chen', '1234567890', 'jack@gmail.com', '10001', 'New York', 'Fifth Avenue', 'orange cat', 'The most lovely cat in the world')";
			statement.executeUpdate(sql);
			// Step 6: insert fake sitter 3333/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO sitters VALUES('3333', '3229c1097c00d497a0fd282d586be050', 'Jack', 'Chen', '1234567890', 'jack@gmail.com', '10001', 'New York', 'Fifth Avenue', '99.9', '4.9')";
			statement.executeUpdate(sql);
			// Step 7: insert fake sitter 4444/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO sitters VALUES('4444', '3229c1097c00d497a0fd282d586be050', 'Alice', 'Jen', '1111111111', 'alice@gmail.com', '15229', 'New West', 'Fifth Avenue', '99.9', '4.9')";
			statement.executeUpdate(sql);
			// Step 8: insert fake sitter 5555/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO sitters VALUES('5555', '3229c1097c00d497a0fd282d586be050', 'Jensen', 'Wang', '2222222222', 'Jensen@gmail.com', '15139', 'New East', 'Fifth Avenue', '99.9', '4.9')";
			statement.executeUpdate(sql);
			// Step 9: insert fake sitter 6666/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO sitters VALUES('6666', '3229c1097c00d497a0fd282d586be050', 'Neal', 'Zeng', '3333333333', 'Neal@gmail.com', '15049', 'New South', 'Fifth Avenue', '99.9', '4.9')";
			statement.executeUpdate(sql);
			// Step 10: insert fake sitter 7777/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO sitters VALUES('7777', '3229c1097c00d497a0fd282d586be050', 'Jill', 'Valentine', '4444444444', 'Jill@gmail.com', '15127', 'New North', 'Fifth Avenue', '99.9', '4.9')";
			statement.executeUpdate(sql);
			// Step 11: insert fake sitter post
			sql = "INSERT INTO sitter_posts VALUES('1', '3333', 'fakeurl', 'testcaption')";
			statement.executeUpdate(sql);
			// Step 12: insert fake sitter post 2
			sql = "INSERT INTO sitter_posts VALUES('2', '3333', 'fakeurl2', 'testcaption2')";
			statement.executeUpdate(sql);
			
			// Step 13: insert fake owner post
			sql = "INSERT INTO owner_posts VALUES('1', '1111', 'fakeurl', 'testcaption')";
			statement.executeUpdate(sql);
			// Step 14: insert fake owner post 2
			sql = "INSERT INTO owner_posts VALUES('2', '1111', 'fakeurl2', 'testcaption2')";
			statement.executeUpdate(sql);
			// Step 15: insert fake owner post 3
			sql = "INSERT INTO owner_posts VALUES('3', '3333', 'fakeurl3', 'testcaption3')";
			statement.executeUpdate(sql);
			// Step 16: insert fake owner post 4
			sql = "INSERT INTO owner_posts VALUES('4', '3333', 'fakeurl4', 'testcaption4')";
			statement.executeUpdate(sql);
			
			// Step 17: insert fake request 4444/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO requests VALUES('1', '1111', '4444', TRUE, 'hello', NOW(), NOW(),CURRENT_TIMESTAMP)";
			statement.executeUpdate(sql);
									
			// Step 18: insert fake request 4444/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO requests VALUES('2', '3333', '4444', FALSE, 'hi', NOW(), NOW(),CURRENT_TIMESTAMP)";
			statement.executeUpdate(sql);
									
			// Step 19: insert fake request 5555/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO requests VALUES('3', '1111', '5555', TRUE, 'hey', NOW(), NOW(),CURRENT_TIMESTAMP)";
			statement.executeUpdate(sql);
									
			// Step 20: insert fake request 5555/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO requests VALUES('4', '3333', '5555', FALSE, 'hi there', NOW(), NOW(),CURRENT_TIMESTAMP)";
			statement.executeUpdate(sql);

			//Test for confirm request. (Used by Chloe)
//			sql = "INSERT INTO owners VALUES('1234', '3229c1097c00d497a0fd282d586be050', 'BLALA', 'Xiaomoxian', '1234567890', 'moxianbao@gmail.com', '10001', 'New York', 'wo de mo xian bao', 'orange cat', 'The most lovely cat in the world')";
//			statement.executeUpdate(sql);
//
//			sql = "INSERT INTO requests VALUES('5', '1234', '5555', TRUE, 'test','2020-02-13','2020-02-19',CURRENT_TIMESTAMP)";
//			statement.executeUpdate(sql);
			
			// Step 21: insert fake review 4444/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO orders VALUES('1', '1111', '4444', '1', TRUE, NOW(), NOW())";
			statement.executeUpdate(sql);
						
			// Step 22: insert fake review 4444/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO orders VALUES('2', '3333', '4444', '2', FALSE, NOW(), NOW())";
			statement.executeUpdate(sql);
						
			// Step 23: insert fake review 5555/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO orders VALUES('3', '1111', '5555', '3', FALSE, NOW(), NOW())";
			statement.executeUpdate(sql);
						
			// Step 24: insert fake review 5555/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO orders VALUES('4', '3333', '5555', '4', TRUE, NOW(), NOW())";
			statement.executeUpdate(sql);

			// Step 25: insert fake review 4444/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO reviews VALUES('1', '1', '1111', '4444', 3, 'good sitter', NOW())";
			statement.executeUpdate(sql);
						
			// Step 26: insert fake owner 4444/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO reviews VALUES('2', '2', '3333', '4444', 4, 'great sitter', NOW())";
			statement.executeUpdate(sql);
			
			// Step 27: insert fake review 5555/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO reviews VALUES('3', '3', '1111', '5555', 5, 'excellent sitter', NOW())";
			statement.executeUpdate(sql);
									
			// Step 28: insert fake owner 5555/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO reviews VALUES('4', '4', '3333', '5555', 2, 'poor sitter', NOW())";
			statement.executeUpdate(sql);

			// Step 29: insert fake owner_ 5555/3229c1097c00d497a0fd282d586be050
			sql = "INSERT INTO unavailable_slot VALUES('5555', '2018-03-31','2018-08-31')";
			statement.executeUpdate(sql);

			conn.close();
			System.out.println("Import done successfully");

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
