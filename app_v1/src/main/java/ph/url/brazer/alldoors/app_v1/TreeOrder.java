package ph.url.brazer.alldoors.app_v1;

import java.util.ArrayList;

/**
 * Created by User on 25.03.2014.
 */
public abstract class TreeOrder {

    private String id;
    protected static ClientRoot root;

    protected String getIdFromTree() {
        return id;
    }

    protected void setId(String id) {
        this.id = id;
    }

    public static ClientRoot getRoot() {
        if (root==null) new ClientRoot(null);
        return root;
    }
}

class ClientRoot extends TreeOrder {

    private ArrayList<ProductNode> products;
    private ProductNode curProduct;

    public ClientRoot(String id) {
        this.setId(id);
        TreeOrder.root = this;
        products = new ArrayList<ProductNode>();
    }

    public String getId() {
        return this.getIdFromTree();
    }
    public ProductNode getCurProduct() {
        return curProduct;
    }

    public ArrayList<ProductNode> getProducts() {
        return products;
    }

    public void addProductAndSetCurrentProduct(ProductNode product) {
        products.add(product);
        curProduct = product;
    }

    public void deleteProduct(String id) {
        ProductNode productToDelete = null;
        for (ProductNode p : products)
            if (id.equals(p.getId())) {
                productToDelete = p;
                break;
            }
        products.remove(productToDelete);
        if (productToDelete==curProduct)
            curProduct = null;
    }

    public double getFullPrice() {
        double sum = 0;
        for (ProductNode p : products)
            sum += p.getFullPrice()*p.getQuantity();
        return sum;
    }

}

class ProductNode extends TreeOrder {

    private double price;
    private ArrayList<OptionLeaf> options;
    private int quantity = 0;
    private boolean isInstalled;

    public ProductNode(String id, double price) {
        this.setId(id);
        this.price = price;
        options = new ArrayList<OptionLeaf>();
    }

    public String getId() {
        return this.getIdFromTree();
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public boolean isInstalled() {
        return isInstalled;
    }

    public void setInstalled(boolean isInstalled) {
        this.isInstalled = isInstalled;
    }

    public void addOption(OptionLeaf option) {
        options.add(option);
    }

    public OptionLeaf getOption(int n) {
        return  options.get(n);
    }

    public ArrayList<OptionLeaf> getOptions() {
        return  options;
    }

    public double getFullPrice() {
        double sum = price;
        for (OptionLeaf option : options)
            sum += option.getAddedPrice();
        return sum;
    }

}

class OptionLeaf extends TreeOrder {

    private String name;
    private String value;
    private double addedPrice;

    public OptionLeaf(String name, String value, double addedPrice) {
        this.name = name;
        this.value = value;
        this.addedPrice = addedPrice;
    }

    public double getAddedPrice() {
        return addedPrice;
    }

    public String getName() {
        return name;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public void setAddedPrice(double addedPrice) {
        this.addedPrice = addedPrice;
    }

}