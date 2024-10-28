package com.oos.praktikum5;

import bank.*;
import bank.Exceptions.*;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

public class AccountController {
    String account;

    PrivateBank bank;

    @FXML
    private ListView<Transaction> transactionsListView;
    @FXML
    private Label accountName;
    @FXML
    private Label balance;

    public void refresh() {
        accountName.setText("Accountname: " + account);
        balance.setText("Bank balance: " + bank.getAccountBalance(account));
        transactionsListView.getItems().clear();
        transactionsListView.getItems().addAll(bank.getTransactions(account));
    }

    @FXML
    public void deleteTransaction() throws TransactionDoesNotExistException, IOException, AccountDoesNotExistException {
        Transaction transaction = transactionsListView.getSelectionModel().getSelectedItem();

        if(transaction == null){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a transaction");
            alert.show();
            return;
        }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setHeaderText("Are you sure you want to delete this transaction?");

        ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.OK_DONE);
        ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.CANCEL_CLOSE);

        alert.getButtonTypes().setAll(yesButton, noButton);

        if (alert.showAndWait().get() == yesButton) {
            bank.removeTransaction(account, transaction);
            refresh();
        }
    }

    @FXML
    public void backButton() throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(P5.class.getResource("main-view.fxml"));
        Scene scene = new Scene(fxmlLoader.load());

        Stage stage = (Stage) transactionsListView.getScene().getWindow();
        stage.setTitle("PrivateBank");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    public void setInformation(String key, PrivateBank fbank){
        account = key;
        bank = fbank;
        refresh();
    }

    @FXML
    public void getAscendingTransactions(){
        transactionsListView.getItems().clear();
        transactionsListView.getItems().addAll(bank.getTransactionsSorted(account, true));
    }

    @FXML
    public void getDescendingTransactions(){
        transactionsListView.getItems().clear();
        transactionsListView.getItems().addAll(bank.getTransactionsSorted(account, false));
    }

    @FXML
    public void getPositivesTransactions(){
        transactionsListView.getItems().clear();
        transactionsListView.getItems().addAll(bank.getTransactionsByType(account,true));
    }

    @FXML
    public void getNegativesTransactions(){
        transactionsListView.getItems().clear();
        transactionsListView.getItems().addAll(bank.getTransactionsByType(account,false));
    }

    @FXML
    public void createTransaction() throws TransactionAlreadyExistException, AccountDoesNotExistException, IOException {
        Dialog dialog = new Dialog<>();
        dialog.setTitle("new Transaction");
        dialog.getDialogPane().getScene().getWindow().sizeToScene();

        ButtonType addTransactionButton = new ButtonType("add", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(addTransactionButton, ButtonType.CANCEL);

        List transactionChoices = new ArrayList<>(Arrays.asList("Transfer", "Payment"));

        ChoiceBox transactionType = new ChoiceBox<>();
        transactionType.getItems().addAll(transactionChoices);

        GridPane general = new GridPane();
        general.add(new Label("Transaction Type: "),0,0);
        general.add(transactionType,1,0);

        TextField date = new TextField();
        TextField amount = new TextField();
        TextField description = new TextField();

        //extra fields needed for Transfer
        TextField sender = new TextField();
        TextField reciever = new TextField();

        transactionType.addEventFilter(ActionEvent.ACTION,event->{
            System.out.println(transactionType.getSelectionModel().getSelectedItem() + " toggled");
            if(transactionType.getSelectionModel().getSelectedItem() == "Transfer"){
                GridPane transfer = new GridPane();
                transfer.add(new Label("Transaction Type: "),0,0);
                transfer.add(transactionType,1,0);

                transfer.add(new Label("date: "),0,1);
                transfer.add(date,1,1);

                transfer.add(new Label("amount: "),0,2);
                transfer.add(amount,1,2);

                transfer.add(new Label("description: "),0,3);
                transfer.add(description,1,3);

                transfer.add(new Label("sender: "),0,4);
                transfer.add(sender,1,4);

                transfer.add(new Label("reciever"),0,5);
                transfer.add(reciever,1,5);

                dialog.getDialogPane().setContent(transfer);
                dialog.getDialogPane().getScene().getWindow().sizeToScene();
            }
            if(transactionType.getSelectionModel().getSelectedItem()== "Payment"){
                GridPane payment = new GridPane();
                payment.add(new Label("Transaction Type: "),0,0);
                payment.add(transactionType,1,0);

                payment.add(new Label("date: "),0,1);
                payment.add(date,1,1);

                payment.add(new Label("amount: "),0,2);
                payment.add(amount,1,2);

                payment.add(new Label("description: "),0,3);
                payment.add(description,1,3);

                dialog.getDialogPane().setContent(payment);
                dialog.getDialogPane().getScene().getWindow().sizeToScene();
            }
        });

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Warning");
        alert.setHeaderText(null);
        alert.setContentText("Necessary information missing!");

        // set the grid and show dialog

        dialog.getDialogPane().setContent(general);

        //get input

        Optional result =  dialog.showAndWait();

        if(result.isPresent() && result.get() == addTransactionButton){
            if(date.getText().isEmpty()||amount.getText().isEmpty()||description.getText().isEmpty()){
                System.out.println("fehlende Eingabe");
                alert.showAndWait();
                return;
            }
            else{
                System.out.println(date.getText()+amount.getText()+description.getText());
            }
// umwandlung von text to double
            double damount = Double.parseDouble(amount.getText());


            if(transactionType.getSelectionModel().getSelectedItem() == "Payment"){
                Payment payment = new Payment(date.getText(),damount,description.getText());

                try {
                    bank.addTransaction(account, payment);
                }catch(TransactionAlreadyExistException | TransactionAttributeException e){
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setContentText("Transaction does Exits");
                    Optional<ButtonType> result2 = alert2.showAndWait();
                    System.out.println(e.getMessage());
                }
            }

            if(transactionType.getSelectionModel().getSelectedItem() =="Transfer"){
                if(sender.getText().isEmpty()||reciever.getText().isEmpty()){
                    System.out.println("fehlende Eingabe");
                    alert.showAndWait();
                    return;
                }
                if(!account.equals(sender.getText())&& !account.equals(reciever.getText()))
                {
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setContentText("Sender or Reciever is not you");
                    Optional<ButtonType> result2 = alert2.showAndWait();
                    return;
                }
                if(account.equals(sender.getText())){
                    try {
                        bank.addTransaction(account, new OutgoingTransfer(date.getText(),damount,description.getText(),sender.getText(),reciever.getText()));
                    }catch(TransactionAlreadyExistException e){
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setContentText(e.getMessage());
                    Optional<ButtonType> result2 = alert2.showAndWait();
                    System.out.println(e.getMessage());
                }
                    catch(TransactionAttributeException e){
                    Alert alert2 = new Alert(Alert.AlertType.ERROR);
                    alert2.setContentText(e.getMessage());
                    Optional<ButtonType> result2 = alert2.showAndWait();
                    System.out.println(e.getMessage());
                }
                }
                if(account.equals(reciever.getText())){
                    try {
                        bank.addTransaction(account, new IncomingTransfer(date.getText(),damount,description.getText(),sender.getText(),reciever.getText()));
                    }catch(TransactionAlreadyExistException e){
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setContentText(e.getMessage());
                        Optional<ButtonType> result2 = alert2.showAndWait();
                        System.out.println(e.getMessage());
                    }
                    catch(TransactionAttributeException e){
                        Alert alert2 = new Alert(Alert.AlertType.ERROR);
                        alert2.setContentText(e.getMessage());
                        Optional<ButtonType> result2 = alert2.showAndWait();
                        System.out.println(e.getMessage());
                    }
                }
            }
            refresh();
        } else {
            System.out.println(result.get());
        }
    }
}