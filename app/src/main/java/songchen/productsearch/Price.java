package songchen.productsearch;

import android.os.Parcel;
import android.os.Parcelable;

public class Price implements Parcelable {
	private double value;
	private String currencyId;

	public Price(String currencyId, double value) {
		this.currencyId = currencyId;
		this.value = value;
	}

	public double getValue() {
		return value;
	}

	public String getCurrencyId() {
		return currencyId;
	}

	protected Price(Parcel in) {
		value = in.readDouble();
		currencyId = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeDouble(value);
		dest.writeString(currencyId);
	}

	@Override
	public int describeContents() {
		return 0;
	}

	public static final Creator<Price> CREATOR = new Creator<Price>() {
		@Override
		public Price createFromParcel(Parcel in) {
			return new Price(in);
		}

		@Override
		public Price[] newArray(int size) {
			return new Price[size];
		}
	};

	@Override
	public String toString() {
		if (currencyId.equals("USD")) {
			return "$"+String.format("%.2f", value);
		} else {
			return String.format("%.2f", value) + " " + currencyId;
		}
	}

	public void add(double added) {
		value += added;
	}
}
