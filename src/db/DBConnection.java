package db;
import java.util.List;
import java.util.Set;


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

}
