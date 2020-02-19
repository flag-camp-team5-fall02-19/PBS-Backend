package rpc;

import java.io.IOException;
import java.util.List;
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
import entity.Owner;


/**
 * It’s called by a sitter when he wants to see all owners who sent requests to him and the requests information.
 * Only request with status True (acceptable) will be shown.
 */
@WebServlet("/viewowner")
public class ViewOwner extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ViewOwner() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			HttpSession session = request.getSession(false);
			JSONObject obj_err = new JSONObject();
			JSONArray array = new JSONArray();
			if (session != null) {
				String userId = session.getAttribute("user_id").toString();
				Set<Owner> owners = connection.viewOwners(userId);
				
				for (Owner owner : owners) {
					JSONObject obj = owner.toJSONObject();
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

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		// TODO Auto-generated method stub
		doGet(request, response);
	}

}
