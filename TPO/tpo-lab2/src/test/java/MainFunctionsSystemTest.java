
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import levit104dami404.tpo.lab2.MainFunctionsSystem;

import static java.lang.Math.PI;
import static org.junit.jupiter.api.Assertions.*;
public class MainFunctionsSystemTest {

    @ParameterizedTest(name = "{index}. System {0} is defined")
    @ValueSource(doubles = {
        -1,
        -1.5,
        -2,
        -2.5,
        -3,
        1, // в обоих случаях NaN
        1.5,
        2,
        2.5,
        3
    })
    void test1(double x) {
        double epsilon = 0.0001;
        double result = MainFunctionsSystem.calculate(x, epsilon);
        double expected = MainFunctionsSystem.calculateExpected(x);
        assertEquals(expected, result, epsilon);
    }


    @ParameterizedTest(name = "{index}. System {0} is not defined")
    @ValueSource(doubles = {
        0,
        -PI
    })
    void test2(double x) {
        double epsilon = 0.0001;
        assertThrows(ArithmeticException.class, () -> MainFunctionsSystem.calculate(x, epsilon));
    }
}
