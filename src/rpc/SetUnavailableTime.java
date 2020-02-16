package rpc;

import java.io.IOException;
import java.sql.Date;
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

@WebServlet("/setUnavailableTime")
public class SetUnavailableTime extends HttpServlet {
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public SetUnavailableTime() {
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
        String sd = request.getParameter("startDay");
        Date startDay = Date.valueOf(sd);
        String ed = request.getParameter("endDay");
        Date endDay = Date.valueOf(ed);
        String ID = request.getParameter("sitter_id");
        DBConnection connection = DBConnectionFactory.getConnection();
        JSONObject obj = new JSONObject();
        try {
            String msg = connection.setUnavailableTime(startDay, endDay, ID);
            obj.put("set_unavailableTime_status", msg);
            RpcHelper.writeJsonObject(response, obj);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            connection.close();
        }
    }
}
