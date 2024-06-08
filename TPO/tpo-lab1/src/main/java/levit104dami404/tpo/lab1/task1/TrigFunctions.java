package levit104dami404.tpo.lab1.task1;

public class TrigFunctions {
    private TrigFunctions() {
    }

    private static final int MAX_N = 100;

    public static double sin(double x) {
        double sin = x;
        double tmp = x;

        for (int i = 1; i < MAX_N; i++) {
            tmp *= -x * x / (2 * i * (2 * i + 1));
            sin += tmp;
        }

        return sin;
    }

    public static double cos(double x) {
        double cos = 1;
        double tmp = 1;

        for (int i = 1; i < MAX_N; i++) {
            tmp *= -x * x / (2 * i * (2 * i - 1));
            cos += tmp;
        }

        return cos;
    }

    public static double tan(double x) {
        if (Math.abs(x) % Math.PI == Math.PI / 2)
            throw new ArithmeticException("Тангенс не определён при x=%s".formatted(x));

        return sin(x) / cos(x);
    }
}
