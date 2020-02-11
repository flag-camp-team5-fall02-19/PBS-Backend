package entity;
import java.util.List;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sitter {
	private String sitterId;
	private String firstName;
	private String lastName;
	private String tel;
	private String email;
	private Integer zipcode;
	private String city;
	private String address;
	private Set<Post> imageUrl;
	private Set<String> reviews;
	private Double reviewScore;
	private Double price;
	
	private Sitter(SitterBuilder builder) {
		// TODO Auto-generated constructor stub
		this.sitterId = builder.sitterId;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.tel = builder.tel;
		this.email=builder.email;
		this.zipcode = builder.zipcode;
		this.city = builder.city;
		this.address = builder.address;
		this.imageUrl = builder.imageUrl;
		this.reviews = builder.reviews;
		this.reviewScore = builder.reviewScore;
		this.price = builder.price;

	}
	public String getSitterId() {
		return sitterId;
	}
	public String getFirstName() {
		return firstName;
	}
	public String getLastName() {
		return lastName;
	}
	public String getTel() { return tel; }
	public String getEmail() {
		return email;
	}
	public Integer getZipcode() {
		return zipcode;
	}
	public String getCity() { return city; }
	public String getAddress() { return address; }
	public Set<Post> getImageUrl() { return imageUrl; }
//	public Boolean getAvailability() { return availability; }
	public Set<String> getReviews() {
		return reviews;
	}
	public double getReviewScore() {
		return reviewScore;
	}
	public double getPrice() {
		return price;
	}

	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("sitter_id", sitterId);
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
			obj.put("reviews", new JSONArray(reviews));
			obj.put("review_score", reviewScore);
			obj.put("price", price);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static class SitterBuilder {
		private String sitterId;
		private String firstName;
		private String lastName;
		private String tel;
		private String email;
		private Integer zipcode;
		private String city;
		private String address;
		private Set<Post> imageUrl;
		private Set<String> reviews;
		private Double reviewScore;
		private double price;
		
		public SitterBuilder setSitterId(String sitterId) {
			this.sitterId = sitterId;
			return this;
		}
		public SitterBuilder setFirstName(String firstName) {
			this.firstName = firstName;
			return this;
		}
		public SitterBuilder setLastName(String lastName) {
			this.lastName = lastName;
			return this;
		}

		public SitterBuilder setTel(String tel) {
			this.tel = tel;
			return this;
		}

		public SitterBuilder setEmail(String email) {
			this.email = email;
			return this;
		}

		public SitterBuilder setZipcode(Integer zipcode) {
			this.zipcode = zipcode;
			return this;
		}

		public SitterBuilder setAddress(String address) {
			this.address = address;
			return this;
		}

		public SitterBuilder setCity(String city) {
			this.city= city;
			return this;
		}

		public SitterBuilder setImageUrl(Set<Post> imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}

//		public SitterBuilder setAvailability(Boolean availability) {
//			this.availability = availability;
//			return this;
//		}
		public SitterBuilder getReviews(Set<String> reviews) {
			this.reviews = reviews;
			return this;
		}

		public SitterBuilder setReviewScore(Double reviewScore) {
			this.reviewScore = reviewScore;
			return this;
		}

		public SitterBuilder setPrice(Double price) {
			this.price = price;
			return this;
		}

		public Sitter build() {
			return new Sitter(this);
		}
	}
	
}

