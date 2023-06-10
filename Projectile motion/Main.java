public class Main {
    public static void main(String[] args) {
        //we give g as a parameter because it can be different due to ellipsoid earth
        ProjectileMotionSimulation projectileMotionSimulation =
                new ProjectileMotionSimulation(9.8, 50, 45, 50, 0.1, 0.1, 30);
        projectileMotionSimulation.simulateUsingEulerMethod();
        System.out.println("\n\n\n\n\n");
        projectileMotionSimulation.simulateUsingMidpointMethod();
    }
}


class ProjectileMotionSimulation {
    private final double g;
    private final double velocity;
    private final double angle;
    private final double mass;
    private final double dragCoefficient;
    private final double stepOfTime;
    private final double maximumPossibleTime;
    private double x = 0;
    private double y = 0;
    private double t = 0;
    private double ax = 0;

    public ProjectileMotionSimulation(double g, double velocity, double angle,
                                      double mass, double dragCoefficient,
                                      double stepOfTime, double maximumPossibleTime) {
        this.g = g;
        this.velocity = velocity;
        this.angle = Math.toRadians(angle);
        this.mass = mass;
        this.dragCoefficient = dragCoefficient;
        this.stepOfTime = stepOfTime;
        this.maximumPossibleTime = maximumPossibleTime;
    }

    public void simulateUsingEulerMethod() {
        double vx = velocity * Math.cos(angle);
        double vy = velocity * Math.sin(angle);

        while (t <= maximumPossibleTime) {
            double v = Math.sqrt(vx * vx + vy * vy);
            double fx = -dragCoefficient * v * vx;
            double fy = -mass * g - dragCoefficient * v * vy;

            ax = fx / mass;
            double ay = fy / mass;

            x += vx * stepOfTime;
            y += vy * stepOfTime;
            vx += ax * stepOfTime;
            vy += ay * stepOfTime;
            t += stepOfTime;
            if (y <= 0) {
                break;
            }
            System.out.printf("t=%.4f x=%.4f y=%.4f \n", t, x, y);
        }
    }

    public void simulateUsingMidpointMethod() {
        double vx = velocity * Math.cos(angle);
        double vy = velocity * Math.sin(angle);
        double ay = -g;

        while (t <= maximumPossibleTime) {
            double v = Math.sqrt(vx * vx + vy * vy);

            double vx_half = vx + ax * stepOfTime / 2.0;
            double vy_half = vy + ay * stepOfTime / 2.0;
            double v_half = Math.sqrt(vx_half * vx_half + vy_half * vy_half);
            double fx_half = -dragCoefficient * v_half * vx_half;
            double fy_half = -mass * g - dragCoefficient * v_half * vy_half;

            ax = fx_half / mass;
            ay = fy_half / mass;

            x += vx * stepOfTime;
            y += vy * stepOfTime;
            vx += ax * stepOfTime;
            vy += ay * stepOfTime;
            t += stepOfTime;
            if (y <= 0) {
                break;
            }

            System.out.printf("t=%.4f x=%.4f y=%.4f \n", t, x, y);

        }
    }
}
