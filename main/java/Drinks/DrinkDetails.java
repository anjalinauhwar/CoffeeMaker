package Drinks;

public class DrinkDetails {
    private String name;
    private StatusOfDrink statusOfDrink;
    private String ingredientMissing;
    public DrinkDetails(String name, StatusOfDrink statusOfDrink, String ingredient)
    {
        this.name = name;
        this.statusOfDrink = statusOfDrink;
        this.ingredientMissing = ingredient;
    }
}
