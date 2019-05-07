package songchen.productsearch;

import android.os.Parcel;
import android.os.Parcelable;

public class ReturnPolicy implements Parcelable {
	private String returnAccepted;
	private String returnWithin;
	private String refundMode;
	private String shipBy;

	public String getReturnAccepted() {
		return returnAccepted;
	}

	public String getReturnWithin() {
		return returnWithin;
	}

	public String getRefundMode() {
		return refundMode;
	}

	public String getShipBy() {
		return shipBy;
	}

	protected ReturnPolicy(Parcel in) {
		returnAccepted = in.readString();
		returnWithin = in.readString();
		refundMode = in.readString();
		shipBy = in.readString();
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(returnAccepted);
		dest.writeString(returnWithin);
		dest.writeString(refundMode);
		dest.writeString(shipBy);
	}


	public static final Creator<ReturnPolicy> CREATOR = new Creator<ReturnPolicy>() {
		@Override
		public ReturnPolicy createFromParcel(Parcel in) {
			return new ReturnPolicy(in);
		}

		@Override
		public ReturnPolicy[] newArray(int size) {
			return new ReturnPolicy[size];
		}
	};

	@Override
	public int describeContents() {
		return 0;
	}
}
