package entity;
import java.util.Set;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class Sitter {
	private String sitterId;
	private String firstName;
	private String lastName;
	private Integer zipcode;
	private String address;
	private Boolean availability;
	private String imageUrl;
	private Set<String> reviews;
	private Double reviewScore;
	private double distance;
	
	private Sitter(SitterBuilder builder) {
		// TODO Auto-generated constructor stub
		this.sitterId = builder.sitterId;
		this.firstName = builder.firstName;
		this.lastName = builder.lastName;
		this.zipcode = builder.zipcode;
		this.address = builder.address;
		this.imageUrl = builder.imageUrl;
		this.availability = builder.availability;
		this.reviews = builder.reviews;
		this.reviewScore = builder.reviewScore;
		this.distance = builder.distance;

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
	public Integer getZipcode() {
		return zipcode;
	}
	public String getAddress() {
		return address;
	}
	public String getImageUrl() {
		return imageUrl;
	}
	public Boolean getAvailability() {
		return availability;
	}
	public Set<String> getReviews() {
		return reviews;
	}
	public double getReviewScore() {
		return reviewScore;
	}
	public double getDistance() {
		return distance;
	}
	
	public JSONObject toJSONObject() {
		JSONObject obj = new JSONObject();
		try {
			obj.put("sitter_id", sitterId);
			obj.put("first_name", firstName);
			obj.put("last_name", lastName);
			obj.put("zipcode", zipcode);
			obj.put("address", address);
			obj.put("image_url", imageUrl);
			obj.put("sitter_availability", availability);
			obj.put("reviews", new JSONArray(reviews));
			obj.put("review_score", reviewScore);
			obj.put("distance", distance);
		} catch (JSONException e) {
			e.printStackTrace();
		}
		return obj;
	}
	
	public static class SitterBuilder {
		private String sitterId;
		private String firstName;
		private String lastName;
		private Integer zipcode;
		private String address;
		private Boolean availability;
		private String imageUrl;
		private Set<String> reviews;
		private Double reviewScore;
		private double distance;
		
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
		public SitterBuilder setZipcode(Integer zipcode) {
			this.zipcode = zipcode;
			return this;
		}
		public SitterBuilder setAddress(String address) {
			this.address = address;
			return this;
		}
		public SitterBuilder setImageUrl(String imageUrl) {
			this.imageUrl = imageUrl;
			return this;
		}
		public SitterBuilder setAvailability(Boolean availability) {
			this.availability = availability;
			return this;
		}
		public SitterBuilder gsetReviews(Set<String> reviews) {
			this.reviews = reviews;
			return this;
		}
		public SitterBuilder setReviewScore(Double reviewScore) {
			this.reviewScore = reviewScore;
			return this;
		}
		public SitterBuilder setDistance(Double distance) {
			this.distance = distance;
			return this;
		}
		
		public Sitter build() {
			return new Sitter(this);
		}
	}
	
	
}

