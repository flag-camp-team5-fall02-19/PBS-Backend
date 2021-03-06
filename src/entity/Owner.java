package entity;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Owner {
	private String ownerId;
	private String firstName;
	private String lastName;
	private String tel;
	private String email;
	private String zipcode;
	private String city;
	private String address;
	private Set<Post> imageUrl;
	private Set<Request> requests;
	private String petType;
	private String petDescription;
	
	
	private Owner(OwnerBuilder builder) {
		// TODO Auto-generated constructor stub
		this.ownerId = builder.ownerId;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.tel = builder.tel;
		this.email=builder.email;
		this.zipcode = builder.zipcode;
		this.city = builder.city;
		this.address = builder.address;
		this.imageUrl = builder.imageUrl;
		this.requests = builder.requests;
		this.petType = builder.petType;
		this.petDescription = builder.petDescription;
		

	}
	public String getOwnerId() {
		return ownerId;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getTel() { 
		return tel; 
	}
	public String getEmail() {
		return email;
	}
	public String getZipcode() {
		return zipcode;
	}
	public String getCity() { 
		return city; 
	}
	public String getAddress() { 
		return address; 
	}
	public Set<Post> getImageUrl() { 
		return imageUrl; 
	}
//	public Boolean getAvailability() { return availability; }
	public Set<Request> getRequests() {
		return requests;
	}
	public String getPetType() {
		return petType;
	}
	public String getPetDescription() {
		return petDescription;
	}
	

	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("owner_id", ownerId);
			obj.put("first_name", firstName);
			obj.put("last_name", lastName);
			obj.put("tel", tel);
			obj.put("email", email);
			obj.put("zipcode", zipcode);
			obj.put("city", city);
			obj.put("address", address);
			JSONArray posts = new JSONArray();
			for (Post p : imageUrl) {
				JSONObject p_obj=p.toJSONObject();
				posts.put(p_obj);
			}
			obj.put("images", posts);
//			obj.put("sitter_availability", availability);
			JSONArray rqst = new JSONArray();
			for (Request r : requests) {
				JSONObject r_obj=r.toJSONObject();
				rqst.put(r_obj);
			}
			obj.put("requests", rqst);
			obj.put("pet_type", petType);
			obj.put("pet_description", petDescription);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static class OwnerBuilder {
		private String ownerId;
		private String firstName;
		private String lastName;
		private String tel;
		private String email;
		private String zipcode;
		private String city;
		private String address;
		private Set<Post> imageUrl;
		private Set<Request> requests;
		private String petType;
		private String petDescription;
		
		
		public OwnerBuilder setOwnerId(String ownerId) {
			this.ownerId = ownerId;
			return this;
		}
		public OwnerBuilder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		public OwnerBuilder setLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public OwnerBuilder setTel(String tel) {
			this.tel = tel;
			return this;
		}

		public OwnerBuilder setEmail(String email) {
			this.email = email;
			return this;
		}

		public OwnerBuilder setZipcode(String zipcode) {
			this.zipcode = zipcode;
			return this;
		}

		public OwnerBuilder setAddress(String address) {
			this.address = address;
			return this;
		}

		public OwnerBuilder setCity(String city) {
			this.city= city;
			return this;
		}

		public OwnerBuilder setImageUrl(Set<Post> imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}

//		public SitterBuilder setAvailability(Boolean availability) {
//			this.availability = availability;
//			return this;
//		}
		public OwnerBuilder setRequests(Set<Request> requests) {
			this.requests = requests;
			return this;
		}
		
		public OwnerBuilder setPetType(String petType) {
			this.petType = petType;
			return this;
		}
		
		public OwnerBuilder setPetDescription(String petDescription) {
			this.petDescription = petDescription;
			return this;
		}


		public Owner build() {
			return new Owner(this);
		}
	}
	
}

