package ratpack.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.BinaryOperator;

/**
 * Holds balance details for a user.
 *
 */
public class Balance implements Serializable {

	private static final long serialVersionUID = 7047422310070684300L;
	
	private AtomicReference<BigDecimal> balance = new AtomicReference<>();
	private String currency;
	
	public Balance(BigDecimal balance, String currency) {
		super();
		this.balance.set(balance);
		this.currency = currency;
	}

	public BigDecimal getBalance() {
		return balance.get().setScale(2, RoundingMode.HALF_UP);
	}

	public void setBalance(BigDecimal balance) {
		this.balance.set(balance);
	}

	public String getCurrency() {
		return currency;
	}

	public void setCurrency(String currency) {
		this.currency = currency;
	}

	public void debit(BigDecimal amount) {
		BinaryOperator<BigDecimal> decrement = (u, v) -> u.subtract(v);
		this.balance.getAndAccumulate(amount, decrement);
	}

	@Override
	public String toString() {
		return "Balance [balance=" + balance + ", currency=" + currency + "]";
	}

}
