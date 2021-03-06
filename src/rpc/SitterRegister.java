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
@WebServlet("/registerSitter")
public class SitterRegister extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SitterRegister() {
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
            String sitter_id = input.getString("sitter_id");
            String password = input.getString("password");
        	String firstname = input.getString("firstname");
			String lastname = input.getString("lastname");
			String email = input.getString("email");
			String phone = input.getString("phone");
            String zipcode = input.getString("zipcode");
            String city = input.getString("city");
            String address = input.getString("address");

            JSONObject obj = new JSONObject();

			if (connection.registerSitter(sitter_id, password, firstname, lastname, phone, email, zipcode, city, address)) {
				obj.put("status", "OK");
			} else {
				obj.put("status", "User Already Exists");
			}

            RpcHelper.writeJsonObject(response, obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

    }
}