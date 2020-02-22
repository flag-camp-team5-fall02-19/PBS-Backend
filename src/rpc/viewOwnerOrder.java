package rpc;

import java.io.IOException;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.json.JSONArray;
import org.json.JSONObject;

import db.DBConnection;
import db.DBConnectionFactory;
import entity.Order;

/**
 * Servlet implementation class viewOwnerOrder
 */
@WebServlet("/viewownerorder")
public class viewOwnerOrder extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public viewOwnerOrder() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			//HttpSession session = request.getSession(false);
			HttpSession session = request.getSession();
			JSONObject obj_err = new JSONObject();
			JSONArray array = new JSONArray();
			if (session != null) {
				//String userId = session.getAttribute("user_id").toString();
				JSONObject input = RpcHelper.readJSONObject(request);
				String userId = input.getString("user_id");
				Set<Order> orders = connection.viewOrder(userId, true);
				
				for (Order order : orders) {
					JSONObject obj = order.toJSONObject();
					array.put(obj);
				}
				RpcHelper.writeJsonArray(response, array);
			} else {
				obj_err.put("status", "Invalid Session");
				response.setStatus(403);
				RpcHelper.writeJsonObject(response, obj_err);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			connection.close();
		}
	}

}
