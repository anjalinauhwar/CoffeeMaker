package CofeeMakerMachine;

import java.util.HashMap;

public class CoffeeMachine {
    private static Integer outlets;
    HashMap<String, Integer> ingredients = new HashMap<>();
    public CoffeeMachine(Integer outlets)
    {
        this.outlets = outlets;
    }
    public int getOutlets()
    {
        return this.outlets;
    }
    public void setOutlets(int outlets)
    {
        this.outlets = outlets;
    }
    public Integer getIngredientQuantity(String ingredient)
    {
        if(ingredients.containsKey(ingredient))
            return ingredients.get(ingredient);
        else
            return 0;
    }
    public void setIngredients(String ingredientName, Integer ingredientQuantity)
    {
        ingredients.put(ingredientName,ingredientQuantity);
    }


}
