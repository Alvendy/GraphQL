public class Product {
    public Long id;
    public String name;
    public Double price;
    public String category;

    public Product(Long id, String name, Double price, String category) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.category = category;
    }
}