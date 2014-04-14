package ph.url.brazer.alldoors.app_v1;

public class Item {

    private String name;
    private String id = "";
    private double price = 0;

    public Item(String name) {
        this.name = name;
    }
    public Item(String name, String id) {
        this.name = name;
        this.id = id;
    }
    public Item(String name, double price) {
        this.name = name;
        this.price = price;
    }
    public Item(String name, String id, double price) {
        this.name = name;
        this.id = id;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return id;
    }

    public double getPrice() {
        return price;
    }

}
