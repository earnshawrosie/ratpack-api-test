package ratpack.util;

import java.math.BigDecimal;

public class BigDecimalGenerator {

	public static BigDecimal generate() {
		BigDecimal max = new BigDecimal("5.05");
		BigDecimal min = new BigDecimal("50.75");
		BigDecimal range = max.subtract(min);
		return min.add(range.multiply(new BigDecimal(Math.random())));
	}
}
