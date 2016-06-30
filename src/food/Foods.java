package food;
/*
 * Store all the foods in an enum for easy change in the future and easy access to food data.
 * Each case of food has an associated name and price.
 * This is done in order to keep the food's information constant across the application.
 */
enum Foods {
    HOTDOG("Hot Dog",2.50), BRAT("Brat",3.50), HAMBURGER("Hamburger",5.00), FRIES("Fries",2.00), SODA("Soda",2.00), WATER("Water",0.00);

    private String name;
    private double price;
    private Foods(String name, double price) {
        this.name = name;
        this.price = price;
    }

    public String getName() { return name; }
    public double getPrice() { return price; }
}
