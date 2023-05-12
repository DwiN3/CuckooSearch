package org.example;

import java.io.IOException;

import cuckoo.search.CuckooSearch;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CuckooSearchController {

    @FXML
    private TextField populationSize, probability, alpha, leftBorder, upperBorder, maxIteration;

    @FXML
    private Button start, reset;

    @FXML
    private Text results;


    private double probability_, alpha_;
    private int populationSize_, maxIteration_;
    private int mode = 0;

    @FXML
    private void run() {
        if(checkEmptyValue()){
            setDefultValues();
        }
        else{
            populationSize_ = Integer.parseInt(populationSize.getText());
            probability_ = Double.parseDouble(probability.getText());
            alpha_ = Double.parseDouble(alpha.getText());
            maxIteration_ = Integer.parseInt(maxIteration.getText());
        }

        double lb_l = -10.0, lb_r = -5.0, ub_l = 5.0, ub_r = 10.0;
        double[] lb = {lb_l, lb_r};
        double[] ub = {ub_l, ub_r};

        // 0 - Twoja funkcja
        // 1 - Funkcja Rosenbrocka
        // 2 - Funkcja Bootha
        // 3 - Funkcja Ackleya
        // 4 - Funkcja Rastrigina
        mode = 0;

        CuckooSearch cuckooSearch = new CuckooSearch((int) populationSize_, probability_, alpha_, lb, ub, (int) maxIteration_);
        cuckooSearch.run(mode);
        results.setText(cuckooSearch.getNameFunction()+"\n"+cuckooSearch.getBestSolution()+"\n"+cuckooSearch.getFitness()+"\n"+cuckooSearch.getOptimum());
    }
    
    private void setDefultValues() {
        populationSize_ = 500;
        probability_ = 0.25;
        alpha_ = 0.8;
        double lb_l = -10.0, lb_r = -5.0, ub_l = 5.0, ub_r = 10.0;
        double[] lb = {lb_l, lb_r};
        double[] ub = {ub_l, ub_r};
        maxIteration_ = 1000;
    }

    private boolean checkEmptyValue(){
        if(populationSize.getText().isEmpty() ||  probability.getText().isEmpty() || alpha.getText().isEmpty() || maxIteration.getText().isEmpty())
            return true;
        else return false;
    }

}
