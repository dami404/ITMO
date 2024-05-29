package levit104dami404.tpo.lab2.math;

public class TrigFunctions {
    private TrigFunctions() {
    }

    public static double sin(double x, double eps) {
        double sin = x;
        double tmp = x;

        int i = 1;
        while (Math.abs(tmp) > eps) {
            tmp *= -x * x / (2 * i * (2 * i + 1));
            sin += tmp;
            i++;
        }

        return sin;
    }

    public static double cos(double x, double eps) {
        return sin(Math.PI / 2 + x, eps);
    }


    public static double cot(double x, double eps) {
        if (Math.abs(x) % Math.PI == 0)
            throw new ArithmeticException("Котангенс не определён при x=%s".formatted(x));

        return cos(x, eps) / sin(x, eps);
    }
}
