package db.mysql;

import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

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
import org.json.JSONArray;

import javax.xml.transform.Result;

public class MySQLConnection implements DBConnection {
    private Connection conn;

    /**
     * It's used by SetUnavailable Time.
     * It is called when an sitter wants to set a period of time to be unavailable.
     * @param startDay the startDay of the chosen unavailable period (included).
     * @param endDay the endDay of the chosen unavailable period (included).
     * @param ID the id of the sitter.
     * @return
     */
    public String setUnavailableTime(Date startDay, Date endDay, String ID) {
		if (conn == null) {
			System.err.println("DB connection failed");
			return "DB connection failed";
		}
		try {
			String sql1 = "SELECT *  FROM unavailable_slot WHERE " +
                    "   (sitter_id = ?) AND ("+
					"   (start_time <= ? AND end_time >= ?) OR" +
					"   (start_time <= ? AND end_time >= ?) OR" +
					"   (start_time >= ? AND end_time <= ?))";
			PreparedStatement check_ps = conn.prepareStatement(sql1);
			check_ps.setString(1,ID);
            check_ps.setString(2,startDay.toString());
            check_ps.setString(3,startDay.toString());
            check_ps.setString(4,endDay.toString());
            check_ps.setString(5,endDay.toString());
            check_ps.setString(6,startDay.toString());
            check_ps.setString(7,endDay.toString());
			ResultSet rs = check_ps.executeQuery();
			if (rs.next()){
				return "time conflict with other unavailable time";
			}
            String sql3 = "SELECT *  FROM orders WHERE " +
                    "   (sitter_id = ?) AND ("+
                    "   (start_day <= ? AND end_day >= ?) OR" +
                    "   (start_day <= ? AND end_day >= ?) OR" +
                    "   (start_day >= ? AND end_day <= ?))";
            PreparedStatement check_ps3 = conn.prepareStatement(sql3);
            check_ps3.setString(1,ID);
            check_ps3.setString(2,startDay.toString());
            check_ps3.setString(3,startDay.toString());
            check_ps3.setString(4,endDay.toString());
            check_ps3.setString(5,endDay.toString());
            check_ps3.setString(6,startDay.toString());
            check_ps3.setString(7,endDay.toString());
            ResultSet rs3 = check_ps3.executeQuery();
            if (rs3.next()){
                return "time conflict with orders";
            }
            String sql2 = "INSERT IGNORE INTO unavailable_slot(sitter_id,start_time,end_time) VALUES (?,?,?)";
            PreparedStatement ps = conn.prepareStatement(sql2);
            ps.setString(1, ID);
            ps.setString(2, startDay.toString());
            ps.setString(3, endDay.toString());
            ps.execute();
            return "successfully inserted!";
		} catch (Exception e) {
			e.printStackTrace();
		}
		return "";
	}

    public MySQLConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver").getConstructor().newInstance();
            conn = DriverManager.getConnection(MySQLDBUtil.URL);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String sendRequest(String ownerId, String sitterId, String message, Date startDay, Date endDay) {
	    if (conn == null) {
            System.err.println("DB connection failed");
            return "DB connection failed";
        }
	    try {
            String sql = "INSERT IGNORE INTO requests(request_id,owner_id,sitter_id,status,message,start_day,end_day,time) VALUES (?,?,?,?,?,?,?,CURRENT_TIMESTAMP)";
            PreparedStatement ps = conn.prepareStatement(sql);
            // generate unique request id by concatenating owner id and current time
            long currentTime = Calendar.getInstance().getTimeInMillis();
            String requestID = ownerId + "-"+ Long.valueOf(currentTime);
            ps.setString(1, requestID);
            ps.setString(2, ownerId);
            ps.setString(3, sitterId);
            ps.setString(4, "true");
            ps.setString(5, message);
            ps.setString(6, startDay.toString());
            ps.setString(7, endDay.toString());
            ps.execute();
            return "successfully sent the request!";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
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
					sitters.add(buildSitterFromResults(rs));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(sitters.size());
        return sitters;
    }

    @Override
    public List<Sitter> searchByCityName(String cityName) {
        List<Sitter> sitters = new ArrayList<>();
        try {
            String sql = "SELECT * FROM sitters WHERE city = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
			stmt.setString(1, cityName);
			ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                sitters.add(buildSitterFromResults(rs));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        System.out.println(sitters.size());
        return sitters;
    }

	private Sitter buildSitterFromResults(ResultSet rs) throws SQLException {
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
		return builder.build();
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
            stmt.setString(1, sitterId);
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
            stmt.setString(1, sitterId);
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

    /**
     * Used by viewOwner servlet to get the posts of an owner.
     * @param ownerId The owner Id.
     * @return
     */
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

    /**
     * Used by viewOwner servlet to get the requests sent by a owner to the certain sitter.
     * @param ownerId The owner id.
     * @return
     */
    @Override
    public Set<Request> GetRequestsByOwnerId(String ownerId, String sitterId) {
        if (conn == null) {
            return null;
        }
        Set<Request> requests = new HashSet<>();
        try {
            String sql = "SELECT * FROM requests WHERE owner_id = ? AND  sitter_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, ownerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                RequestBuilder builder = new RequestBuilder();
                String request_id = rs.getString("request_id");
                String message = rs.getString("message");
                String startDay = rs.getString("start_day");
                String endDay = rs.getString("end_day");
                String time = rs.getString("time");
                builder.setMessage(message);
                builder.setStartDay(startDay);
                builder.setEndDay(endDay);
                builder.setTime(time);
                builder.setID(request_id);
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


    /**
     * It's used to by ViewOwner servlet for sitters to see owners who sent requests to him.
     * Only request with status True (acceptable) will be shown.
     * @param userId The user who wants to view requests he received.
     * @return
     */
    @Override
    public Set<Owner> viewOwners(String userId) {

        if (conn == null) {
            // return new ArrayList<>();
            return new HashSet<>();
        }

        Set<Owner> owners = new HashSet<>();
        try {
            String sql = "SELECT * FROM requests WHERE sitter_id = ? AND status = TRUE";
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
                    builder.setRequests(GetRequestsByOwnerId(ownerId,userId));
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

    /**
     * Used by ViewSitter servlet to get sitter detailed information.
     * @param sitter_id the sitter id.
     * @return
     */
    public Sitter getSitterInformation(String sitter_id){
        if (conn == null) {
            return null;
        }
        try {
            String sql = "SELECT * FROM sitters WHERE sitter_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);

            stmt.setString(1, sitter_id);

            ResultSet rs = stmt.executeQuery();

            while(rs.next()){
                Sitter sitter = buildSitterFromResults(rs);
                return sitter;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Used by ViewSitter servlet to get the unavailable time of a sitter.
     * @param sitter_id The sitter id.
     * @return
     */
    public JSONArray getUnavailableTime(String sitter_id){
        if (conn == null) {
            return null;
        }
        JSONArray res = new JSONArray();
        try {
            String sql = "SELECT * FROM unavailable_slot WHERE sitter_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, sitter_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                JSONArray temp = new JSONArray();
                temp.put(rs.getString("start_time"));
                temp.put(rs.getString("end_time"));
                res.put(temp);
            }
            String sql2 = "SELECT * FROM orders WHERE sitter_id = ?";
            PreparedStatement stmt2 = conn.prepareStatement(sql2);
            stmt2.setString(1, sitter_id);
            ResultSet rs2 = stmt2.executeQuery();
            while(rs2.next()){
                JSONArray temp = new JSONArray();
                temp.put(rs2.getString("start_day"));
                temp.put(rs2.getString("end_day"));
                res.put(temp);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

	@Override
	public boolean registerSitter(String sitter_id, String password, String firstname, String lastname, String zipCode,
			String city, String address, String email) {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean registerOwner(String owner_id, String password, String firstname, String lastname, String email,
			String phone, String petType, String petDes, String price) {
		// TODO Auto-generated method stub
		return false;
	}

    /**
     * It's used by ConfirmRequest servlet. It is called when a sitter accepts the request from the owner.
     * @param owner_id The owner who sent the request.
     * @param sitter_id The sitter who received the request.
     * @param request_id The request id.
     * @return
     */
    @Override
    public String confirmRequest(String owner_id, String sitter_id, String request_id) {
        try {
            String sql = "SELECT * FROM requests WHERE request_id = ?";
            PreparedStatement stmt = conn.prepareStatement(sql);
            stmt.setString(1, request_id);
            ResultSet rs = stmt.executeQuery();
            while(rs.next()){
                String owner_id_database = rs.getString("owner_id");
                String sitter_id_database = rs.getString("sitter_id");
                Boolean request_status_database = rs.getBoolean("sitter_id");
                String start_day_database = rs.getString("start_day");
                String end_day_database = rs.getString("end_day");

                if (!owner_id_database.equals(owner_id)){
                    return "ERROR: owner_id is not consistent with request id.";
                }
                if (!sitter_id_database.equals(sitter_id)){
                    return "ERROR: sitter_id is not consistent with request id.";
                }
                if (request_status_database==false){
                    return "ERROR: request status is False. The request could not be confirmed";
                }

                //Make a new order.
                long currentTime = Calendar.getInstance().getTimeInMillis();
                String orderID = request_id + "-"+ Long.valueOf(currentTime);

                String insert_sql = "INSERT INTO orders VALUES(?, ?, ?, ?, TRUE , ?, ?)";
                PreparedStatement insertStatement = conn.prepareStatement(insert_sql);
                insertStatement.setString(1,orderID);
                insertStatement.setString(2,owner_id);
                insertStatement.setString(3,sitter_id);
                insertStatement.setString(4,request_id);
                insertStatement.setString(5,start_day_database);
                insertStatement.setString(6,end_day_database);
                insertStatement.execute();

                //Turn the request status into false.
                String update_sql = "UPDATE requests SET status = FALSE WHERE request_id=?";
                PreparedStatement updateStatement = conn.prepareStatement(update_sql);
                updateStatement.setString(1,request_id);
                updateStatement.executeUpdate();

                //to detect if the owner has other conflict request with this request
                String sql1 = "SELECT * FROM requests WHERE ( owner_id = ? OR sitter_id = ? ) AND status = TRUE";
                PreparedStatement stmt1 = conn.prepareStatement(sql1);
                stmt1.setString(1, owner_id);
                stmt1.setString(2, sitter_id);
                ResultSet rs1 = stmt1.executeQuery();
                while (rs1.next()){
                    String id_another = rs1.getString("request_id");
                    String start = rs1.getString("start_day");
                    String end = rs1.getString("end_day");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd-MM-yyyy");
                    java.util.Date start_another_request = sdf.parse(start);
                    java.util.Date end_another_request = sdf.parse(end);
                    java.util.Date start_confirm = sdf.parse(start_day_database);
                    java.util.Date end_confirm = sdf.parse(end_day_database);
                    //The case if the time is conflict.
                    if ( ! (end_another_request.before(start_confirm) ||start_another_request.after(end_confirm) ) ) {
                        String update_sql2 = "UPDATE requests SET status = FALSE WHERE request_id=?";
                        PreparedStatement updateStatement2 = conn.prepareStatement(update_sql2);
                        updateStatement2.setString(1,id_another);
                        updateStatement2.executeUpdate();
                    }
                }
                return "Request confirmed successfully!";
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return "";
    }


    @Override
    public String addReview(String orderId, String sitterId, String ownerId, Integer score, String comment) {
        if (conn == null) {
            System.err.println("DB connection failed");
            return "DB connection failed";
        }
        try {
            String sql = "INSERT IGNORE INTO reviews (review_id,order_id,owner_id,sitter_id,score,comment,comment_time) VALUES (?,?,?,?,?,?,CURRENT_TIMESTAMP)";
            PreparedStatement ps = conn.prepareStatement(sql);
            // generate unique review id by concatenating owner id and current time
            long currentTime = Calendar.getInstance().getTimeInMillis();
            String reviewID = ownerId + "-"+ Long.valueOf(currentTime);
            ps.setString(1, reviewID);
            ps.setString(2, orderId);
            ps.setString(3, ownerId);
            ps.setString(4, sitterId);
            ps.setString(5, Integer.toString(score));
            ps.setString(6, comment);
            ps.execute();

            // update the sitter score
            sql = "SELECT * FROM reviews WHERE sitter_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, sitterId);
            ResultSet rs = ps.executeQuery();
            // retrieve the number of reviews corresponding to the sitter
            int totalReviewNumber = 0;
            if (rs != null) {
                rs.beforeFirst();
                rs.last();
                totalReviewNumber = rs.getRow();
            }

            // get the current score of the sitter
            sql = "SELECT * FROM sitters WHERE sitter_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, sitterId);
            rs = ps.executeQuery();

            // calculate the new score and update to sitter table
            double currentScore = 0.0;
            while (rs.next()) {
                currentScore = rs.getDouble("review_score");
            }

            double newScore = (currentScore * (totalReviewNumber - 1) + score) / totalReviewNumber;

            System.out.println("current score " + currentScore);
            System.out.println("review # " + totalReviewNumber);
            System.out.println("new score " + newScore);

            sql = "UPDATE sitters SET review_score = ? WHERE sitter_id = ?";
            ps = conn.prepareStatement(sql);
            ps.setString(1, Double.toString(newScore));
            ps.setString(2, sitterId);
            ps.execute();
            return "successfully sent the review!";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

//    @Override
//    public boolean registerOwner(String owner_id, String password, String firstname, String lastname, String email, String phone, String petType, String petDes, String price) {
//        return false;
//    }
//
//    @Override
//    public boolean sitterRegister (String sitter_id, String password, String firstname, String lastname) {
//    	if (conn == null) {
//    		System.err.println("DB connection failed");
//    		return false;
//    	}
//
//		try {
//			String sql = "INSERT IGNORE INTO users VALUES (?, ?, ?, ?)";
//			PreparedStatement ps = conn.prepareStatement(sql);
//			ps.setString(1, sitter_id);
//			ps.setString(2, password);
//			ps.setString(3, firstname);
//			ps.setString(4, lastname);
//
//			return ps.executeUpdate() == 1;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;	
//    }

//    @Override
//    public boolean ownerRegister (String owner_id, String password, String firstname, String lastname) {
//    	if (conn == null) {
//    		System.err.println("DB connection failed");
//    		return false;
//    	}
//
//		try {
//			String sql = "INSERT IGNORE INTO users VALUES (?, ?, ?, ?)";
//			PreparedStatement ps = conn.prepareStatement(sql);
//			ps.setString(1, owner_id);
//			ps.setString(2, password);
//			ps.setString(3, firstname);
//			ps.setString(4, lastname);
//
//			return ps.executeUpdate() == 1;
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		return false;
//    }
}
