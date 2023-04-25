package cuckoo.search;

public class SetFunctions {
    private String nameFunction, optimum;
    private double[] leftBorder, upperBorder;
    public SetFunctions(int mode){
        if(mode == 1){
            nameFunction = "Funkcja Rosenbrocka";
            leftBorder = new double[]{-2.048, -2.048};
            upperBorder = new double[]{2.048, 2.048};
            optimum="Optimum: [1.0, 1.0]";

        }
        if(mode == 2){
            nameFunction = "Funkcja Bootha";
            leftBorder = new double[]{-10.0, -10.0};
            upperBorder = new double[]{10.0, 10.0};
            optimum="Optimum: [1.0, 3.0]";
        }
        if(mode == 3){
            nameFunction = "Funkcja Ackleya";
            leftBorder = new double[]{-32.0, -32.0};
            upperBorder = new double[]{32.0, 32.0};
            optimum="Optimum: [0.0, 0.0]";
        }
        if(mode == 4){
            nameFunction = "Funkcja Rastrigina";
            leftBorder = new double[]{-5.12, -5.12};
            upperBorder = new double[]{5.12, 5.12};
            optimum="Optimum: [0.0, 0.0]";
        }
    }

    public String getNameFunction(){
        return nameFunction;
    }

    public String getOptimum() {
        return optimum;
    }

    public double[] getLeftBorder() {
        return leftBorder;
    }

    public double[] getUpperBorder() {
        return upperBorder;
    }
}
