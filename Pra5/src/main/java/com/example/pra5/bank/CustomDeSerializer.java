package com.example.pra5.bank;

import com.example.pra5.bank.exceptions.*;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *  Die unterschiedlichen Typen von Transaktionen in das JSON-Format serialisieren und deserialisieren
 */
//Had l Klasse kat implementi JSON serialize w JSON Deserialize
public class CustomDeSerializer implements JsonSerializer<List<Transaction>>, JsonDeserializer<List<Transaction>>
{
    //TransactionDetails kayna west TransactionType
    @Override
    public JsonElement serialize(List<Transaction> transactionsList, Type type, JsonSerializationContext jsonSerializationContext)
    {
        JsonArray jsonArrayTransactions = new JsonArray(); //We creat a JsonArray

        for(Transaction transaction : transactionsList) {
            JsonObject transactionType = new JsonObject(); //Here we create a JSON Object to load the "TYPE OF THE Transaction" and the Details
            JsonObject transactionDetails = new JsonObject();//a JSON Object to load the Transaction Details

            if (transaction instanceof Transfer) {
                transactionDetails.addProperty("sender", ((Transfer) transaction).getSender()); //Add the Sender to the Transaction Details
                transactionDetails.addProperty("recipient", ((Transfer) transaction).getRecipient()); //Add the Recipient to the Transaction Details
            } else if (transaction instanceof Payment) {
                transactionDetails.addProperty("incomingInterest", ((Payment) transaction).getIncomingInterest()); //Add the incomingInterest to the Transaction Details
                transactionDetails.addProperty("outgoingInterest", ((Payment) transaction).getOutgoingInterest()); //Add the outgoingInterest to the Transaction Details
            }
            // Add the commun Infos to the Details
            transactionDetails.addProperty("date", transaction.getDate());
            transactionDetails.addProperty("amount", transaction.getAmount());
            transactionDetails.addProperty("description", transaction.getDescription());

            transactionType.addProperty("CLASSNAME", transaction.getClass().getSimpleName());
            transactionType.add("INSTANCE", transactionDetails);

            jsonArrayTransactions.add(transactionType);
        }
        return jsonArrayTransactions;
    }

    @Override
    public List<Transaction> deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonArray transactionJsonArray = jsonElement.getAsJsonArray(); //My JSON Array

        List<Transaction> transactionsList = new ArrayList<>(); //Where the Transactions will be added

        for (JsonElement transactionJsonElement : transactionJsonArray) {
            JsonObject transactionJsonObject = transactionJsonElement.getAsJsonObject();
            String classname = transactionJsonObject.get("CLASSNAME").getAsString();
            JsonObject instance = transactionJsonObject.get("INSTANCE").getAsJsonObject();

            if (classname.equals("Payment")) {
                try {
                    transactionsList.add(new Payment(
                            instance.get("date").getAsString(),
                            instance.get("amount").getAsDouble(),
                            instance.get("description").getAsString(),
                            instance.get("incomingInterest").getAsDouble(),
                            instance.get("outgoingInterest").getAsDouble()
                    ));
                } catch (TransactionAttributeException e) {
                    e.printStackTrace();
                }
            } else if (classname.equals("IncomingTransfer")) {
                try {
                    transactionsList.add( new IncomingTransfer(
                            instance.get("date").getAsString(),
                            instance.get("amount").getAsDouble(),
                            instance.get("description").getAsString(),
                            instance.get("sender").getAsString(),
                            instance.get("recipient").getAsString()
                    ));
                } catch (TransactionAttributeException e) {
                    e.printStackTrace();
                }
            } else if (classname.equals("OutgoingTransfer")) {
                try {
                    transactionsList.add( new OutgoingTransfer(
                            instance.get("date").getAsString(),
                            instance.get("amount").getAsDouble(),
                            instance.get("description").getAsString(),
                            instance.get("sender").getAsString(),
                            instance.get("recipient").getAsString()
                    ));
                } catch (TransactionAttributeException e) {
                    e.printStackTrace();
                }
            }
        }
        return transactionsList;
    }
}