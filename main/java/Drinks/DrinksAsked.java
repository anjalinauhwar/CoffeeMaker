package Drinks;

import java.util.ArrayList;
import java.util.List;

public class DrinksAsked {
    List<DrinkDetails> drinksOrdered = new ArrayList<>();
    public void addDrinks(DrinkDetails drinkDetails)
    {
        drinksOrdered.add(drinkDetails);
    }
    public List<DrinkDetails> getDrinksOrdered()
    {
        return drinksOrdered;
    }

}
