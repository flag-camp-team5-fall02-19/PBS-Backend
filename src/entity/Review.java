package entity;

import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Review {
    private Integer score;
    private String comment;

    private Review(ReviewBuilder builder) {
        // TODO Auto-generated constructor stub
        this.score = builder.score;
        this.comment = builder.comment;
    }

    public Integer getInteger() {
        return score;
    }
    public String getComment() {
        return comment;
    }

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
            obj.put("score", score);
            obj.put("comment", comment);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static class ReviewBuilder {
        private Integer score;
        private String comment;

        public ReviewBuilder setScore(Integer score) {
            this.score = score;
            return this;
        }
        public ReviewBuilder setComment(String comment) {
            this.comment = comment;
            return this;
        }

        public Review build() {
            return new Review(this);
        }
    }

}
