import org.knowm.xchart.SwingWrapper;
import org.knowm.xchart.XYChart;
import org.knowm.xchart.XYChartBuilder;
import org.knowm.xchart.style.Styler;

public class Main {

    private static final double DT = 0.1;
    private static final double TMAX = 10;

    private static final double L = 1;
    private static final double G = 9.81;

    private static final double ALPHA = 0.1;
    private static final double OMEGA = 0;

    private static final int TIME_STEPS = (int) (TMAX / DT);

    private static final double[] TIME_DATA = new double[TIME_STEPS];
    private static final double[] ALPHA_IE_DATA = new double[TIME_STEPS];
    private static final double[] ALPHA_RK4_DATA = new double[TIME_STEPS];
    private static final double[] POTENTIAL_ENERGY_IE_DATA = new double[TIME_STEPS];
    private static final double[] KINETIC_ENERGY_IE_DATA = new double[TIME_STEPS];
    private static final double[] TOTAL_ENERGY_IE_DATA = new double[TIME_STEPS];
    private static final double[] POTENTIAL_ENERGY_RK4_DATA = new double[TIME_STEPS];
    private static final double[] KINETIC_ENERGY_RK4_DATA = new double[TIME_STEPS];
    private static final double[] TOTAL_ENERGY_RK4_DATA = new double[TIME_STEPS];

    private static final XYChart chart = new XYChartBuilder().width(800).height(600).title("Pendulum Motion").xAxisTitle("Time (s)").yAxisTitle("Angle (rad)").build();

    private static final XYChart energyChart = new XYChartBuilder().width(800).height(600).title("Pendulum Energy").xAxisTitle("Time (s)").yAxisTitle("Energy (J)").build();

    static {
        chart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);
        energyChart.getStyler().setLegendPosition(Styler.LegendPosition.InsideNW);

        double alphaIE = ALPHA;
        double omegaIE = OMEGA;
        double alphaRK4 = ALPHA;
        double omegaRK4 = OMEGA;

        for (int i = 0; i < TIME_STEPS; i++) {
            double t = i * DT;
            TIME_DATA[i] = t;

            double sinAlphaIE = Math.sin(alphaIE);
            double cosAlphaIE = Math.cos(alphaIE);
            double sinAlphaRK4 = Math.sin(alphaRK4);
            double cosAlphaRK4 = Math.cos(alphaRK4);

            double alphaPrimeIE = omegaIE;
            double omegaPrimeIE = -G / L * sinAlphaIE;
            alphaIE += DT / 2 * alphaPrimeIE;
            omegaIE += DT / 2 * omegaPrimeIE;
            alphaPrimeIE = omegaIE;
            omegaPrimeIE = -G / L * sinAlphaIE;
            alphaIE += DT / 2 * alphaPrimeIE;
            omegaIE += DT / 2 * omegaPrimeIE;

            double k1alpha = omegaRK4;
            double k1omega = -G / L * sinAlphaRK4;
            double k2alpha = omegaRK4 + DT / 2 * k1omega;
            double k2omega = -G / L * Math.sin(alphaRK4 + DT / 2 * k1alpha);
            double k3alpha = omegaRK4 + DT / 2 * k2omega;
            double k3omega = -G / L * Math.sin(alphaRK4 + DT / 2 * k2alpha);
            double k4alpha = omegaRK4 + DT * k3omega;
            double k4omega = -G / L * Math.sin(alphaRK4 + DT * k3alpha);
            alphaRK4 += DT / 6 * (k1alpha + 2 * k2alpha + 2 * k3alpha + k4alpha);
            omegaRK4 += DT / 6 * (k1omega + 2 * k2omega + 2 * k3omega + k4omega);
            ALPHA_IE_DATA[i] = alphaIE;
            ALPHA_RK4_DATA[i] = alphaRK4;

            double potentialEnergyIE = -G * L * cosAlphaIE;
            double kineticEnergyIE = 0.5 * L * L * omegaIE * omegaIE;
            double totalEnergyIE = potentialEnergyIE + kineticEnergyIE;
            POTENTIAL_ENERGY_IE_DATA[i] = potentialEnergyIE;
            KINETIC_ENERGY_IE_DATA[i] = kineticEnergyIE;
            TOTAL_ENERGY_IE_DATA[i] = totalEnergyIE;

            double potentialEnergyRK4 = -G * L * cosAlphaRK4;
            double kineticEnergyRK4 = 0.5 * L * L * omegaRK4 * omegaRK4;
            double totalEnergyRK4 = potentialEnergyRK4 + kineticEnergyRK4;
            POTENTIAL_ENERGY_RK4_DATA[i] = potentialEnergyRK4;
            KINETIC_ENERGY_RK4_DATA[i] = kineticEnergyRK4;
            TOTAL_ENERGY_RK4_DATA[i] = totalEnergyRK4;
        }
    }

        public static void main(String[] args) {
            chart.addSeries("Euler", TIME_DATA, ALPHA_IE_DATA);
            chart.addSeries("RK4", TIME_DATA, ALPHA_RK4_DATA);

            energyChart.addSeries("Euler: Potential Energy", TIME_DATA, POTENTIAL_ENERGY_IE_DATA);
            energyChart.addSeries("Euler: Kinetic Energy", TIME_DATA, KINETIC_ENERGY_IE_DATA);
            energyChart.addSeries("Euler: Total Energy", TIME_DATA, TOTAL_ENERGY_IE_DATA);
            energyChart.addSeries("RK4: Potential Energy", TIME_DATA, POTENTIAL_ENERGY_RK4_DATA);
            energyChart.addSeries("RK4: Kinetic Energy", TIME_DATA, KINETIC_ENERGY_RK4_DATA);
            energyChart.addSeries("RK4: Total Energy", TIME_DATA, TOTAL_ENERGY_RK4_DATA);

            SwingWrapper<XYChart> wrapper = new SwingWrapper<>(chart);
            wrapper.displayChart();

            SwingWrapper<XYChart> energyWrapper = new SwingWrapper<>(energyChart);
            energyWrapper.displayChart();
        }

    }

