package org.example;

import java.io.IOException;

import cuckoo.search.CuckooSearch;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

public class CuckooSearchController {

    @FXML
    private TextField getProbability;
    @FXML
    private TextField getAlpha;
    @FXML
    private TextField getLb_l;
    @FXML
    private TextField getLb_r;
    @FXML
    private TextField getUb_l;
    @FXML
    private TextField getUb_r;
    @FXML
    private TextField getMaxIteration;
    @FXML
    private TextField getPopulationSize;
    @FXML
    private ChoiceBox<String> chooseFunction;
    @FXML
    private Text setResult;
    @FXML
    private Button start;
    @FXML
    private Button reset;

    private Number numProbability = 0;
    private Number numAlpha = 0;
    private Number numLb_l = 0;
    private Number numLb_r = 0;
    private Number numUb_l = 0;
    private Number numUb_r = 0;
    private Number numMaxIterations = 0;
    private Number numPopulationSize = 0;
    int mode = 0;

    public void initialize(){
        getProbability.setOnKeyTyped(keyEvent -> numProbability = validateInput(getProbability));
        getMaxIteration.setOnKeyTyped(keyEvent -> numMaxIterations = validateInput(getMaxIteration));
        getUb_r.setOnKeyTyped(keyEvent -> numUb_r = validateInput(getUb_r));
        getUb_l.setOnKeyTyped(keyEvent -> numUb_l =validateInput(getUb_l));
        getAlpha.setOnKeyTyped(keyEvent -> numAlpha = validateInput(getAlpha));
        getPopulationSize.setOnKeyTyped(keyEvent -> numPopulationSize = validateInput(getPopulationSize));
        getLb_r.setOnKeyTyped(keyEvent -> numLb_r = validateInput(getLb_r));
        getLb_l.setOnKeyTyped(keyEvent -> numLb_l = validateInput(getLb_l));

        chooseFunction.getItems().addAll("Rosenbrock", "Booth", "Ackley", "Rastrigin");
        chooseFunction.setValue("Wybierz funkcję");
        chooseFunction.setOnAction(event -> {
            String selectedFunction = chooseFunction.getSelectionModel().getSelectedItem();
            switch (selectedFunction) {
                case "Rosenbrock":
                    mode = 1;
                    break;
                case "Booth":
                    mode = 2;
                    break;
                case "Ackley":
                    mode = 3;
                    break;
                case "Rastrigin":
                    mode = 4;
                    break;
                default:
                    mode = 0;
            }
            System.out.println("Selected function mode: " + mode);
        });

    }

    public Number validateInput(TextField textField){
        String text = textField.getText();
        if (!text.matches("-?\\d*\\.?\\d*")) {
            textField.setText(text.replaceAll("[^-\\d.]", ""));
        }
        try {
            if (text.contains(".")) {
                return Double.parseDouble(text);
            } else {
                return Integer.parseInt(text);
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid input: " + text);
            return null;
        }

    } //obługa wprowadzania cyfr, blokowanie liter

    public void cuckoo(){
        start.setOnAction(v->{
            int populationSize = numPopulationSize.intValue();
            double probability = numProbability.doubleValue();
            double alpha = numAlpha.doubleValue();
            double lb_l = numLb_l.doubleValue();
            double lb_r = numLb_r.doubleValue();
            double ub_l = numUb_l.doubleValue();
            double ub_r = numUb_r.doubleValue();
            int maxIterations = numMaxIterations.intValue();
            double[] lb = new double[]{lb_l, lb_r};
            double[] ub = new double[]{ub_l, ub_r};

            CuckooSearch cuckooSearch = new CuckooSearch(populationSize, probability, alpha, lb, ub, maxIterations);
            cuckooSearch.run(mode);
            System.out.println(cuckooSearch.getNameFunction());
            System.out.println(cuckooSearch.getBestSolution());
            System.out.println(cuckooSearch.getFitness());
            System.out.println(cuckooSearch.getOptimum());

            setResult.setText(cuckooSearch.getNameFunction() + "\n"
                    + cuckooSearch.getBestSolution() + "\n"
                    + cuckooSearch.getFitness() + "\n"
                    + cuckooSearch.getOptimum() + "\n");
        });
    }

    public void resetData() {
        reset.setOnAction(v->{
            getProbability.clear();
            getAlpha.clear();
            getLb_l.clear();
            getLb_r.clear();
            getUb_l.clear();
            getUb_r.clear();
            getMaxIteration.clear();
            getPopulationSize.clear();
            setResult.setText("");
        });
    }
}
