package rpc;


import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Set;

import entity.Sitter;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import db.DBConnection;
import db.DBConnectionFactory;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Servlet implementation class Search
 */
@WebServlet("/search")
public class Search extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public Search() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String zipcode = request.getParameter("zipcode");
		Integer radius = Integer.parseInt(request.getParameter("radius"));
		String unit = request.getParameter("unit");
		String cityname = request.getParameter("cityname");
		DBConnection connection = DBConnectionFactory.getConnection();
		try {
			List<Sitter> sitters;
			if (cityname != null) {
				sitters = connection.searchByCityName(cityname);
			} else {
				sitters = connection.searchByZipcode(zipcode,radius,unit);
			}
			JSONArray array = new JSONArray();
			for (Sitter sitter : sitters) {
				JSONObject obj = sitter.toJSONObject();
				array.put(obj);
			}
			RpcHelper.writeJsonArray(response, array);
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
