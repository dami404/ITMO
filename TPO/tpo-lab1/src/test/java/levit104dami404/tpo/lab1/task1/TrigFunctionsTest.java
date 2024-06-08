package levit104dami404.tpo.lab1.task1;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.*;

public class TrigFunctionsTest {
    @ParameterizedTest(name = "{index}. tg({0}) is defined")
    @ValueSource(doubles = {
            0,
            2 * PI,
            -2 * PI,

            PI,
            -PI,

            PI / 4,
            -PI / 4,

            3 * PI / 4,
            -3 * PI / 4,

            5 * PI / 4,
            -5 * PI / 4,

            -PI / 2 - 10e-6,
            -PI / 2 + 10e-6,

            PI / 2 - 10e-6,
            PI / 2 + 10e-6,
    })
    void checkDefined(double x) {
        assertEquals(TrigFunctions.tan(x), Math.tan(x), 0.00001);
    }

    @ParameterizedTest(name = "{index}. tg({0}) is not defined")
    @ValueSource(doubles = {
            PI / 2,
            -PI / 2,

            3 * PI / 2,
            -3 * PI / 2,

            PI / 2 + 2 * PI,
            -PI / 2 - 2 * PI,

            3 * PI / 2 + 2 * PI,
            -3 * PI / 2 - 2 * PI
    })
    void checkNotDefined(double x) {
        assertThrows(ArithmeticException.class, () -> TrigFunctions.tan(x));
    }
}