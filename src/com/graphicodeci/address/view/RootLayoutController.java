package com.graphicodeci.address.view;

import com.graphicodeci.address.controller.MainApp;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.stage.FileChooser;

import java.io.File;

/**
 * Created by MARTIAL ANOUMAN on 6/25/2017.
 *
 * The contrllr for RootLayout.fxml
 */
public class RootLayoutController {

    //Reference to the main app
    private MainApp mainApp ;

    /**
     * Called by main app to give back reference to itself
     * @param mainApp
     */
    public void setMainApp(MainApp mainApp) {
        this.mainApp = mainApp;
    }

    /**
     * Initializes an empty address book
     * @return
     */
    @FXML
    private void handleNew(){

        mainApp.getPersonData().clear();
        mainApp.setPersonFilePath(null);

    }

    /**
     * Opens a file chooser dialog to select an xml file to load
     */
    @FXML
    private void handleOpen(){

        FileChooser fileChooser = new FileChooser();

        //Set extension filter
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("XMl files (*.xml)","*.xml");
        fileChooser.getExtensionFilters().addAll(extFilter);

        //Show file filter
        File file = fileChooser.showOpenDialog(mainApp.getPrimaryStage());

        if(file != null)
            mainApp.loadPersonDataFromFile(file);
    }

    /**
     * Save the address book to the current opened file.
     * If no file opened then show save as dialog
     */
    @FXML
    private void handleSave(){
        File file = this.mainApp.getPersonFilePath();

        if(file != null)
            this.mainApp.savePersonDataToFile(file);
        else{
            handleSaveAS();
        }

    }

    /**
     * Show Save dialog to specify the file name to save XML
     */
    @FXML
    private void handleSaveAS(){
        FileChooser fileChooser = new FileChooser();
        FileChooser.ExtensionFilter extFilter =
                new FileChooser.ExtensionFilter("XML files (*.xml)","*.xml");
        fileChooser.getExtensionFilters().addAll(extFilter);

        //show save dialog
        File file = fileChooser.showSaveDialog(this.mainApp.getPrimaryStage());

        if(file != null){
            //Make sure the file has correct extension
            if(!file.getPath().endsWith(".xml"))
                file = new File(file.getPath() + ".xml");

            this.mainApp.savePersonDataToFile(file);
        }
    }

    /**
     * Show about dialog
     */
    @FXML
    private void handleAbout(){

        Alert about = new Alert(Alert.AlertType.INFORMATION);
        about.setTitle("AddressApp");
        about.setHeaderText("About");
        about.setContentText("Application created by Martial Anouman, " +
                "inspired by Code.Makery");

        about.showAndWait();
    }

    /**
     * Ends the application with status code 0.
     */
    @FXML
    private void handleExit(){
        System.exit(0);
    }



}
