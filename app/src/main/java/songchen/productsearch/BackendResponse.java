package songchen.productsearch;

public class BackendResponse {
	protected String ack;
	protected String errorId;
	protected String message;

	public String getAck() {
		return ack;
	}

	public String getErrorId() {
		return errorId;
	}

	public String getMessage() {
		return message;
	}

	@Override
	public String toString() {
		return "ack: " + this.ack + ", errorId: " + this.errorId + ", message: " + this.message;
	}
}
