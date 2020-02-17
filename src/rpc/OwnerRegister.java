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
@WebServlet("/registerOwner")
public class OwnerRegister extends HttpServlet {
	private static final long serialVersionUID = 1L;
    
    /**
     * @see HttpServlet#HttpServlet()
     */
    public OwnerRegister() {
        super();
        // TODO Auto-generated constructor stub
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
			String firstname = input.getString("first_name");
			String lastname = input.getString("last_name");
			String email = input.getString("email");
			String phone = input.getString("phone");
			String petType = input.getString("pet_type");
			String petDes = input.getString("pet_des");
			String price = input.getString("pet_range");
			
			JSONObject obj = new JSONObject();				// create JSON object
			if (connection.registerUser(owner_id, password, firstname, lastname, email, phone, petType, petDes, price)) {
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
