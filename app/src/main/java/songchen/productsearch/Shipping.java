package songchen.productsearch;

import android.os.Parcel;
import android.os.Parcelable;

public class Shipping implements Parcelable {
	private Price price;

	protected Shipping(Parcel in) {
		price = in.readParcelable(Price.class.getClassLoader());
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(price, flags);
	}

	public String getCost() {
		if (price != null ) {
			if (price.getValue() == 0) {
				return "Free Shipping";
			} else {
				return price.toString() + " Shipping";
			}
		} else {
			return "Shipping: N/A";
		}
	}

	public String getDetailCost() {
		if (price == null) {
			return null;
		} else if (price.getValue() == 0) {
			return "Free Shipping";
		} else {
			return price.toString();
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Shipping> CREATOR = new Creator<Shipping>() {
		@Override
		public Shipping createFromParcel(Parcel in) {
			return new Shipping(in);
		}

		@Override
		public Shipping[] newArray(int size) {
			return new Shipping[size];
		}
	};
}
