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
 * It is called when a sitter accepts the request from the owner.
 * It will: 1) add a new order to the orders table,
 * 2) change the status of the request into False, which means the request could no longer be accepted,
 * 3) change the status of other requests sent by the same owner and time conflict with this request into False,
 * 4) change the status of other requests sent to the same sitter and time conflict with this request into False.
 */
@WebServlet("/confirmRequest")
public class ConfirmRequest extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public ConfirmRequest() {
        super();
        // TODO Auto-generated constructor stub.
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
        DBConnection connection = DBConnectionFactory.getConnection();
        try {
            String request_id = request.getParameter("request_id");
            String owner_id = request.getParameter("owner_id");
            String user_id = request.getParameter("user_id");
            JSONObject obj = new JSONObject();
            String msg = connection.confirmRequest(owner_id,user_id,request_id);
            obj.put("confirmRequest_status", msg);
            RpcHelper.writeJsonObject(response, obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
