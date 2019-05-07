package songchen.productsearch;

import android.os.Parcel;
import android.os.Parcelable;

public class Product implements Parcelable {
	private String[] imageUrl;
	private String title;
	private Price price;
	private String subtitle;
	private NameValuePair[] specifics;
	private Seller seller;
	private Boolean globalShipping;
	private Integer handlingTime;
	private String condition;
	private ReturnPolicy returnPolicy;
	private String shareUrl;

	public String[] getImageUrl() {
		return imageUrl;
	}

	public String getTitle() {
		return title;
	}

	public Price getPrice() {
		return price;
	}

	public String getSubtitle() {
		return subtitle;
	}

	public NameValuePair[] getSpecifics() {
		return specifics;
	}

	public Seller getSeller() {
		return seller;
	}

	public Boolean getGlobalShipping() {
		return globalShipping;
	}

	public Integer getHandlingTime() {
		return handlingTime;
	}

	public String getCondition() {
		return condition;
	}

	public ReturnPolicy getReturnPolicy() {
		return returnPolicy;
	}

	public String getShareUrl() {
		return shareUrl;
	}

	protected Product(Parcel in) {
		imageUrl = in.createStringArray();
		title = in.readString();
		price = in.readParcelable(Price.class.getClassLoader());
		subtitle = in.readString();
		specifics = toNameValuePairs(in.readParcelableArray(NameValuePair.class.getClassLoader()));
		seller = in.readParcelable(Seller.class.getClassLoader());
		globalShipping = in.readByte() != 0;
		handlingTime = in.readInt();
		condition = in.readString();
		returnPolicy = in.readParcelable(ReturnPolicy.class.getClassLoader());
		shareUrl = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(imageUrl);
		dest.writeString(title);
		dest.writeParcelable(price, flags);
		dest.writeString(subtitle);
		dest.writeParcelableArray(specifics, flags);
		dest.writeParcelable(seller, flags);
		dest.writeByte((byte)(globalShipping? 1 : 0));
		dest.writeInt(handlingTime);
		dest.writeString(condition);
		dest.writeParcelable(returnPolicy, flags);
		dest.writeString(shareUrl);
	}

	public String prettyHandlingTime() {
		if (handlingTime == null) {
			return null;
		}
		if (handlingTime < 2) {
			return handlingTime + " day";
		} else {
			return handlingTime + " days";
		}
	}


	public String getBrand() {
		if (specifics == null) {
			return null;
		}
		for (NameValuePair pair:specifics) {
			if (pair.getName().toLowerCase().equals("brand")) {
				return pair.getValue()[0];
			}
		}
		return null;
	}

	private static NameValuePair[] toNameValuePairs(Parcelable[] parcelables) {
		NameValuePair[] pairs = new NameValuePair[parcelables.length];
		for (int i=0; i<pairs.length; i++) {
			pairs[i] = (NameValuePair)parcelables[i];
		}
		return pairs;
	}

	public static final Creator<Product> CREATOR = new Creator<Product>() {
		@Override
		public Product createFromParcel(Parcel in) {
			return new Product(in);
		}

		@Override
		public Product[] newArray(int size) {
			return new Product[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
}
