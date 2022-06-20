package ratpack.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

/*
 * User account details
 */
public class User implements Serializable {

	private static final long serialVersionUID = 8223363198171410812L;
	
	private String id;
	private Balance balance;
	private List<Transaction> transactions = Collections.synchronizedList(new ArrayList<Transaction>());

	public User(final Balance balance) {
		this.id = UUID.randomUUID().toString();
		this.balance = balance;
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Balance getBalance() {
		return balance;
	}

	public void setBalance(Balance balance) {
		this.balance = balance;
	}

	public List<Transaction> getTransactions() {
		return transactions;
	}

	public void setTransactions(List<Transaction> transactions) {
		this.transactions = transactions;
	}
	
	public void addTransaction(Transaction tx) {
		transactions.add(tx);
	}

	@Override
	public String toString() {
		return "User [id=" + id + ", balance=" + balance + ", transactions=" + transactions + ", getId()=" + getId()
				+ ", getBalance()=" + getBalance() + ", getTransactions()=" + getTransactions() + ", getClass()="
				+ getClass() + ", hashCode()=" + hashCode() + ", toString()=" + super.toString() + "]";
	}
}
