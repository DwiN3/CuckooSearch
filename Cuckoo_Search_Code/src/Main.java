public class Main {
    public static void main(String[] args) {

        int populationSize = 100;
        double pa = 0.25;
        double alpha = 0.5;
        double lb_l = -5.0, lb_r= -5.0, ub_l= 5.0, ub_r= 5.0;
        double[] lb = {lb_l, lb_r};
        double[] ub = {ub_l, ub_r};
        int maxIterations = 100;

        int mode = 1;
        // 0 - Twoja funkcja
        // 1 - Funkcja Rosenbrocka
        // 2 - Funkcja Bootha
        // 3 - Funkcja Ackleya
        // 4 - Funkcja Rastrigina

        CuckooSearch cuckooSearch = new CuckooSearch(populationSize, pa, alpha, lb, ub, maxIterations);
        cuckooSearch.run(mode);
        System.out.println(cuckooSearch.getNameFunction());
        System.out.println(cuckooSearch.getBestSolution());
        System.out.println(cuckooSearch.getFitness());
        System.out.println(cuckooSearch.getOptimum());
    }
}