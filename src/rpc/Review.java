package rpc;

import db.DBConnection;
import db.DBConnectionFactory;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.sql.Date;

/**
 * Servlet implementation class Comment
 */
@WebServlet("/comment")
public class Comment extends HttpServlet {

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Comment() {
        super();
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
        String orderId = request.getParameter("order_id");
        String sitterId = request.getParameter("sitter_id");
        String userId = request.getParameter("owner_id");
        Double score = 0.0;
        if (request.getParameter("score") != null) {
            score = Double.valueOf(request.getParameter("score"));
        }
        String comment = request.getParameter("comment");
        DBConnection connection = DBConnectionFactory.getConnection();
        JSONObject obj = new JSONObject();
        try {
            String msg = connection.makeComment(orderId, sitterId, userId, score, comment);
            obj.put("make_comment_status", msg);
            RpcHelper.writeJsonObject(response, obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
