import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import levit104dami404.tpo.lab2.math.TrigFunctions;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.*;

public class TrigFunctionsTest {

    @ParameterizedTest(name = "{index}. Cot {0} is defined")
    @ValueSource(doubles = {
        1,
        -1,
        2,
        3,
        4.12
    })
    void test1(double x) {
        double epsilon = 0.0001;
        double result = TrigFunctions.cot(x, epsilon);
        double expected = 1/Math.tan(x);

        assertEquals(expected, result, epsilon);
    }

    @ParameterizedTest(name = "{index}. Cot {0} is not defined")
    @ValueSource(doubles = {
        0,
        -PI,
        PI
    })
    void test2(double x) {
        double epsilon = 0.0001;
        assertThrows(ArithmeticException.class, () -> TrigFunctions.cot(x, epsilon));
    }
}
