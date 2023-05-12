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

    private int mode = 0;
    private double populationSize_, probability_, alpha_, maxIteration_;

    @FXML
    private void start() {
        populationSize_ = Integer.parseInt(populationSize.getText());
        probability_ = Integer.parseInt(probability.getText());
        alpha_ = Integer.parseInt(alpha.getText());
        double lb_l = -10.0, lb_r = -5.0, ub_l = 5.0, ub_r = 10.0;
        double[] lb = {lb_l, lb_r};
        double[] ub = {ub_l, ub_r};
        maxIteration_ = Integer.parseInt(maxIteration.getText());

        if(populationSize_ == 0 || probability_ == 0 || alpha_ == 0 || maxIteration_ == 0){
            setDefultValues();
        }

        // 0 - Twoja funkcja
        // 1 - Funkcja Rosenbrocka
        // 2 - Funkcja Bootha
        // 3 - Funkcja Ackleya
        // 4 - Funkcja Rastrigina
        mode = 0;

        CuckooSearch cuckooSearch = new CuckooSearch((int) populationSize_, probability_, alpha_, lb, ub, (int) maxIteration_);
        cuckooSearch.run(mode);

        reset.setText(cuckooSearch.getNameFunction()+"\n"+cuckooSearch.getBestSolution()+"\n"+cuckooSearch.getFitness()+"\n"+cuckooSearch.getOptimum());
    }
    
    
    public void setDefultValues() {
        populationSize_ = 500;
        probability_ = 0.25;
        alpha_ = 0.8;
        double lb_l = -10.0, lb_r = -5.0, ub_l = 5.0, ub_r = 10.0;
        double[] lb = {lb_l, lb_r};
        double[] ub = {ub_l, ub_r};
        maxIteration_ = 1000;
    }
}
