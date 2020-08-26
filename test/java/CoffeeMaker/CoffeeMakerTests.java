package CoffeeMaker;

import CofeeMakerMachine.CoffeeMachine;
import Drinks.DrinkDetails;
import Drinks.DrinksAsked;
import Drinks.StatusOfDrink;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.mockito.internal.matchers.apachecommons.ReflectionEquals;

public class CoffeeMakerTests {

    private  int drinksServed=0;
    private  CoffeeMachine coffeeMachine= new CoffeeMachine(0);
    private  DrinksAsked drinksAsked = new DrinksAsked();
    private  JsonObject jsonInput;
    private  JsonObject jsonEmptyOrderRequest;
    CoffeeMaker coffeeMaker;

    @Before
    public void setup()
    {
        coffeeMaker = new CoffeeMaker(drinksServed, coffeeMachine, drinksAsked);
        String stringInputIngredients = "{\"machine\":{\"outlets\":{\"count_n\":1}, \"refill_quantity\":{\"hot_water\":5}, \"total_items_quantity\":{\"hot_water\":100, \"hot_milk\":50, \"tea_leaves_syrup\":50}, \"beverages\":{\"hot_milk\":{\"hot_water\":60, \"hot_milk\":150}, \"tea\":{\"hot_water\":10, \"tea_leaves\":5},\"hot_water\":{\"hot_water\":55} , \"water\":{\"hot_water\":5}}}}";
        jsonInput = new JsonParser().parse(stringInputIngredients).getAsJsonObject();
        String stringEmptyOrderRequest = "{\"machine\":{\"outlets\":{\"count_n\":2}, \"total_items_quantity\":{\"hot_water\":100, \"hot_milk\":50, \"tea_leaves_syrup\":50}}}";
        jsonEmptyOrderRequest = new JsonParser().parse(stringEmptyOrderRequest).getAsJsonObject();
    }

    @Test
    public void testProcessOrders_EmptyJsonObject()
    {
        coffeeMaker.processOrders(new JsonObject());

        Assert.assertEquals(coffeeMachine.getOutlets(),0);

        Assert.assertEquals((long)coffeeMachine.getIngredientQuantity("hot_water"),0);
        Assert.assertEquals((long)coffeeMachine.getIngredientQuantity("hot_milk"),0);
        Assert.assertEquals((long)coffeeMachine.getIngredientQuantity("tea_leaves_syrup"),0);

        Assert.assertEquals(drinksAsked.getDrinksOrdered().size(),0);

    }
    @Test
    public void testProcessOrders_EmptyOrderRequest()
    {
        coffeeMaker.processOrders(jsonEmptyOrderRequest);

        Assert.assertEquals(coffeeMachine.getOutlets(),2);

        Assert.assertEquals((long)coffeeMachine.getIngredientQuantity("hot_water"),100);
        Assert.assertEquals((long)coffeeMachine.getIngredientQuantity("hot_milk"),50);
        Assert.assertEquals((long)coffeeMachine.getIngredientQuantity("tea_leaves_syrup"),50);

        Assert.assertEquals(drinksAsked.getDrinksOrdered().size(),0);

    }

    @Test
    public void testProcessOrders_HappyCase()
    {
        coffeeMaker.processOrders(jsonInput);

        Assert.assertEquals(coffeeMachine.getOutlets(),1);

        Assert.assertEquals((long)coffeeMachine.getIngredientQuantity("hot_water"),50);
        Assert.assertEquals((long)coffeeMachine.getIngredientQuantity("hot_milk"),50);
        Assert.assertEquals((long)coffeeMachine.getIngredientQuantity("tea_leaves_syrup"),50);

        Assert.assertEquals(drinksAsked.getDrinksOrdered().size(),4);


        DrinkDetails drinkDetails1 = new DrinkDetails("hot_milk", StatusOfDrink.NOT_SUFFICIENT_INGREDIENT,"hot_milk");
        Assert.assertTrue(new ReflectionEquals(drinkDetails1).matches(drinksAsked.getDrinksOrdered().get(0)));

        DrinkDetails drinkDetails2 = new DrinkDetails("tea", StatusOfDrink.NOT_AVAILABLE_INGREDIENT,"tea_leaves");
        Assert.assertTrue(new ReflectionEquals(drinkDetails2).matches(drinksAsked.getDrinksOrdered().get(1)));

        DrinkDetails drinkDetails3 = new DrinkDetails("hot_water", StatusOfDrink.PREPARED,"");
        Assert.assertTrue(new ReflectionEquals(drinkDetails3).matches(drinksAsked.getDrinksOrdered().get(2)));

        DrinkDetails drinkDetails4 = new DrinkDetails("water", StatusOfDrink.NO_OUTLET_AVAILABLE,"");
        Assert.assertTrue(new ReflectionEquals(drinkDetails4).matches(drinksAsked.getDrinksOrdered().get(3)));

    }

}
