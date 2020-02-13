package db.mysql;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;
import java.sql.Date;

import db.DBConnection;
import entity.Sitter;
import entity.Order;
import entity.Order.OrderBuilder;
import entity.Owner;
import entity.Owner.OwnerBuilder;
import entity.Post;
import entity.Request;
import entity.Request.RequestBuilder;
import entity.Sitter.SitterBuilder;
import entity.Post.PostBuilder;
import entity.Review;
import entity.Review.ReviewBuilder;
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
		//TODO
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
					builder.setZipcode(rs.getString("zipcode"));
					builder.setAddress(rs.getString("address"));
					builder.setCity(rs.getString("city"));
					builder.setPrice(rs.getDouble("price"));
					builder.setReviewScore(rs.getDouble("review_score"));
					builder.setImageUrl(GetImagesBySitterId(sitterId));
					builder.setReviews(GetReviewsBySitterId(sitterId));
					sitters.add(builder.build());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(sitters.size());
		return sitters;
	}

	@Override
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
	public Set<Review> GetReviewsBySitterId(String sitterId) {
		if (conn == null) {
			return null;
		}
		Set<Review> reviews = new HashSet<>();
		try {
			String sql = "SELECT * FROM reviews WHERE sitter_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1,sitterId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				ReviewBuilder builder = new ReviewBuilder();
				Integer score = rs.getInt("score");
				String comment = rs.getString("comment");
				builder.setScore(score);
				builder.setComment(comment);
				reviews.add(builder.build());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return reviews;
	}
	
	@Override
	public Set<Post> GetImagesByOwnerId(String ownerId) {
		if (conn == null) {
			return null;
		}
		Set<Post> images = new HashSet<>();
		try {
			String sql = "SELECT * FROM owner_posts WHERE owner_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, ownerId);
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
	public Set<Request> GetRequestsByOwnerId(String ownerId) {
		if (conn == null) {
			return null;
		}
		Set<Request> requests = new HashSet<>();
		try {
			String sql = "SELECT * FROM requests WHERE owner_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, ownerId);
			ResultSet rs = stmt.executeQuery();
			while (rs.next()) {
				RequestBuilder builder = new RequestBuilder();
				String message = rs.getString("message");
				builder.setMessage(message);
				requests.add(builder.build());
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return requests;
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
	public Set<String> viewSitterIds(String zipcode, Integer radius) {
		if (conn == null) {
			return new HashSet<>();
		}
		
		Set<String> sitterIds = new HashSet<>();
		
		ZipCodeClient client = new ZipCodeClient();
        List<String> zipcodes = client.searchZipCodeByRadius(zipcode, radius, "km");
        for (String str : zipcodes) {
        	try {
    			String sql = "SELECT sitter_id FROM sitters WHERE zipcode = ?";
    			PreparedStatement stmt = conn.prepareStatement(sql);
    			stmt.setString(1, str);
    			
    			ResultSet rs = stmt.executeQuery();
    			
    			while (rs.next()) {
    				String sitterId = rs.getString("sitter_id");
    				sitterIds.add(sitterId);
    			}
    		} catch (SQLException e) {
    			e.printStackTrace();
    		}
        }
		
		return sitterIds;
	}


	@Override
	public Set<Sitter> viewSitters(String userId) {
		if (conn == null) {
			return new HashSet<>();
		}
		
		Set<Sitter> sitters = new HashSet<>();
		
		String zipcode = ""; 
		Integer radius = 20;
		
		try {
			String sql = "SELECT * FROM owners WHERE owner_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
				
			stmt.setString(1, userId);
				
			ResultSet rs = stmt.executeQuery();
				
			zipcode = rs.getString("zipcode");

		} catch (SQLException e) {
			e.printStackTrace();
		}
		
		Set<String> sitterIds = viewSitterIds(zipcode, radius);
		
		try {
			String sql = "SELECT * FROM sitters WHERE sitter_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			for (String sitter_id : sitterIds) {
				stmt.setString(1, sitter_id);
				
				ResultSet rs = stmt.executeQuery();
				
				SitterBuilder builder = new SitterBuilder();
				
				while (rs.next()) {
					builder.setSitterId(sitter_id);
					builder.setFirstName(rs.getString("first_name"));
					builder.setLastName(rs.getString("last_name"));
					builder.setTel(rs.getString("tel"));
					builder.setEmail(rs.getString("email"));
					builder.setZipcode(rs.getString("zipcode"));
					builder.setAddress(rs.getString("address"));
					builder.setCity(rs.getString("city"));
					builder.setPrice(rs.getDouble("price"));
					builder.setReviewScore(rs.getDouble("review_score"));
					builder.setImageUrl(GetImagesBySitterId(sitter_id));
					builder.setReviews(GetReviewsBySitterId(sitter_id));
					
					sitters.add(builder.build());
				}
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return sitters;
	}



	@Override
	public Set<Owner> viewOwners(String userId) {
		
		if (conn == null) {
			// return new ArrayList<>();
			return new HashSet<>();
		}
		
		Set<Owner> owners = new HashSet<>();
		try {
			String sql = "SELECT * FROM requests WHERE sitter_id = ?";
			PreparedStatement stmt = conn.prepareStatement(sql);
			
			System.out.println("userId is: " + userId);
			
			stmt.setString(1, userId);
			ResultSet rs = stmt.executeQuery();
			
			Set<String> ownerIds = new HashSet<>();
			
			while (rs.next()) {
				System.out.println(rs.getString("owner_id"));
				ownerIds.add(rs.getString("owner_id"));
			}
			
			System.out.println("ownerIds' set size is: " + ownerIds.size());
			
			for (String ownerId : ownerIds) {
				sql = "SELECT * FROM owners WHERE owner_id = ?";
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, ownerId);
				rs = stmt.executeQuery();
				
				while (rs.next()) {
					OwnerBuilder builder = new OwnerBuilder();
					builder.setOwnerId(rs.getString("owner_id"));
					builder.setFirstName(rs.getString("first_name"));
					builder.setLastName(rs.getString("last_name"));
					builder.setTel(rs.getString("tel"));
					builder.setEmail(rs.getString("email"));
					builder.setZipcode(rs.getString("zipcode"));
					builder.setAddress(rs.getString("address"));
					builder.setCity(rs.getString("city"));
					builder.setPetType(rs.getString("pet_type"));
					builder.setPetDescription(rs.getString("pet_description"));
					builder.setImageUrl(GetImagesByOwnerId(ownerId));
					builder.setRequests(GetRequestsByOwnerId(ownerId));
					owners.add(builder.build());
				}
			}
			
		} catch (SQLException e) {
			e.printStackTrace();
		}
		System.out.println(owners.size());
		// return new ArrayList<>(owners);
		return owners;
	}
	
	
	@Override
	public Set<Order> viewOrder(String userId, Boolean isOwner) {
		if (conn == null) {
			// return new ArrayList<>();
			return new HashSet<>();
		}
		Set<Order> orders = new HashSet<>();
		if (isOwner) {
			// the user is an owner
			try {
				
				OrderBuilder builder = new OrderBuilder();
				
				String ownerId = userId;
				String ownerFirstName = "";
				String ownerLastName = "";
				
				String sql = "SELECT * FROM owners WHERE owner_id = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, ownerId);
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()) {
					ownerFirstName = rs.getString("first_name");
					ownerLastName = rs.getString("last_name");
				}
				
				System.out.println(ownerFirstName + ownerLastName);
				sql = "SELECT * FROM orders WHERE owner_id = ?";
				stmt = conn.prepareStatement(sql);
				
				System.out.println("owner is: " + userId);
				
				stmt.setString(1, userId);
				rs = stmt.executeQuery();
				Queue<String> sitterIds = new LinkedList<>();
				while (rs.next()) {
					
					String sitterId = rs.getString("sitter_id");
					sitterIds.offer(sitterId);
				}
				
				Queue<String> sitterNames = new LinkedList<>();
				while (!sitterIds.isEmpty()) {
					sql = "SELECT * FROM sitters WHERE sitter_id = ?";
					stmt = conn.prepareStatement(sql);
					
					stmt.setString(1, sitterIds.poll());
					rs = stmt.executeQuery();
					while (rs.next()) {
						sitterNames.offer(rs.getString("first_name"));
						sitterNames.offer(rs.getString("last_name"));
					}
				}
				
				
				sql = "SELECT * FROM orders WHERE owner_id = ?";
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, userId);
				rs = stmt.executeQuery();
				while (rs.next()) {
					builder.setOwnerFirstName(ownerFirstName);
					builder.setOwnerLastName(ownerLastName);
					builder.setSitterFirstName(sitterNames.poll());
					builder.setSitterLastName(sitterNames.poll());
					builder.setOrderStatus(rs.getBoolean("status"));
					orders.add(builder.build());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println(orders.size());
			
		} else {
			// the user is a sitter
			try {
				
				OrderBuilder builder = new OrderBuilder();
				
				String sitterId = userId;
				String sitterFirstName = "";
				String sitterLastName = "";
				
				String sql = "SELECT * FROM sitters WHERE sitter_id = ?";
				PreparedStatement stmt = conn.prepareStatement(sql);
				stmt.setString(1, sitterId);
				ResultSet rs = stmt.executeQuery();
				
				while (rs.next()) {
					sitterFirstName = rs.getString("first_name");
					sitterLastName = rs.getString("last_name");
				}
				
				System.out.println(sitterFirstName + sitterLastName);
				sql = "SELECT * FROM orders WHERE sitter_id = ?";
				stmt = conn.prepareStatement(sql);
				
				System.out.println("sitter is: " + userId);
				
				stmt.setString(1, userId);
				rs = stmt.executeQuery();
				Queue<String> ownerIds = new LinkedList<>();
				while (rs.next()) {
					
					String ownerId = rs.getString("owner_id");
					ownerIds.offer(ownerId);
				}
				
				Queue<String> ownerNames = new LinkedList<>();
				while (!ownerIds.isEmpty()) {
					sql = "SELECT * FROM owners WHERE owner_id = ?";
					stmt = conn.prepareStatement(sql);
					
					stmt.setString(1, ownerIds.poll());
					rs = stmt.executeQuery();
					while (rs.next()) {
						ownerNames.offer(rs.getString("first_name"));
						ownerNames.offer(rs.getString("last_name"));
					}
				}
				
				
				sql = "SELECT * FROM orders WHERE sitter_id = ?";
				stmt = conn.prepareStatement(sql);
				
				stmt.setString(1, userId);
				rs = stmt.executeQuery();
				while (rs.next()) {
					builder.setOwnerFirstName(ownerNames.poll());
					builder.setOwnerLastName(ownerNames.poll());
					builder.setSitterFirstName(sitterFirstName);
					builder.setSitterLastName(sitterLastName);
					builder.setOrderStatus(rs.getBoolean("status"));
					orders.add(builder.build());
				}
			} catch (SQLException e) {
				e.printStackTrace();
			}
			System.out.println(orders.size());
		}
		return orders;
	} 

}
