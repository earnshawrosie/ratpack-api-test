package ratpack.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Holds details of a transaction.
 *
 */
public class Transaction implements Serializable {

	private static final long serialVersionUID = 6290111732258196542L;
	
	private String date;
	private String description;
	private BigDecimal amount;
	private String currency;

	public Transaction() {
		super();
	}
	
	public Transaction(String date, String description, BigDecimal amount, String currency) {
		super();
		this.date = date;
		this.description = description;
		this.amount = amount;
		this.currency = currency;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public BigDecimal getAmount() {
		return amount;
	}

	public void setAmount(BigDecimal amount) {
		this.amount = amount;
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@Override
	public String toString() {
		return "Transaction [date=" + date + ", description=" + description + ", amount=" + amount + ", currency="
				+ currency + "]";
	}

}
