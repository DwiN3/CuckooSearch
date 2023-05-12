package cuckoo.search;

public class SetFunctions {
    private String nameFunction, optimum;
    private double[] leftBorder, upperBorder;
    private double lb_l, lb_r, ub_l, ub_r;
    public SetFunctions(int mode){
        if(mode == 1){
            nameFunction = "Funkcja Rosenbrocka";
            lb_l = -2.048; lb_r = -2.048;
            ub_l =  2.048; ub_r =  2.048;
            leftBorder = new double[]{lb_l, lb_r};
            upperBorder = new double[]{ub_l, ub_r};
            optimum="Optimum: [1.0, 1.0]";

        }
        if(mode == 2){
            lb_l = -10.0; lb_r = -10.0;
            ub_l =  10.0; ub_r =  10.0;
            nameFunction = "Funkcja Bootha";
            leftBorder = new double[]{lb_l, lb_r};
            upperBorder = new double[]{ub_l, ub_r};
            optimum="Optimum: [1.0, 3.0]";
        }
        if(mode == 3){
            nameFunction = "Funkcja Ackleya";
            lb_l = -32.0; lb_r = -32.0;
            ub_l =  32.0; ub_r =  32.0;
            leftBorder = new double[]{lb_l, lb_r};
            upperBorder = new double[]{ub_l, ub_r};
            optimum="Optimum: [0.0, 0.0]";
        }
        if(mode == 4){
            nameFunction = "Funkcja Schwefela";
            lb_l = -500.0; lb_r = -500.0;
            ub_l =  500.0; ub_r =  500.0;
            leftBorder = new double[]{lb_l, lb_r};
            upperBorder = new double[]{ub_l, ub_r};
            optimum="Optimum: [0.0, 0.0]";
        }
    }

    public String getNameFunction(){ return nameFunction;
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

    public double getLb_l() {
        return lb_l;
    }

    public double getLb_r() {
        return lb_r;
    }

    public double getUb_l() {
        return ub_l;
    }

    public double getUb_r() {
        return ub_r;
    }
}
