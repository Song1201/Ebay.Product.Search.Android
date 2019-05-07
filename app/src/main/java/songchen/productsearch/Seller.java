package songchen.productsearch;

import android.os.Parcel;
import android.os.Parcelable;

public class Seller implements Parcelable {
	private String storeName;
	private String storeUrl;
	private Integer feedbackScore;
	private Double popularity;
	private String feedbackRatingStar;

	public String getStoreName() {
		return storeName;
	}

	public String getStoreUrl() {
		return storeUrl;
	}

	public Integer getFeedbackScore() {
		return feedbackScore;
	}

	public Double getPopularity() {
		return popularity;
	}

	public String getFeedbackRatingStar() {
		return feedbackRatingStar;
	}

	protected Seller(Parcel in) {
		storeName = in.readString();
		storeUrl = in.readString();
		feedbackScore = in.readInt();
		popularity = in.readDouble();
		feedbackRatingStar = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(storeName);
		dest.writeString(storeUrl);
		dest.writeInt(feedbackScore);
		dest.writeDouble(popularity);
		dest.writeString(feedbackRatingStar);
	}


	public static final Creator<Seller> CREATOR = new Creator<Seller>() {
		@Override
		public Seller createFromParcel(Parcel in) {
			return new Seller(in);
		}

		@Override
		public Seller[] newArray(int size) {
			return new Seller[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
}
