package org.example;

import cuckoo.search.CuckooSearch;
import cuckoo.search.SetFunctions;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.text.Text;

import java.util.Arrays;

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
    @FXML
    private Button setDefault;

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

        start.setOnAction(this::cuckoo);
        setDefault.setOnAction(this::defaultData);
        reset.setOnAction(this::resetData);
        getProbability.setOnKeyTyped(keyEvent -> numProbability = validateInput(getProbability));
        getMaxIteration.setOnKeyTyped(keyEvent -> numMaxIterations = validateInput(getMaxIteration));
        getAlpha.setOnKeyTyped(keyEvent -> numAlpha = validateInput(getAlpha));
        getPopulationSize.setOnKeyTyped(keyEvent -> numPopulationSize = validateInput(getPopulationSize));
        getLb_r.setOnKeyTyped(keyEvent -> {
            if(mode !=0 ){
                setAutoFunction();
                getLb_r.appendText(keyEvent.getCharacter());
            }
            numLb_r = validateInput(getLb_r);
            getLb_r.positionCaret(getLb_r.getText().length());
        });
        getLb_l.setOnKeyTyped(keyEvent -> {
            if(mode !=0 ){
                setAutoFunction();
                getLb_l.appendText(keyEvent.getCharacter());
            }
            numLb_l = validateInput(getLb_l);
            getLb_l.positionCaret(getLb_l.getText().length());
        });
        getUb_r.setOnKeyTyped(keyEvent -> {
            if(mode !=0 ){
                setAutoFunction();
                getUb_r.appendText(keyEvent.getCharacter());
            }
            numUb_r = validateInput(getUb_r);
            getUb_r.positionCaret(getUb_r.getText().length());
        });
        getUb_l.setOnKeyTyped(keyEvent -> {
            if(mode !=0 ){
                setAutoFunction();
                getUb_l.appendText(keyEvent.getCharacter());
            }
            numUb_l = validateInput(getUb_l);
            getUb_l.positionCaret(getUb_l.getText().length());
        });

        chooseFunction.getItems().addAll("Twoja funkcja", "Rosenbrock", "Booth", "Ackley", "Schwefel");
        chooseFunction.setValue("Twoja funkcja");
        chooseFunction.setOnAction(event -> {
            String selectedFunction = chooseFunction.getSelectionModel().getSelectedItem();
            switch (selectedFunction) {
                case "Rosenbrock":
                    mode = 1;
                    setFunction(mode);
                    break;
                case "Booth":
                    mode = 2;
                    setFunction(mode);
                    break;
                case "Ackley":
                    mode = 3;
                    setFunction(mode);
                    break;
                case "Schwefel":
                    mode = 4;
                    setFunction(mode);
                    break;
                case "Twoja funkcja":
                    mode = 0;
                    getLb_l.clear();
                    getLb_r.clear();
                    getUb_l.clear();
                    getUb_r.clear();
                    break;
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

    private void setAutoFunction(){
        chooseFunction.setValue("Twoja funkcja");
        mode = 0;
    }

    private void setFunction(int mode){
        SetFunctions set = new SetFunctions(mode);
        getLb_l.setText(String.valueOf(set.getLb_l()));
        getLb_r.setText(String.valueOf(set.getLb_r()));
        getUb_l.setText(String.valueOf(set.getUb_l()));
        getUb_r.setText(String.valueOf(set.getUb_r()));

        numLb_l = validateInput(getLb_l);
        numLb_r = validateInput(getLb_r);
        numUb_l =validateInput(getUb_l);
        numUb_r = validateInput(getUb_r);
    }

    @FXML
    private void cuckoo(ActionEvent event){
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

    }

    @FXML
    public void resetData(ActionEvent event) {
        chooseFunction.setValue("Twoja funkcja");

        getProbability.clear();
        getAlpha.clear();
        getLb_l.clear();
        getLb_r.clear();
        getUb_l.clear();
        getUb_r.clear();
        getMaxIteration.clear();
        getPopulationSize.clear();

        numProbability = null;
        numAlpha = null;
        numLb_l = null;
        numLb_r = null;
        numUb_l = null;
        numUb_r = null;
        numMaxIterations = null;
        numPopulationSize = null;

        setResult.setText("");
    }

    @FXML
    public void defaultData(ActionEvent event) {

        chooseFunction.setValue("Twoja funkcja");
        mode = 0;

        getProbability.setText("0.25");
        numProbability = validateInput(getProbability);

        getAlpha.setText("0.8");
        numAlpha = validateInput(getAlpha);

        getLb_l.setText("-10.0");
        numLb_l = validateInput(getLb_l);

        getLb_r.setText("-5.0");
        numLb_r = validateInput(getLb_r);

        getUb_l.setText("5.0");
        numUb_l =validateInput(getUb_l);

        getUb_r.setText("10.0");
        numUb_r = validateInput(getUb_r);

        getMaxIteration.setText("1000");
        numMaxIterations = validateInput(getMaxIteration);

        getPopulationSize.setText("500");
        numPopulationSize = validateInput(getPopulationSize);

        setResult.setText("");
    }
}
