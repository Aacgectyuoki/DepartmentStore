class Product {
    private int productId;
    private String productName;
    private double buyingPrice;
    private double sellingPrice;
    private int availableQuantity;
    private String category;

    public Product(int productId, String productName, double buyingPrice, int availableQuantity, String category) {
        this.productId = productId;
        this.productName = productName;
        this.buyingPrice = buyingPrice;
        this.sellingPrice = buyingPrice + (buyingPrice * 0.5);
        this.availableQuantity = availableQuantity;
        this.category = category;
    }

    public int getProductId() { return productId; }
    public String getProductName() { return productName; }
    public double getBuyingPrice() { return buyingPrice; }
    public double getSellingPrice() { return sellingPrice; }
    public int getAvailableQuantity() { return availableQuantity; }
    public String getCategory() { return category; }

    public void setAvailableQuantity(int quantity) { this.availableQuantity = quantity; }

    @Override
    public String toString() {
        return "Product ID: " + productId +
                ", Name: " + productName +
                ", Buying Price: $" + buyingPrice +
                ", Selling Price: $" + sellingPrice +
                ", Quantity: " + availableQuantity +
                ", Category: " + category;
    }
}
