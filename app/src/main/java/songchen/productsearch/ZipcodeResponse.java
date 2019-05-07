package songchen.productsearch;

import java.util.List;

public class ZipcodeResponse extends BackendResponse {
	private List<String> zips;

	public List<String> getZips() {
		return zips;
	}

	@Override
	public String toString() {
		if (ack.equals("Success")) {
			return "ack: " + ack + ", zips: " + zips;
		} else {
			return super.toString();
		}
	}
}
