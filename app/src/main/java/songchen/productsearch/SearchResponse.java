package songchen.productsearch;

public class SearchResponse extends BackendResponse {
	private Item[] items;

	public Item[] getItems() {
		return items;
	}

	@Override
	public String toString() {
		if (ack.equals("Failure")) {
			return super.toString();
		}
		String str = "ack: " + ack + "\n";
		str += "items:";
		for (Item item:items) {
			str += "\n" + item.toString();
		}
		return str;
	}
}
