import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;

public class CuckooSearch {

    private int populationSize; // Liczba potencjalnych rozwiązań problemu
    private double pa; // Prawdopodobieństwo z jakim kukułki będą próbowały zastąpić gniazda innych kukułek
    private double alpha; // Skala kroku, którym poruszają się kukułki
    private double[] lb; // Dolna granica przedziału poszukiwań optymalnego rozwiązania
    private double[] ub; // Górna granica przedziału poszukiwań optymalnego rozwiązania
    private int maxIterations; // Maksymalna liczba iteracji algorytmu
    private Random random; // Obiekt generatora liczb losowych

    private String nameFunction="Twoja Funkcja", bestSolution="", optimum="";
    private double fitness=0;
    private int mode=0;

    public String getNameFunction() {
        return nameFunction;
    }

    public String getBestSolution() {
        return bestSolution;
    }

    public double getFitness() {
        return fitness;
    }

    public String getOptimum() {
        return optimum;
    }

    public CuckooSearch(int populationSize, double pa, double alpha, double[] lb, double[] ub, int maxIterations) {
        this.populationSize = populationSize;
        this.pa = pa;
        this.alpha = alpha;
        this.lb = lb;
        this.ub = ub;
        this.maxIterations = maxIterations;
        this.random = new Random(); // Inicjalizacja generatora liczb losowych
    }

    public double[] run() {
        // Inicjalizacja populacji
        double[][] population = new double[populationSize][lb.length];
        for (int i = 0; i < populationSize; i++) {
            for (int j = 0; j < lb.length; j++) {
                population[i][j] = lb[j] + random.nextDouble() * (ub[j] - lb[j]);
            }
        }

        // Sortowanie populacji według wartości funkcji celu
        sortPopulation(population);

        // Inicjalizacja najlepszego rozwiązania
        double[] bestSolution = population[0];

        // Wykonywanie iteracji
        for (int iteration = 1; iteration <= maxIterations; iteration++) {
            // Generowanie nowej populacji
            double[][] newPopulation = new double[populationSize][lb.length];
            for (int i = 0; i < populationSize; i++) {
                // Generowanie nowego rozwiązania
                double[] newSolution = new double[lb.length];
                for (int j = 0; j < lb.length; j++) {
                    double r = random.nextDouble();
                    if (r < pa) {
                        // Generowanie nowego rozwiązania za pomocą lotów Levy'ego
                        double sigma = Math.pow((gamma(1.0 + alpha) * Math.sin(Math.PI * alpha / 2.0) / (gamma((1.0 + alpha) / 2.0) * alpha * Math.pow(2.0, (alpha - 1.0) / 2.0))), 1.0 / alpha);
                        double u = random.nextGaussian() * sigma;
                        double v = random.nextGaussian() * sigma;
                        double step = u / Math.pow(Math.abs(v), 1.0 / alpha);
                        double newValue = population[i][j] + step;
                        newSolution[j] = clamp(newValue, lb[j], ub[j]);
                    } else {
                        // Generowanie nowego rozwiązania za pomocą losowego przesunięcia
                        int cuckooIndex = random.nextInt(populationSize);
                        double[] cuckoo = population[cuckooIndex];
                        double stepSize = random.nextDouble() * alpha;
                        double[] step = new double[lb.length];
                        for (int k = 0; k < lb.length; k++) {
                            step[k] = stepSize * (cuckoo[k] - population[i][k]);
                        }
                        double[] newValue = new double[lb.length];  // obliczanie wartości funkcji celu dla nowego rozwiązania
                        for (int k = 0; k < lb.length; k++) {
                            newValue[k] = population[i][k] + step[k];
                        }
                        newSolution[j] = clamp(newValue[j], lb[j], ub[j]);
                    }
                }
                newPopulation[i] = newSolution;
            }

            //
            sortPopulation(newPopulation);


            for (int i = populationSize - 1; i >= populationSize / 2; i--) {
                population[i] = newPopulation[populationSize - 1 - i];
            }

            // Sort the population by fitness
            sortPopulation(population);

            // Update the best solution
            if (fitness(population[0]) < fitness(bestSolution)) {
                bestSolution = population[0];
            }

            // Wyświetlanie najlepszego wyniku
            //System.out.println("Iteration " + iteration + ": " + fitness(bestSolution));
        }

        return bestSolution;
    }

    // Sortuje populację według wartości funkcji dopasowania w porządku rosnącym
    private void sortPopulation(double[][] population) {
        Arrays.sort(population, Comparator.comparingDouble(this::fitness));
    }

    // Zwraca wartość funkcji dopasowania rozwiązania
    private double fitness(double[] solution) {
        double x = solution[0];
        double y = solution[1];
        return Math.pow(x, 2) + Math.pow(y, 2);
    }

    // Zwraca losową wartość z rozkładu normalnego
    private double randn() {
        return random.nextGaussian();
    }

    // Zwraca wartość funkcji gamma dla x
    private double gamma(double x) {
        return Math.exp(lgamma(x));
    }

    // Zwraca wartość funkcji logarytmu gamma dla x
    private double lgamma(double x) {
        double[] coef = {0.99999999999980993, 676.5203681218851, -1259.1392167224028,
                771.32342877765313, -176.61502916214059, 12.507343278686905,
                -0.13857109526572012, 9.9843695780195716e-6, 1.5056327351493116e-7};
        double sum = coef[0];
        for (int i = 1; i < coef.length; i++) {
            sum += coef[i] / (x + i);
        }
        return Math.log(sum * Math.sqrt(2 * Math.PI)) - (x + 0.5) * Math.log(x + 5.5) + (x + 5.5);
    }

    // Ogranicza wartość do przedziału
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }

    public void run(int mode) {
        this.mode = mode;
        set();
        CuckooSearch cuckooSearch = new CuckooSearch(populationSize, pa, alpha, lb, ub, maxIterations);
        double[] solution = cuckooSearch.run();

        bestSolution = Arrays.toString(solution);
        fitness = cuckooSearch.fitness(solution);
    }

    // Ustawia wartości dla odpowiedniego trybu
    private void set(){
        if(mode == 1){
            nameFunction = "Funkcja Rosenbrocka";
            lb = new double[]{-2.048, -2.048};
            ub = new double[]{2.048, 2.048};
            optimum="Optimum: [1.0, 1.0]";

        }
        if(mode == 2){
            nameFunction = "Funkcja Bootha";
            lb = new double[]{-10.0, -10.0};
            ub = new double[]{10.0, 10.0};
            optimum="Optimum: [1.0, 3.0]";
        }
        if(mode == 3){
            nameFunction = "Funkcja Ackleya";
            lb = new double[]{-32.0, -32.0};
            ub = new double[]{32.0, 32.0};
            optimum="Optimum: [0.0, 0.0]";
        }
        if(mode == 4){
            nameFunction = "Funkcja Rastrigina";
            lb = new double[]{-5.12, -5.12};
            ub = new double[]{5.12, 5.12};
            optimum="Optimum: [0.0, 0.0]";
        }
    }
}
