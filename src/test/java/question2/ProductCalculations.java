package question2;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ProductCalculationsTest {

    private static final double DELTA = 0.001;

    @Test
    void GetVat_WithValidData_ReturnsCorrectValue() {
        ProductCalculations calculations = new ProductCalculations(3500);
        assertEquals(525.0, calculations.GetVat(), DELTA);
    }

    @Test
    void GetCommission_WithValidData_ReturnsCorrectValue() {
        ProductCalculations calculations = new ProductCalculations(3500);
        assertEquals(175.0, calculations.GetCommission(), DELTA);
    }

    @Test
    void GetTotal_WithValidData_ReturnsCorrectValue() {
        ProductCalculations calculations = new ProductCalculations(3500);
        assertEquals(4200.0, calculations.GetTotal(), DELTA);
    }
}
