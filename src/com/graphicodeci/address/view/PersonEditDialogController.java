package com.graphicodeci.address.view;

import com.graphicodeci.address.helper.DateHelper;
import com.graphicodeci.address.model.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

/**
 * Created by MARTIAL ANOUMAN on 6/24/2017.
 */
public class PersonEditDialogController {

    @FXML private TextField firstNameField;
    @FXML private TextField lastNameField;
    @FXML private TextField streetField;
    @FXML private TextField postalCodeField;
    @FXML private TextField cityField;
    @FXML private TextField birthdayField;

    private Stage dialogStage;
    private Person person;
    private boolean okClicked = false;

    @FXML
    /**
     * Initializes the controller class. Called after fxml file has been loaded.
     */
    private void initialize(){}

    /**
     * Sets dialog stage
     * @param stage
     */
    public void setDialogStage(Stage stage){
        this.dialogStage = stage;
    }

    /**
     * Sets the person to be edited in the dialog
     * @param person
     */
    public void setPerson(Person person) {
        this.person = person;

        firstNameField.setText(person.getFirstName());
        lastNameField.setText(person.getLastName());
        streetField.setText(person.getStreet());
        postalCodeField.setText(String.valueOf(person.getPostalCode()));
        cityField.setText(person.getCity());
        birthdayField.setText(DateHelper.format(person.getBirthday()));
        birthdayField.setPromptText("dd.MM.yyyy");
    }

    /**
     * Returns true if OK button clicked. False otherwise
     * @return
     */
    public boolean isOkClicked(){
        return okClicked;
    }

    @FXML
    /**
     * Called when OK button clicked
     */
    private void handleOk(){

        if(isInputValid())
        {
            person.setFirstName(firstNameField.getText());
            person.setLastName(lastNameField.getText());
            person.setStreet(streetField.getText());
            person.setPostalCode(Integer.parseInt(postalCodeField.getText()));
            person.setCity(cityField.getText());
            person.setBirthday(DateHelper.parse(birthdayField.getText()));

            okClicked = true;
            dialogStage.close();
        }

    }

    @FXML
    /**
     * Called when Cancel button is clicked
     */
    private void handleCancel(){
        dialogStage.close();
    }

    /**
     * Validate user input in the text fields.
     * @return whether all inputs are valids or not
     */
    private boolean isInputValid(){

        String errorMessage = "";

        if(firstNameField.getText() == null || firstNameField.getText().isEmpty() )
            errorMessage += "Invalid first name ! \n";
        if(lastNameField.getText() == null || lastNameField.getText().isEmpty() )
            errorMessage += "Invalid last name ! \n";
        if(streetField.getText() == null || streetField.getText().isEmpty() )
            errorMessage += "Invalid street !\n";
        if(postalCodeField.getText() == null || postalCodeField.getText().isEmpty() )
            errorMessage += "Invalid postal code !\n";
        else{
            //Try parse postal code
            try {
                Integer.parseInt(postalCodeField.getText());
            } catch (NumberFormatException e) {
                errorMessage += "Invalid postal code (must be an integer) !\n";
            }
        }
        if(cityField.getText() == null || cityField.getText().isEmpty() )
            errorMessage += "Invalid city name !\n";
        if(birthdayField.getText() == null || birthdayField.getText().isEmpty() )
            errorMessage += "Invalid birthday date !\n";
        else{
            if(!DateHelper.validDate(birthdayField.getText()))
                errorMessage += "Invalid birthday date. Use 'dd.MM.yyyy' format !\n";
        }

        if(errorMessage.isEmpty())
            return true;
        else{
            Alert errorAlert = new Alert(Alert.AlertType.ERROR);
            errorAlert.setTitle("Invalid Fields");
            errorAlert.setHeaderText("Please correct invalid fields");
            errorAlert.setContentText(errorMessage);
            errorAlert.initOwner(dialogStage);

            errorAlert.showAndWait();

            return false;
        }
    }
}
