package songchen.productsearch;

import android.os.Parcel;
import android.os.Parcelable;

public class NameValuePair implements Parcelable {
	private String name;
	private String[] value;

	public String getName() {
		return name;
	}

	public String[] getValue() {
		return value;
	}

	protected NameValuePair(Parcel in) {
		name = in.readString();
		value = in.createStringArray();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(name);
		dest.writeStringArray(value);
	}

	public static final Creator<NameValuePair> CREATOR = new Creator<NameValuePair>() {
		@Override
		public NameValuePair createFromParcel(Parcel in) {
			return new NameValuePair(in);
		}

		@Override
		public NameValuePair[] newArray(int size) {
			return new NameValuePair[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
}
