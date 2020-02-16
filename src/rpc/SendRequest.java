package rpc;

import db.DBConnection;
import db.DBConnectionFactory;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.Date;

/**
 * Servlet implementation class Logout
 */
@WebServlet("/sendRequest")
public class SendRequest extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SendRequest() {
        super();
        // TODO Auto-generated constructor stub
    }

    /**
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        doPost(request, response);
    }

    /**
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
     */
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // TODO Auto-generated method stub
        String userId = request.getParameter("owner_id");
        String sitterId = request.getParameter("sitter_id");
        String message = request.getParameter("message");
        String sd = request.getParameter("start_day");
        Date startDay = Date.valueOf(sd);
        String ed = request.getParameter("end_day");
        Date endDay = Date.valueOf(ed);
        DBConnection connection = DBConnectionFactory.getConnection();
        JSONObject obj = new JSONObject();
        try {
            String msg = connection.sendRequest(userId, sitterId, message, startDay, endDay);
            obj.put("set_unavailableTime_status", msg);
            RpcHelper.writeJsonObject(response, obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }

    }
}
