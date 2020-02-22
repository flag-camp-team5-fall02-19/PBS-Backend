package rpc;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;

/**
 * Servlet implementation class Owner Register
 */
@WebServlet("/ownerregister")
public class OwnerRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OwnerRegister() {
        super();
        // TODO Auto-generated constructor stub.
    }
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			JSONObject input = RpcHelper.readJSONObject(request);
			String owner_id = input.getString("owner_id");
			String password = input.getString("password");
			String firstname = input.getString("firstname");
			String lastname = input.getString("lastname");
			String email = input.getString("email");
			String phone = input.getString("phone");
			String zipcode = input.getString("zipcode");
            String city = input.getString("city");
            String address = input.getString("address");
            
			JSONObject obj = new JSONObject();				// create JSON object
			
			if (connection.registerOwner(owner_id, password, firstname, lastname, phone, email, zipcode, city, address)) {
				obj.put("status", "OK");					// success, OK
			} else {
				obj.put("status", "User Already Exists");	// already exists
			}
			RpcHelper.writeJsonObject(response, obj);		// write to object
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();	
		}
	}
}		
