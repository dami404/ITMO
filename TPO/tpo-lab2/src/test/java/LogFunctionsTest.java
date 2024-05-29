import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import levit104dami404.tpo.lab2.math.LogFunctions;


import static org.junit.jupiter.api.Assertions.*;
public class LogFunctionsTest {

    @ParameterizedTest(name = "{index}. Ln {0} is defined")
    @ValueSource(doubles = {
        1,
        2,
        3,
        4.12
    })
    void test1(double x) {
        double epsilon = 0.0001;
        double result = LogFunctions.ln(x, epsilon);
        double expected = Math.log(x);

        assertEquals(expected, result, epsilon);
    }

    @ParameterizedTest(name = "{index}. Ln {0} is not defined")
    @ValueSource(doubles = {
        0,
        -1
    })
    void test2(double x) {
        double epsilon = 0.0001;
        assertThrows(ArithmeticException.class, () -> LogFunctions.ln(x, epsilon));
    }

    @ParameterizedTest(name = "{index}. Log1 {0} is not defined")
    @ValueSource(doubles = {
        1
    })
    void test3(double x) {
        double epsilon = 0.0001;
        assertThrows(ArithmeticException.class, () -> LogFunctions.log(x,1, epsilon));
    }

    @ParameterizedTest(name = "{index}. Log2_Stub {0} is  defined")
    @ValueSource(doubles = {
        1
    })
    void test4(double x) {
        double epsilon = 0.0001;
        double result = LogFunctions.logStub(x, 2, epsilon);
        double expected = Math.log(x)/Math.log(2);

        assertEquals(expected, result, epsilon);
    }

    @ParameterizedTest(name = "{index}. Log2_Stub {0} is not defined")
    @ValueSource(doubles = {
        0, 
        -1,
    })
    void test5(double x) {
        double epsilon = 0.0001;
        assertThrows(ArithmeticException.class, () -> LogFunctions.log(x,1, epsilon));
    }
}
