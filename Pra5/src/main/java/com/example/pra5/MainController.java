package com.example.pra5;

import com.example.pra5.bank.*;
import com.example.pra5.bank.exceptions.*;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.Optional;

public class MainController {
    @FXML ListView<String> AccountListe;
    private PrivateBank fxmlBank;

    @FXML public void initialize() throws AccountAlreadyExistsException, IOException, TransactionAlreadyExistException, AccountDoesNotExistException {

        fxmlBank = new PrivateBank("FXMLBank","Fxmlbank",0.4,0.6);
        AccountListe.getItems().addAll(fxmlBank.getAllAccounts());
    }

    private void refresh(){
        AccountListe.getItems().clear();
        AccountListe.getItems().addAll(fxmlBank.getAllAccounts());
    }

    /**
     * method to select a Item from the Accountlist and change into a Detail view of the chosen account
     *
     * @param event
     * @throws IOException
     */
    @FXML public void auswaehlen(ActionEvent event) throws IOException {

        if(AccountListe.getSelectionModel().getSelectedItem() == null){
            return;
        }

        System.out.println( AccountListe.getSelectionModel().getSelectedItem());

        FXMLLoader fxmlLoader = new FXMLLoader(Pra5.class.getResource("Accountview.fxml"));
// ertellen ein Fenster und packt alles da rein
        Stage stage = (Stage) AccountListe.getScene().getWindow();
        Scene scene = new Scene(fxmlLoader.load());
        stage.setScene(scene);
// ganze transaktion anzeigen
        AccountController ac = fxmlLoader.getController();
        ac.setInformation(AccountListe.getSelectionModel().getSelectedItem(), fxmlBank);
    }

    /**
     * method to delete Account in GUI
     *
     * @param event
     * @throws AccountDoesNotExistException
     * @throws IOException
     */
    @FXML public void loeschen(ActionEvent event) throws AccountDoesNotExistException, IOException {
        System.out.println("delete Button pressed");

        String account = AccountListe.getSelectionModel().getSelectedItem();


        try{
            //meldung
            Alert dialog = new Alert(Alert.AlertType.CONFIRMATION);

            dialog.setTitle("Delete account");
            dialog.setHeaderText("Do you really wanna delete the Account: " + account +" ?");

            ButtonType yesButton = new ButtonType("yes");
            ButtonType noButton = new ButtonType("no", ButtonBar.ButtonData.CANCEL_CLOSE);

            dialog.getButtonTypes().setAll(yesButton,noButton);

            var answer = dialog.showAndWait();

            if(answer.get()== yesButton){
                System.out.println("Lösche Account " + account );
                fxmlBank.deleteAccount(account);
                refresh();
            }
        }catch(AccountDoesNotExistException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Account Does not Exits");
            Optional<ButtonType> result = alert.showAndWait();
            System.out.println(e.getMessage());
        }
    }

    /**
     * method to create a new Account in GUI
     *
     * @param actionEvent
     * @throws AccountAlreadyExistsException
     * @throws IOException
     */
    public void newAccount(ActionEvent actionEvent) throws AccountAlreadyExistsException, IOException {
        System.out.println("add new Account Button pressed");

        TextInputDialog dialog = new TextInputDialog("Account Name");
        dialog.setTitle("Create a new Account");
        dialog.setHeaderText("choose a name for the account: ");

        var input = dialog.showAndWait();
        try {
            if (input.isPresent()) {
                System.out.println("Chosen Account name: " + input.get());
                fxmlBank.createAccount(input.get());
                refresh();
            }
        }catch (AccountAlreadyExistsException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Account Does Exits");
            Optional<ButtonType> result = alert.showAndWait();
            System.out.println(e.getMessage());
        }
    }
}