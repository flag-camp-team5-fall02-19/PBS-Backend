package entity;

import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Post {
    private String url;
    private String caption;

    private Post(PostBuilder builder) {
        // TODO Auto-generated constructor stub
        this.url = builder.url;
        this.caption = builder.caption;
    }

    public String getUrl() {
        return url;
    }
    public String getCaption() {
        return caption;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("URL", url);
            obj.put("caption", caption);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static class PostBuilder {
        private String url;
        private String caption;

        public PostBuilder setUrl(String url) {
            this.url = url;
            return this;
        }
        public PostBuilder setCaption(String caption) {
            this.caption = caption;
            return this;
        }

        public Post build() {
            return new Post(this);
        }
    }

}

