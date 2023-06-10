import java.util.Scanner;

public class Main {
    public static final double PI = 3.1415;
    public static final int ITERATIONS = 50;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        double a = 0;
        double b = 0;
        double fac = 1;
        System.out.println("Enter the number for x to compute sin(x) ");
        try {
            a = scanner.nextDouble();
        }catch (Exception e){
            System.err.println(e.getMessage());
        }

        double mul = 1;
        a = a % (PI * 2.0);
        if (a > PI) {
            mul = -1.0;
            a = 2 * PI - a;
        }
        if (a > 0.5 * PI) {
            a = PI - a;
        }

        for (int i = 0; i < ITERATIONS; i++) {
            if (i != 0) {
                fac *= i * 2;
                fac *= i * 2 + 1;
            }

            double c = Math.pow(a, (i * 2) + 1);
            if (i % 2 == 1) {
                b -= c / fac;
            } else {
                b += c / fac;
            }
        }

        b *= mul;

        System.out.println("Answer " + b);

    }
}
