package food;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.InputMethodEvent;
import javafx.scene.input.KeyEvent;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;

public class FoodProgramController implements Initializable {
    // Text Fields storing the user input for the amount of each item.
    @FXML private TextField hotDogAmount;
    @FXML private TextField bratAmount;
    @FXML private TextField hamburgerAmount;
    @FXML private TextField friesAmount;
    @FXML private TextField sodaAmount;
    @FXML private TextField waterAmount;

    // Label that display the price as well as other outputs (i.e. non-fatal errors)
    @FXML private Label priceLabel;

    // ListView to display the running total of daily sales, and HashMap to store the information.
    @FXML private ListView totalList;
    private HashMap<String,Object> cumulativeSales = new HashMap<>();

    // Set a tax constant for easy change in the future.
    final double TAX = 0.00;


    /** This is a helper method made to limit code duplication. It checks that a TextField contains a properly formatted number and then returns it. */
    private int intValueOf(TextField field) throws NumberFormatException, NegativeNumberException {
        // Parse user input, will throw NumberFormatException if incorrect.
        int num = Integer.parseInt(field.getText());

        // If negative, the input is invalid and an error is thrown.
        if (num < 0) throw new NegativeNumberException();

        return num;
    }

    /** Another helper method designed to shorten the calculate method. It adds the amount of sales to the running total. */
    private void addToCumulativeSales(String key, TextField field) throws NumberFormatException, NegativeNumberException  {
        cumulativeSales.put(key, intValueOf(field) + (Integer)cumulativeSales.get(key));
    }

    /** Calculates the total price for the selected items and adds that to the running total */
    public void calculate() {
        double totalPrice = 0.00;

        try {
            // Add the price of each item times the amount of item to the total price
            totalPrice += Foods.HOTDOG.getPrice() * intValueOf(hotDogAmount);
            // Add to the running total of sales
            addToCumulativeSales(Foods.HOTDOG.getName(), hotDogAmount);

            // Repeat the same process for the rest of the items

            totalPrice += Foods.BRAT.getPrice() * intValueOf(bratAmount);
            addToCumulativeSales(Foods.BRAT.getName(), bratAmount);

            totalPrice += Foods.HAMBURGER.getPrice() * intValueOf(hamburgerAmount);
            addToCumulativeSales(Foods.HAMBURGER.getName(), hamburgerAmount);

            totalPrice += Foods.FRIES.getPrice() * intValueOf(friesAmount);
            addToCumulativeSales(Foods.FRIES.getName(), friesAmount);

            totalPrice += Foods.SODA.getPrice() * intValueOf(sodaAmount);
            addToCumulativeSales(Foods.SODA.getName(), sodaAmount);

            totalPrice += Foods.WATER.getPrice() * intValueOf(waterAmount);
            addToCumulativeSales(Foods.WATER.getName(), waterAmount);


            // Add the tax
            totalPrice += totalPrice * TAX;

            // Add the profit to the running total (or cumulativeSales)
            cumulativeSales.put("Total Profit", totalPrice + (Double)cumulativeSales.get("Total Profit"));

            // Update the ListView containing the running total
            updateTotalList();

            priceLabel.setText("Price: $" + totalPrice);
        } catch(NumberFormatException err) {
            // The user inputted invalid characters that didn't properly format into an integer.
            priceLabel.setText("Please only input real, positive integers.");
        } catch (NegativeNumberException err) {
            // The user inputted a negative number.
            priceLabel.setText("Please only input positive integers.");
        }
    }

    /** Updates the list containing cumulative sales to display the current information. Should be called whenever the information is changed. */
    public void updateTotalList() {
        // Clear the totalList then update it with the relevant information.

        totalList.getItems().clear();

        for (String key: cumulativeSales.keySet()) {
            totalList.getItems().add(key + ": " + cumulativeSales.get(key));
        }
    }

    /** Completely resets the state of the application, setting everything to their default values. */
    public void clear() {
        hotDogAmount.setText("0");
        bratAmount.setText("0");
        hamburgerAmount.setText("0");
        friesAmount.setText("0");
        sodaAmount.setText("0");
        waterAmount.setText("0");

        priceLabel.setText("Price: $0.00");

        cumulativeSales.put(Foods.HOTDOG.getName(), 0);
        cumulativeSales.put(Foods.BRAT.getName(), 0);
        cumulativeSales.put(Foods.HAMBURGER.getName(),0);
        cumulativeSales.put(Foods.FRIES.getName(),0);
        cumulativeSales.put(Foods.SODA.getName(),0);
        cumulativeSales.put(Foods.WATER.getName(),0);
        cumulativeSales.put("Total Profit", 0.0);
        updateTotalList();
    }

    /** Exits the application safely and completely. Same as the 'X' button. */
    public void exit() {
        System.exit(0);
    }

    /** A helper method to set character (number only) and length restrictions on text fields. */
    private void setNumberAndLengthConstraints(TextField field) {
        // Add an observer to each TextField so that we can control what they input.
        field.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                // Allow the user to make the field empty so that they can properly edit it.
                // If they attempt to calculate it while a field is empty, the error is caught.
                if (newValue.length() == 0) return;

                // Set a constraint on the length so it cannot be greater than 3.
                if (newValue.length() > 3) {
                    field.setText(oldValue);
                    return;
                }

                try {
                    // Check to see if the field contains a properly formatted Integer.
                    int num = Integer.parseInt(newValue);
                } catch (NumberFormatException err) {
                    // The newValue is not properly formatted, so return to the old value.
                    field.setText(oldValue);
                }

            }
        });
    }

    /** Called when the application is finished initializing - prepares the app for use. */
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Set constraints on all the text fields to only allow numbers and limit the size.
        setNumberAndLengthConstraints(hotDogAmount);
        setNumberAndLengthConstraints(bratAmount);
        setNumberAndLengthConstraints(hamburgerAmount);
        setNumberAndLengthConstraints(friesAmount);
        setNumberAndLengthConstraints(sodaAmount);
        setNumberAndLengthConstraints(waterAmount);

        totalList.setEditable(false);

        // Once the application is properly initialized, clear the screen to prepare it for user use.
        clear();
    }
}
