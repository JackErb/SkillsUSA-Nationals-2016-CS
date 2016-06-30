package grades;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.scene.input.MouseEvent;

import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

public class GradeProgramController implements Initializable {
    // Displays all grades/scores for the selected class.
    @FXML private ListView scoreListView;

    // TextFields containing specific information for a selected grade.
    @FXML private TextField nameField;
    @FXML private TextField scoreField;
    @FXML private TextField maxScoreField;

    // Displays all classes.
    @FXML private ListView classListView;
    // Contains all grade info for each class.
    private HashMap<String,ArrayList<Grade>> allClassGrades;

    // A label used to output the class information as well as to display caught errors to the user.
    @FXML private Label classInfoLabel;

    // Returns the list of grades from the selected class. Returns null if there is none selected.
    private ArrayList<Grade> getGrades() {
        return allClassGrades.get(classListView.getSelectionModel().getSelectedItem());
    }

    // Updates the selected grade for the selected subject.
    @FXML public void updateGrade() {
        try {
            ArrayList<Grade> classGrades = getGrades();
            if (classGrades == null) {
                displayError("Class grades were not found. Please make sure that you have selected everything correctly.");
                return;
            }

            // Get the index of the selected grade
            int index = scoreListView.getSelectionModel().getSelectedIndex();

            // User has not selected anything.
            if (index < 0) return;

            Grade grade = classGrades.get(index);
            if (grade == null) {
                displayError("There was a problem. Please reselect and retry.");
                return;
            }

            // Update grade information fields to properly reflect the grade's info
            grade.setName(nameField.getText());
            grade.setScore(Integer.parseInt(scoreField.getText()));
            grade.setMaxScore(Integer.parseInt(maxScoreField.getText()));

            // Update the view so the information will be properly displayed.
            updateView();
        } catch (NumberFormatException err) {
            // If any NumberFormatExceptions are thrown when trying to parse the integers, it will be caught here.
            displayError("Please only input real, valid integers.");
            return;
        }
    }

    @FXML public void deleteSelectedGrade() {
        int index = scoreListView.getSelectionModel().getSelectedIndex();

        // Nothing is selected, so return from the method.
        if (index < 0) {
            displayError("Please select an item before deleting.");
            return;
        }

        ArrayList<Grade> classGrades = getGrades();
        if (classGrades == null) {
            displayError("Class grades were not found. Please make sure nothing invalid is inputted.");
            return;
        }
        classGrades.remove(index);
        updateView();
    }

    // Clears the screen. Removes all non-default information.
    @FXML public void clear() {
        for (String key: allClassGrades.keySet()) {
            allClassGrades.get(key).clear();
        }

        updateView();
    }

    // Calculates the grade for the selected class.
    @FXML public void calculate() {
        String classInfo = "";


        ArrayList<Grade> grades = getGrades();
        if (grades == null || grades.size() == 0) {
            displayError("Please input one or more grade.");
            return;
        }

        // Calculate the grade's statistics for the selected class
        double average = 0.0;
        double low = grades.get(0).computerPercentage();
        double high = grades.get(0).computerPercentage();
        for (Grade grade: grades) {
            average += grade.computerPercentage();
            if (grade.computerPercentage() < low) { low = grade.computerPercentage(); }
            if (grade.computerPercentage() > high) { high = grade.computerPercentage(); }
        }
        average /= grades.size();


        // Properly format and input the stats into the classInfo variable
        classInfo += "Class: " + classListView.getSelectionModel().getSelectedItem() + "\n";
        classInfo += "Average: " + (int)Math.round(average * 100) + "% \n";
        classInfo += "High: " + (high * 100) + "% \n";
        classInfo += "Low: " + (low * 100) + "%";

        // Display the information.
        classInfoLabel.setText(classInfo);
    }

    // Displays a string in a the classInfoLabel alerting the user that they have done something wrong.
    // The error should have recommended steps to rectify the error.
    private void displayError(String s) {
        classInfoLabel.setText(s);
    }

    // Exits the program completely.
    @FXML public void exit() {
        System.exit(0);
    }

    // Adds a new, default grade to the currently selected class.
    @FXML public void newGrade() {
        ArrayList<Grade> classGrades = getGrades();
        if (classGrades == null) {
            displayError("Class grades were not found. Please make sure nothing invalid is inputted.");
            return;
        }
        classGrades.add(new Grade("Sample",0,0));

        updateView();
    }

    // Updates the information in the view completely.
    public void updateView() {
        ArrayList<Grade> classGrades = getGrades();
        if (classGrades == null) {
            displayError("Class grades were not found. Please make sure nothing invalid is inputted.");
            return;
        }

        // Clears then updates the list of scores/grades for the current class.
        scoreListView.getItems().clear();
        scoreListView.setItems(FXCollections.observableArrayList(classGrades));
    }

    // Called automatically when the scene is finished initializing
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Add the default classes to a HashMap containg all the raw grade information.
        allClassGrades = new HashMap<>();
        allClassGrades.put("Programming",new ArrayList<Grade>());
        allClassGrades.put("Art",new ArrayList<Grade>());
        allClassGrades.put("Science", new ArrayList<Grade>());
        allClassGrades.put("Math", new ArrayList<Grade>());
        allClassGrades.put("History", new ArrayList<Grade>());

        // Display the classes in the classListView
        classListView.setItems(FXCollections.observableArrayList(new String[]{"Programming","Art","Science","Math","History"}));

        // Update the view whenever the user clicks on the classListView. This is so that if they select a new class, it updates to that.
        classListView.setOnMouseClicked(new javafx.event.EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                updateView();
            }
        });

        classListView.selectionModelProperty().addListener(new ChangeListener() {
            @Override
            public void changed(ObservableValue observable, Object oldValue, Object newValue) {
                System.out.println("ok");
            }
        });

        // Every time the user clicks, this updates the text fields below so that the user can properly edit the information.
        scoreListView.setOnMouseClicked(new javafx.event.EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                int index = scoreListView.getSelectionModel().getSelectedIndex();
                // User has not selected anything so return.
                if (index < 0) { return; }

                Grade grade = allClassGrades.get(classListView.getSelectionModel().getSelectedItem()).get(index);
                if (grade == null) {
                    displayError("There was an error in manipulating the grade. Please reselect everything and try again.");
                    return;
                }

                nameField.setText(grade.getName());
                scoreField.setText("" + grade.getScore());
                maxScoreField.setText("" + grade.getMaxScore());
            }
        });
    }
}