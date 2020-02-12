package db;
import java.sql.Date;
import java.util.List;
import java.util.Set;

import entity.Post;
import entity.Sitter;




public interface DBConnection {
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
	 * Get the sitter id for a user.
	 * 
	 * @param userId
	 * @return sitterIds
	 */
	public Set<String> viewSitterIds(String userId);

	/**
	 * Get the related sitters for a user.
	 * 
	 * @param userId
	 * @return sitters
	 */
	public Set<Sitter> viewSitters(String userId);

	public List<Sitter> searchByZipcode(String zipCode, Integer radius, String unit);

	public Set<Post> GetImagesBySitterId(String sitterId);

	public void setUnavailableTime(Date startDay, Date endDay, String ID);
}
