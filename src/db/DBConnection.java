package db;
import java.sql.Date;
import java.util.List;
import java.util.Set;

import entity.Order;
import entity.Owner;
import entity.Post;
import entity.Request;
import entity.Review;
import entity.Sitter;
import org.json.JSONArray;


public interface DBConnection {
    String sendRequest(String ownerId, String sitterId, String message, Date startDay, Date endDay);

    /**
	 * Close the connection.
	 */
	public void close();
	
	/**
	 * Get full name of a user as pet owner. (This is not needed for main course, just for demo
	 * and extension).
	 * 
	 * @param userId
	 * @return full name of the user
	 */
	public String getOwnerFullname(String userId);
	
	/**
	 * Get full name of a user as pet sitter. (This is not needed for main course, just for demo
	 * and extension).
	 * 
	 * @param userId
	 * @return full name of the user
	 */
	public String getSitterFullname(String userId);


	/**
	 * Return whether the credential is correct. (This is not needed for main
	 * course, just for demo and extension)
	 * 
	 * @param userId
	 * @param password
	 * @return boolean
	 */
	public boolean verifyOwnerLogin(String userId, String password);

	/**
	 * Return whether the credential is correct. (This is not needed for main
	 * course, just for demo and extension)
	 * 
	 * @param userId
	 * @param password
	 * @return boolean
	 */
	public boolean verifySitterLogin(String userId, String password);
	
	/**
	 * Search items near a geolocation and a term (optional).
	 * 
	 * @param zipcode
	 * @param cityName

	 *            (Nullable)
	 * @return list of Sitters
	 */
	public List<Sitter> searchSitters(int zipcode, String cityName);
	
	/**
	 * Get the sitter id for a user(pet owner).
	 * 
	 * @param zipcode
	 * @param radius
	 * @return sitterIds
	 */
	public Set<String> viewSitterIds(String zipcode, Integer radius);

	/**
	 * Get the related sitters for a user.
	 * 
	 * @param userId
	 * @return sitters
	 */
	public Set<Sitter> viewSitters(String userId);
	
	/**
	 * Get the related owners for a user(sitter).
	 * 
	 * @param userId
	 * @return owners
	 */
	public Set<Owner> viewOwners(String userId);
	
	/**
	 * Get the related sitters for a user (owner) within given the given range of zipcode, radius and unit.
	 * 
	 * @param zipCode
	 * @param radius
	 * @param unit
	 * @return sitters
	 */
	public List<Sitter> searchByZipcode(String zipCode, Integer radius, String unit);

	/**
	 * Get the related sitters in a given city.
	 * @param cityName
	 * @return
	 */
	public List<Sitter> searchByCityName(String cityName);
	
	/**
	 * Get the related sitters' images for a user.
	 * 
	 * @param sitterId
	 * @return posts
	 */
	public Set<Post> GetImagesBySitterId(String sitterId);
	
	/**
	 * Get the related sitters' reviews for a user.
	 * 
	 * @param sitterId
	 * @return reviews
	 */
	public Set<Review> GetReviewsBySitterId(String sitterId);
	
	/**
	 * Get the related owners' images for a user.(sitter)
	 * 
	 * @param ownerId
	 * @return posts
	 */
	public Set<Post> GetImagesByOwnerId(String ownerId);
	
	
	/**
	 * Get the related owners' requests for a user.
	 * 
	 * @param ownerId
	 * @return requests
	 */
	public Set<Request> GetRequestsByOwnerId(String ownerId, String userId);
	
	/**
	 * Get the related orders for a user (sitter or owner).
	 * 
	 * @param userId
	 * @param isOwner
	 * @return orders
	 */
	public Set<Order> viewOrder(String userId, Boolean isOwner);

	/**
	 *
	 * @param startDay
	 * @param endDay
	 * @param ID
	 * @return
	 */
	public String setUnavailableTime(Date startDay, Date endDay, String ID);

	/**
	 *
	 * @param sitter_id
	 * @return
	 */
    public Sitter getSitterInformation(String sitter_id);

	/**
	 *
	 * @param sitter_id
	 * @return
	 */
	public JSONArray getUnavailableTime(String sitter_id);

	String confirmRequest(String owner_id, String user_id, String request_id);

	/**
	 * sitter register
	 * 
	 * @param sitter_id
	 * @param password
	 * @param firstname
	 * @param lastname
	 * @param zipCode
	 * @param city
	 * @param address
	 * @param email
	 * @return ifSuccess
	 */

//	public boolean registerSitter (String sitter_id, String password, String firstname, String lastname, String zipCode, String city, String address, String email);
	

	public boolean registerSitter (String sitter_id, String password, String firstname, String lastname, String zipCode, String city, String address, String email);


	/**
	 *
	 * @param orderId
	 * @param sitterId
	 * @param ownerId
	 * @param score
	 * @param comment
	 * @return
	 */

	public String addReview(String orderId, String sitterId, String ownerId, Integer score, String comment);

	/**
	 * owner register
	 *
	 * @param owner_id
	 * @param password
	 * @param firstname
	 * @param lastname
	 * @param email
	 * @param phone
	 * @param petType
	 * @param petDes
	 * @param priceRange
	 * @return ifSuccess.
	 */
	public boolean registerOwner (String owner_id, String password, String firstname, String lastname, String email, String phone, String petType, String petDes, String price);
//
//	boolean sitterRegister(String sitter_id, String password, String firstname, String lastname);
}
