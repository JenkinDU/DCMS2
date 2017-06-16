package dcms.transaction;

public interface ITransaction {
	void doCommit() throws TransactionException ;
	void backCommit();
}
