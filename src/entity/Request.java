package entity;

import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Request {
    private String message;

    private Request(RequestBuilder builder) {
        // TODO Auto-generated constructor stub
        this.message = builder.message;
    }

   
    public String getMessage() {
        return message;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("message", message);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static class RequestBuilder {
        private String message;

        public RequestBuilder setMessage(String message) {
            this.message = message;
            return this;
        }

        public Request build() {
            return new Request(this);
        }
    }

}

