package entity;

import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Order {
	private String order_id;
    private String owner_firstName;
    private String owner_lastName;
    private String sitter_firstName;
    private String sitter_lastName;
    private Boolean status;

    private Order(OrderBuilder builder) {
        // TODO Auto-generated constructor stub
    	this.order_id = builder.order_id;
        this.owner_firstName = builder.owner_firstName;
        this.owner_lastName = builder.owner_lastName;
        this.sitter_firstName = builder.sitter_firstName;
        this.sitter_lastName = builder.sitter_lastName;
        this.status = builder.status;
    }
    
    public String getOrderId() {
    	return order_id;
    }

    public String getOwnerFirstName() {
        return owner_firstName;
    }
    public String getOwnerLastName() {
        return owner_lastName;
    }
    public String getSitterFirstName() {
        return sitter_firstName;
    }
    public String getSitterLastName() {
        return sitter_lastName;
    }
    public Boolean getStatus() {
    	return status;
    }
    

    public JSONObject toJSONObject() {
        JSONObject obj = new JSONObject();
        try {
        	obj.put("order_id", order_id);
            obj.put("owner_firstName", owner_firstName);
            obj.put("owner_lastName", owner_lastName);
            obj.put("sitter_firstName", sitter_firstName);
            obj.put("sitter_lastName", sitter_lastName);
            obj.put("order_status", status);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return obj;
    }

    public static class OrderBuilder {
    	private String order_id;
        private String owner_firstName;
        private String owner_lastName;
        private String sitter_firstName;
        private String sitter_lastName;
        private Boolean status;
        
        public OrderBuilder setOrderId(String order_id) {
        	this.order_id = order_id;
        	return this;
        }

        public OrderBuilder setOwnerFirstName(String owner_firstName) {
            this.owner_firstName = owner_firstName;
            return this;
        }
        public OrderBuilder setOwnerLastName(String owner_lastName) {
            this.owner_lastName = owner_lastName;
            return this;
        }
        public OrderBuilder setSitterFirstName(String sitter_firstName) {
            this.sitter_firstName = sitter_firstName;
            return this;
        }
        public OrderBuilder setSitterLastName(String sitter_lastName) {
            this.sitter_lastName = sitter_lastName;
            return this;
        }
        public OrderBuilder setOrderStatus(Boolean status) {
            this.status = status;
            return this;
        }

        public Order build() {
            return new Order(this);
        }
    }

}

