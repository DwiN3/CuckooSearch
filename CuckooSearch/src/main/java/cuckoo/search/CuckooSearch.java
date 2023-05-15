package cuckoo.search;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Random;
import java.util.stream.IntStream;

public class CuckooSearch {
    private final int populationSize;
    private final double probability;
    private final double alpha;
    private final double[] lowerBorder;
    private final double[] upperBorder;
    private final int maxIterations;
    private final Random random;
    private double[][] population;

    private String nameFunction = "Twoja Funkcja";
    private String bestSolution = "";
    private String optimum = "";
    private double fitness = 0;
    private int mode = 0;

    public CuckooSearch(int populationSize,
                        double probability,
                        double alpha,
                        double[] lowerBorder,
                        double[] upperBorder,
                        int maxIterations) {

        this.populationSize = populationSize;
        this.probability = probability;
        this.alpha = alpha;
        this.lowerBorder = lowerBorder;
        this.upperBorder = upperBorder;
        this.maxIterations = maxIterations;
        this.random = new Random();
    }

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

    public double[] run() {
        initializePopulation();
        sortPopulation();
        double[] solution = population[0];

        for (var iteration = 1; iteration <= maxIterations; iteration++) {
            var newPopulation = generateNewPopulation();
            sortPopulation();
            updatePopulation(newPopulation);
            sortPopulation();

            if (calculateFitness(newPopulation[0]) < calculateFitness(solution))
                solution = newPopulation[0];
        }

        return solution;
    }

    private void initializePopulation() {
        population = new double[populationSize][lowerBorder.length];
        for (var i = 0; i < populationSize; i++) {
            for (var j = 0; j < lowerBorder.length; j++) {
                population[i][j] = lowerBorder[j] + random.nextDouble() * (upperBorder[j] - lowerBorder[j]);
            }
        }
    }

    private void sortPopulation() {
        Arrays.sort(population, Comparator.comparingDouble(this::calculateFitness));
    }

    private double[][] generateNewPopulation() {
        return Arrays.stream(new double[populationSize][lowerBorder.length])
                .map(this::generateNewSolution)
                .toArray(double[][]::new);
    }

    private double[] generateNewSolution(double[] solution) {
        return Arrays.stream(solution)
                .map(solutionElement -> {
                    var randomProbability = random.nextDouble();
                    if (randomProbability < probability) {
                        var sigma = calculateSigma();
                        var gaussianU = random.nextGaussian() * sigma;
                        var gaussianV = random.nextGaussian() * sigma;
                        var step = gaussianU / Math.pow(Math.abs(gaussianV), 1.0 / alpha);
                        var newValue = solutionElement + step;
                        return clamp(newValue, lowerBorder[0], upperBorder[0]);
                    } else {
                        var cuckooIndex = random.nextInt(populationSize);
                        var cuckoo = population[cuckooIndex];
                        var stepSize = random.nextDouble() * alpha;
                        var step = calculateStep(cuckoo, solution, stepSize);
                        var newValue = calculateNewValue(solution, step);
                        return clamp(newValue[0], lowerBorder[0], upperBorder[0]);
                    }
                })
                .toArray();
    }

    private double calculateSigma() {
        var numerator = gamma(1.0 + alpha) * Math.sin(Math.PI * alpha / 2.0);
        var denominator = gamma((1.0 + alpha) / 2.0) * alpha * Math.pow(2.0, (alpha - 1.0) / 2.0);
        var fraction = numerator / denominator;

        return Math.pow(fraction, 1.0 / alpha);
    }

    private double gamma(double x) {
        return Math.exp(logGamma(x));
    }

    private double logGamma(double x) {
        double[] coefficients = {0.99999999999980993, 676.5203681218851, -1259.1392167224028,
                771.32342877765313, -176.61502916214059, 12.507343278686905,
                -0.13857109526572012, 9.9843695780195716e-6, 1.5056327351493116e-7};

        var sum = Arrays.stream(coefficients, 1, coefficients.length)
                .map(coefficient -> coefficient / (x + coefficient))
                .sum();

        return Math.log(sum * Math.sqrt(2 * Math.PI)) - (x + 0.5) * Math.log(x + 5.5) + (x + 5.5);
    }

    private double[] calculateStep(double[] cuckoo, double[] solution, double stepSize) {
        return IntStream.range(0, lowerBorder.length)
                .mapToDouble(k -> stepSize * (cuckoo[k] - solution[k]))
                .toArray();
    }

    private double[] calculateNewValue(double[] solution, double[] step) {
        return IntStream.range(0, lowerBorder.length)
                .mapToDouble(k -> solution[k] + step[k])
                .toArray();
    }

    private void updatePopulation(double[][] newPopulation) {
        IntStream.rangeClosed(populationSize / 2, populationSize - 1)
                .mapToObj(x -> populationSize - 1 - x)
                .forEach(x -> population[x] = newPopulation[x]);
    }

    private double calculateFitness(double[] solution) {
        var x = solution[0];
        var y = solution[1];
        double result;

        switch (mode) {
            case 1: // Funkcja Rosenbrocka
                result = Math.pow(1 - x, 2) + 100 * Math.pow(y - Math.pow(x, 2), 2);
                break;
            case 2: // Funkcja Bootha
                result = Math.pow(x + 2 * y - 7, 2) + Math.pow(2 * x + y - 5, 2);
                break;
            case 3: // Funkcja Ackleya
                var term1 = -20 * Math.exp(-0.2 * Math.sqrt(0.5 * (Math.pow(x, 2) + Math.pow(y, 2))));
                var term2 = -Math.exp(0.5 * (Math.cos(2 * Math.PI * x) + Math.cos(2 * Math.PI * y)));
                result = term1 + term2 + Math.E + 20;
                break;
            case 4: // Funkcja Rastrigina
                var sum = Math.pow(x, 2) - 10 * Math.cos(2 * Math.PI * x) + Math.pow(y, 2) - 10 * Math.cos(2 * Math.PI * y);
                result = 20 + sum;
                break;
            default:
                result = 0;
                break;
        }

        return result;
    }

    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(value, max));
    }

    public void run(int mode) {
        this.mode = mode;
        if (mode != 0) {
            SetFunctions setFunctions = new SetFunctions(mode);
            nameFunction = setFunctions.getNameFunction();
            optimum += " " + setFunctions.getOptimum();
        }

        var cuckooSearch = new CuckooSearch(
                populationSize,
                probability,
                alpha,
                lowerBorder,
                upperBorder,
                maxIterations
        );

        var solution = cuckooSearch.run();

        bestSolution = Arrays.toString(solution);
        fitness = cuckooSearch.calculateFitness(solution);
    }
}

