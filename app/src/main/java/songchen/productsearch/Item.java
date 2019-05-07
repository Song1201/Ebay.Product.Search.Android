package songchen.productsearch;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
	private String itemId;
	private String imageUrl;
	private String title;
	private Price price;
	private Shipping shipping;
	private String zip;
	private String condition;
	private Integer daysLeft;
	private String viewItemUrl;

	public String getTitle() {
		return title;
	}

	public String getImageUrl() {
		return imageUrl;
	}

	public String getZip() {
		return zip;
	}

	public Shipping getShipping() {
		return shipping;
	}

	public String getCondition() {
		if (condition == null) {
			return "Condition: N/A";
		}
		return condition;
	}

	public Price getPrice() {
		return price;
	}

	public String getItemId() {
		return itemId;
	}

	public Integer getDaysLeft() {
		return daysLeft;
	}

	public String getViewItemUrl() {
		return viewItemUrl;
	}

	@Override
	public String toString() {
		return "itemId: " + itemId + ", imageUrl: " + imageUrl + ", title: " +  title
			+ ", price: " + price + ", shipping: " + shipping.getCost() + ", zip: "
			+ zip + ", condition: " + condition;
	}

	protected Item(Parcel in) {
		itemId = in.readString();
		imageUrl = in.readString();
		title = in.readString();
		price = in.readParcelable(Price.class.getClassLoader());
		shipping = in.readParcelable(Shipping.class.getClassLoader());
		zip = in.readString();
		condition = in.readString();
		daysLeft = (Integer) in.readSerializable();
		viewItemUrl = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(itemId);
		dest.writeString(imageUrl);
		dest.writeString(title);
		dest.writeParcelable(price, flags);
		dest.writeParcelable(shipping, flags);
		dest.writeString(zip);
		dest.writeString(condition);
		dest.writeSerializable(daysLeft);
		dest.writeString(viewItemUrl);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Item> CREATOR = new Creator<Item>() {
		@Override
		public Item createFromParcel(Parcel in) {
			return new Item(in);
		}

		@Override
		public Item[] newArray(int size) {
			return new Item[size];
		}
	};
}
