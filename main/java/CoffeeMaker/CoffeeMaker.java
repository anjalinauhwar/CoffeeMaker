package CoffeeMaker;

import CofeeMakerMachine.CoffeeMachine;
import Drinks.DrinkDetails;
import Drinks.DrinksAsked;
import Drinks.StatusOfDrink;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.FileReader;
import java.util.Map;

public class CoffeeMaker {

    // number of drinks prepared by the coffee Maker
    private  int drinksServed;
    // drinksAsked contains the status of all the drinks ordered from the coffee Maker
    private  DrinksAsked drinksAsked;
    private  CoffeeMachine coffeeMachine;

    public CoffeeMaker(int drinksServed, CoffeeMachine coffeeMachine, DrinksAsked drinksAsked)
    {
        this.drinksServed = drinksServed;
        this.coffeeMachine = coffeeMachine;
        this.drinksAsked = drinksAsked;
    }
    public  void processOrders(JsonObject inputTestCase)
    {
        try{
            JsonObject machine = inputTestCase.getAsJsonObject("machine");
            setMachine(machine);
            refillIngredientsInMachine(machine);
            getDrinks(machine);
        }
        catch (Exception e)
        {
            // unable to process all the orders due to some error
            System.out.println("Please try again!!");
        }
    }
    // set up the coffee Machine by setting up ingredients and the number of outlets of the machine
    private  void setMachine(JsonObject machine)
    {
        try{
            Integer outlets = machine.getAsJsonObject("outlets").get("count_n").getAsInt();
            coffeeMachine.setOutlets(outlets);

            //setting up ingredients with the quantity given
            JsonObject ingredients = machine.getAsJsonObject("total_items_quantity");
            for(Map.Entry<String, JsonElement> entry : ingredients.entrySet()) {
                coffeeMachine.setIngredients(entry.getKey(), entry.getValue().getAsInt());
            }
        }
        catch (Exception e)
        {
            //error occurred while setting up the ingredients
            System.out.println("Sorry for inconvience!! Please try after Some time.");
            throw e;
        }
    }
    private void getDrinks(JsonObject machine)
    {
        try{
            JsonObject beverages = machine.getAsJsonObject("beverages");
            for(Map.Entry<String, JsonElement> entry : beverages.entrySet()) {
                String drinkName = entry.getKey();

                // when all the outlets are being used to serve other drinks
                if(drinksServed==coffeeMachine.getOutlets())
                {
                    DrinkDetails drinkDetails = new DrinkDetails(drinkName, StatusOfDrink.NO_OUTLET_AVAILABLE, "");
                    drinksAsked.addDrinks(drinkDetails);
                    System.out.println(drinkName+ " can not be prepared as no outlet is available.");
                    continue;
                }
                JsonObject recipe = entry.getValue().getAsJsonObject();
                boolean ingredientsSufficient = true;
                for(Map.Entry<String, JsonElement> ingredients : recipe.entrySet()) {
                    String ingredientRequired = ingredients.getKey();
                    Integer ingredientAmountRequired = ingredients.getValue().getAsInt();

                    // quantity of ingredients required is more than the available amount of the ingredients
                    if(coffeeMachine.getIngredientQuantity(ingredientRequired)<ingredientAmountRequired)
                    {
                        ingredientsSufficient = false;
                        if(coffeeMachine.getIngredientQuantity(ingredientRequired)==0)
                        {
                            DrinkDetails drinkDetails = new DrinkDetails(drinkName, StatusOfDrink.NOT_AVAILABLE_INGREDIENT, ingredientRequired);
                            System.out.println(drinkName+ " can not be prepared as "+ ingredientRequired + " is not available.");
                            drinksAsked.addDrinks(drinkDetails);
                        } else
                        {
                            DrinkDetails drinkDetails = new DrinkDetails(drinkName, StatusOfDrink.NOT_SUFFICIENT_INGREDIENT, ingredientRequired);
                            System.out.println(drinkName+ " can not be prepared as "+ ingredientRequired + " is not sufficient.");
                            drinksAsked.addDrinks(drinkDetails);
                        }

                        break;
                    }
                }

                // All the ingredients are available for making the drink
                if(ingredientsSufficient)
                {
                    for(Map.Entry<String, JsonElement> ingredients : recipe.entrySet()) {
                        String ingredientRequired = ingredients.getKey();
                        Integer ingredientAmountRequired = ingredients.getValue().getAsInt();
                        coffeeMachine.setIngredients(ingredientRequired,coffeeMachine.getIngredientQuantity(ingredientRequired)-ingredientAmountRequired);

                        //Show indicator for ingredients who are running out of stock
                        if( coffeeMachine.getIngredientQuantity(ingredientRequired)==0)
                        {
                            showIndicatorForIngredient(ingredientRequired);
                        }
                    }
                    // Drink has been prepared successfully
                    DrinkDetails drinkDetails = new DrinkDetails(drinkName, StatusOfDrink.PREPARED, "");
                    System.out.println(drinkName+ " is prepared");
                    drinksAsked.addDrinks(drinkDetails);
                    drinksServed++;
                }

            }
        }
        catch (Exception e)
        {
            System.out.println("There is an problem in machine. Drinks can not be prepared!!");
        }
    }

    // When some ingredient is running out of stock
    private static void showIndicatorForIngredient(String ingredient)
    {
        System.out.println(ingredient + "is running out of stock. Please Refill it.");
    }
    private void refillIngredientsInMachine(JsonObject machine)
    {
        try{
            //refilling the ingredients with the quantity given
            JsonObject ingredients = machine.getAsJsonObject("refill_quantity");
            for(Map.Entry<String, JsonElement> ingredient : ingredients.entrySet()) {
                coffeeMachine.setIngredients(
                        ingredient.getKey(),
                        coffeeMachine.getIngredientQuantity(
                                ingredient.getKey()) + ingredient.getValue().getAsInt());
            }
        }
        catch (Exception e)
        {
            //error occurred while refilling up the ingredients
            System.out.println("Unable to refill the ingredients" + e);
        }
    }

}
