package entity;

import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Request {
    private String message;
    private String time;
    private String startDay;
    private String endDay;

    private Request(RequestBuilder builder) {
        // TODO Auto-generated constructor stub
        this.message = builder.message;
        this.time = builder.time;
        this.startDay = builder.startDay;
        this.endDay = builder.endDay;
    }

   
    public String getMessage() {
        return message;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("message", message);
            obj.put("time",time);
            obj.put("start_day",startDay);
            obj.put("end_day",endDay);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static class RequestBuilder {
        private String message;
        private String time;
        private String startDay;
        private String endDay;

        public RequestBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public RequestBuilder setTime(String t) {
            this.time = t;
            return this;
        }

        public RequestBuilder setStartDay(String t) {
            this.startDay = t;
            return this;
        }

        public RequestBuilder setEndDay(String t) {
            this.endDay = t;
            return this;
        }


        public Request build() {
            return new Request(this);
        }
    }

}

