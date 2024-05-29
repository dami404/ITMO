package levit104dami404.tpo.lab2;

import levit104dami404.tpo.lab2.math.LogFunctions;
import levit104dami404.tpo.lab2.math.TrigFunctions;


public class MainFunctionsSystem {
    private MainFunctionsSystem() {
    }

    public static double calculate(double x, double eps) {
        if (x <= 0) {
            return TrigFunctions.cot(x, eps);
        } else {
            double log10x = LogFunctions.log(x, 10, eps);
            double log3x = LogFunctions.log(x, 3, eps);
            double log2x = LogFunctions.log(x, 2, eps);
            double lnx = LogFunctions.ln(x, eps);

            return Math.pow(Math.pow(((log10x / log3x) / log2x), 2) - (Math.pow(log2x, 3) + lnx), 3);
        }
    }

    public static double calculateExpected(double x){
        if (x <= 0) {
            return 1/Math.tan(x);
        } else {
            double log10x = Math.log10(x);
            double log3x = Math.log(x) / Math.log(3);
            double log2x = Math.log(x) / Math.log(2);
            double lnx = Math.log(x);

            return Math.pow(Math.pow(((log10x / log3x) / log2x), 2) - (Math.pow(log2x, 3) + lnx), 3);
        }
    }
}
