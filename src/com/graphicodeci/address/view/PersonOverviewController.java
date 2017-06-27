package com.graphicodeci.address.view;

import com.graphicodeci.address.controller.MainApp;
import com.graphicodeci.address.helper.DateHelper;
import com.graphicodeci.address.model.Person;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;


/**
 * Created by MARTIAL ANOUMAN on 6/24/2017.
 * @author Martial Anouman
 */
public class PersonOverviewController {

    @FXML
    private TableView<Person> personTable;
    @FXML
    private TableColumn<Person,String> firstNameColumn ;
    @FXML
    private TableColumn<Person,String> lastNameColumn;

    @FXML
    private Label firstNameLabel;
    @FXML
    private Label lastNameLabel;
    @FXML
    private Label streetLabel;
    @FXML
    private Label postalCodeLabel;
    @FXML
    private Label cityLabel;
    @FXML
    private Label birthdayLabel;

    //REference to MainApp
    private MainApp mainApp ;

    /**
     * Constructor. Called before initialize()
     */
    public PersonOverviewController(){

    }

    /**
     * this func initializes the controller class.
     * It is called automatically after the fxml file has been loaded
     */
    @FXML
    private void initialize(){

        //Initialize the personTable with the two column
        firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
        lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());

        //Clear person details
        showPersonDetails(null);

        /*
        Add a iistener to tablePerson to notified when a line is selected.
        Show person details
        */
        personTable.getSelectionModel().selectedItemProperty().addListener(
                (observable, oldValue, newValue) -> showPersonDetails(newValue)
        );

    }

    /**
     * Show the selected person details in the labels on the right
     * or clear all the labels if person is null
     * @param person the selected person or null of none
     */
    private void showPersonDetails(Person person){

        if(person != null){
            firstNameLabel.setText(person.getFirstName());
            lastNameLabel.setText(person.getLastName());
            streetLabel.setText(person.getStreet());
            postalCodeLabel.setText( Integer.toString(person.getPostalCode()) );
            cityLabel.setText(person.getCity());
            birthdayLabel.setText(DateHelper.format(person.getBirthday()));
        }else{
            firstNameLabel.setText("");
            lastNameLabel.setText("");
            streetLabel.setText("");
            postalCodeLabel.setText("");
            cityLabel.setText("");
            birthdayLabel.setText("");
        }
    }


    @FXML
    /**
     * Called when user click on delete button
     */
    private void handleDeletePerson(){
        int selectedIndex = personTable.getSelectionModel().getSelectedIndex();

        if(selectedIndex >= 0 ){
            personTable.getItems().remove(selectedIndex);
        }else{
            //If nothing selected
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.initOwner(this.mainApp.getPrimaryStage());
            warningAlert.setTitle("No Selection");
            warningAlert.setHeaderText("No Person Selected");
            warningAlert.setContentText("Please select a person in the table.");

            warningAlert.showAndWait();
        }
    }

    @FXML
    /**
     * Called when user click on new button.
     * Show dialog for creating new person.
     */
    private void handleNewPerson(){
        Person tempPerson = new Person();

        boolean isOkClicked =  this.mainApp.showEditPersonDialog(tempPerson);
        if(isOkClicked) this.mainApp.getPersonData().add(tempPerson);
    }

    @FXML
    /**
     * Called when user click on edit button.
     * Show dialog to edit selected person
     */
    private void handleEditPerson(){
        Person selectedPerson = personTable.getSelectionModel().getSelectedItem();

        if(selectedPerson != null){
            boolean isOkClicked = this.mainApp.showEditPersonDialog(selectedPerson);
            if(isOkClicked) //Show edited person details
                showPersonDetails(selectedPerson);
        }else{
            //If nothing selected
            Alert warningAlert = new Alert(Alert.AlertType.WARNING);
            warningAlert.initOwner(this.mainApp.getPrimaryStage());
            warningAlert.setTitle("No Selection");
            warningAlert.setHeaderText("No Person Selected");
            warningAlert.setContentText("Please select a person in the table to edit.");

            warningAlert.showAndWait();
        }
    }

    /**
     * Called by MainApp to give back a reference to itself
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;

        //Add personList to th table
        personTable.setItems(mainApp.getPersonData());
    }

}
