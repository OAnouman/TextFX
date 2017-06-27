package com.graphicodeci.address.controller;/**
 * Created by MARTIAL ANOUMAN on 6/23/2017.
 */

import com.graphicodeci.address.model.Person;
import com.graphicodeci.address.model.PersonListWrapper;
import com.graphicodeci.address.view.PersonEditDialogController;
import com.graphicodeci.address.view.PersonOverviewController;
import com.graphicodeci.address.view.RootLayoutController;
import javafx.application.Application;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.image.Image;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.prefs.Preferences;

public class MainApp extends Application {

    private Stage prinaryStage;
    private BorderPane rootLayout;
    private ObservableList<Person> personData = FXCollections.observableArrayList();


    /**
     * Construtor
     *
     */
    public MainApp (){
        personData.add(new Person("Martial", "Anouman"));
        personData.add(new Person("Kouadjo", "Eric"));
        personData.add(new Person("Mathurin","Kouakou"));
        personData.addAll(new Person("Marc","Fossou"),
                new Person("Ginette", "Ebrotié"),
                new Person("Francine", "Le Cloirec"),
                new Person("Rose","Traoré"),
                new Person("Aram", "Mondah"));
    }

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) {
        this.prinaryStage = primaryStage;
        this.prinaryStage.setTitle("Address App");
        //Set application icon
        this.prinaryStage.getIcons().add(new Image("file:resources/images/address_app.png"));

        initRootLayout();

        showPersonOverview();



        prinaryStage.show();
    }

    /**
     * Init RootLayout
     */

    public void initRootLayout(){

        List result = loadFxmlResource("../view/RootLayout.fxml");
        rootLayout = (BorderPane) result.get(1);
        FXMLLoader loader = (FXMLLoader)  result.get(0);
        Scene scene =  new Scene(rootLayout);
        prinaryStage.setScene(scene);

        //Get the Rootlayout controller
        RootLayoutController controller = loader.getController();
        controller.setMainApp(this);

        prinaryStage.show();

        //Try to open last opened file
        File file = getPersonFilePath();
        if(file != null)
            loadPersonDataFromFile(file);

    }

    /**
     * Shows the PersonOverview inside the RootLayout
     */
    public void showPersonOverview(){

        List result = loadFxmlResource("../view/PersonOverview.fxml");
        AnchorPane personOverview = (AnchorPane) result.get(1);
        FXMLLoader loader = (FXMLLoader) result.get(0);

        //Add personOverview in RootLayout
        rootLayout.setCenter(personOverview);

        //Retrieve the personOverviewController
        PersonOverviewController controller = loader.getController();
        controller.setMainApp(this);
    }

    public boolean showEditPersonDialog(Person person){

        List result = loadFxmlResource("../view/PersonEditDialog.fxml");
        AnchorPane personEditDialog = (AnchorPane) result.get(1);
        FXMLLoader loader = (FXMLLoader) result.get(0);

        //Creating dialog stage
        Stage dialogStage = new Stage();
        dialogStage.setTitle("Edit Person");
        dialogStage.initModality(Modality.WINDOW_MODAL);
        dialogStage.initOwner(prinaryStage);
        Scene dialogScene = new Scene(personEditDialog);
        dialogStage.setScene(dialogScene);

        //Getting PersonEditDialogController
        PersonEditDialogController controller = loader.getController();
        controller.setDialogStage(dialogStage);
        controller.setPerson(person);

        //Show the dialog
        dialogStage.showAndWait();

        return controller.isOkClicked();
    }

    /**
     * Load fxml ressource
     */
    public List loadFxmlResource(String url){
        try {
            //loqding rootLqyout fxml file
            FXMLLoader loader = new FXMLLoader();
            loader.setLocation(MainApp.class.getResource(url));
            Pane pane = loader.load();

            List result = new ArrayList();
            result.add(loader);
            result.add(pane);

            return result;

        } catch (IOException e) {
           return null;
        }
    }


    /**
     * Retutns the person file preference, i.e the last file opened.
     * The preference is read from OS specific registry.
     * If no such preference can't be found return null.
     *
     * @return
     */
    public File getPersonFilePath(){
        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        String filePath = prefs.get("filePath",null);
        if(filePath != null)
            return new File(filePath);
        else
            return null;
    }

    /**
     * Sets the file path of the current file loaded.
     * Persisted in specific OS registry
     *
     * @param file file loaded or null to remove the path
     */
    public void setPersonFilePath(File file){

        Preferences prefs = Preferences.userNodeForPackage(MainApp.class);
        if(file != null){
            //Adding file path
            prefs.put("filePath",file.getPath());

            //Update Stage title
            this.prinaryStage.setTitle("AddressApp - [" + file.getPath()+"]");
        }else
        {
            prefs.remove("filePath");
            //Update stage title
            this.prinaryStage.setTitle("AddressApp");
        }
    }

    /**
     * Load personData from specified file
     * @param file
     */
    public void loadPersonDataFromFile(File file){

        try {
            //Obtaining JAXBContext instance
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);
            //Creating unmarshaller
            Unmarshaller um = context.createUnmarshaller();

            //Reading XML and unmarshalliing (XML to Java)
            PersonListWrapper wrapper = (PersonListWrapper) um.unmarshal(file);

            personData.clear();
            personData.addAll(wrapper.getPersons());

            //Persist last file loaded path
            setPersonFilePath(file);


        } catch (JAXBException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not load data from file");
            alert.setContentText("Could not load data from file. File path :\n" + file.getPath() + "\n" +
                                    "Error details :\n" + e.getMessage());

            alert.showAndWait();
        }
    }

    /**
     * Save personData to specified file.
     * @param flle
     */
    public void savePersonDataToFile(File file){

        try {
            //Obtaining JAXBContext instance
            JAXBContext context = JAXBContext.newInstance(PersonListWrapper.class);

            //Creating marshaller
            Marshaller ma = context.createMarshaller();
            ma.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT,true);

            //Wrapping personData
            PersonListWrapper wrapper = new PersonListWrapper();
            wrapper.setPersons(personData);

            //Marshalling (Java to XML) and saving XML to the file
            ma.marshal(wrapper,file);

            //Persist file path
            setPersonFilePath(file);

        } catch (JAXBException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setHeaderText("Could not save data to file");
            alert.setContentText("Could not save data to file. File path :\n" + file.getPath() + "\n" +
                    "Error details :\n" + e.getMessage());

            alert.showAndWait();
        }
    }

    /**
     * Return the main stage
     * @return
     */
    public Stage getPrimaryStage() {
        return prinaryStage;
    }

    /**
     * Return the personData as an ObservableList of Person
     * @return ObservableList<Person> personData
     */
    public ObservableList<Person> getPersonData() {
        return personData;
    }


}
