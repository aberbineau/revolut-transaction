package fr.berbineau.transaction.data;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.math.BigDecimal;

import org.junit.jupiter.api.Test;

public class TransactionTest {

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionWhenAmountIsNegative() {
        BigDecimal negativeAmount = BigDecimal.valueOf(-2.0);
        assertThrows(IllegalArgumentException.class, () -> new Transaction(negativeAmount, 1L, 2L));
    }

    @Test
    public void constructorShouldThrowIllegalArgumentExceptionWhenAmountIsZero() {
        assertThrows(IllegalArgumentException.class, () -> new Transaction(BigDecimal.ZERO, 1L, 2L));
    }
}
