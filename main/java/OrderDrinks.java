import CofeeMakerMachine.CoffeeMachine;
import CoffeeMaker.CoffeeMaker;
import Drinks.DrinksAsked;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;

public class OrderDrinks {

    // path of the input test case file which is in json format
    private static final String orderInputPath = "./sampleInput.json";

    public static void main(String[] args) {
        try {
            CoffeeMaker coffeeMaker = new CoffeeMaker(0, new CoffeeMachine(0), new DrinksAsked());
            FileReader reader = new FileReader(ClassLoader.getSystemResource(orderInputPath).getFile());
            JsonParser jsonParser = new JsonParser();
            JsonObject inputTestCase = (JsonObject) jsonParser.parse(reader);
            coffeeMaker.processOrders(inputTestCase);
        }
        catch (Exception ex)
        {
            System.out.println("Unable to set CoffeeMaker.");
        }

    }
}
