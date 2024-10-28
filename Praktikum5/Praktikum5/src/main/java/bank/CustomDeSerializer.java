package bank;

import bank.Exceptions.TransactionAttributeException;
import com.google.gson.*;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 *  Die unterschiedlichen Typen von Transaktionen in das JSON-Format serialisieren und deserialisieren
 */
public class CustomDeSerializer implements JsonSerializer<List<Transaction>>, JsonDeserializer<List<Transaction>>
{
    @Override
    public JsonElement serialize(List<Transaction> transactionsList, Type type, JsonSerializationContext jsonSerializationContext)
    {
        JsonArray jsonArrayTransactions = new JsonArray();

        for(Transaction transaction : transactionsList) {
            JsonObject transactionType = new JsonObject();
            JsonObject transactionDetails = new JsonObject();

            if (transaction instanceof Transfer) {
                transactionDetails.addProperty("sender", ((Transfer) transaction).getSender());
                transactionDetails.addProperty("recipient", ((Transfer) transaction).getRecipient());
            } else if (transaction instanceof Payment) {
                transactionDetails.addProperty("incomingInterest", ((Payment) transaction).getIncomingInterest());
                transactionDetails.addProperty("outgoingInterest", ((Payment) transaction).getOutgoingInterest());
            }

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
        JsonArray transactionJsonArray = jsonElement.getAsJsonArray();

        List<Transaction> transactionsList = new ArrayList<>();

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