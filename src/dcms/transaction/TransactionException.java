package dcms.transaction;

public class TransactionException extends Exception {

	private String message;
	
	public TransactionException(String message) {
		super(message);
		this.message = message;
	}
}
