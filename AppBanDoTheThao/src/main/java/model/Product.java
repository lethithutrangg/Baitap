package model;

public class Product {
	 private int productID;
    private String productName;
    private int categoryID;
    private String description;
    private String size;
    private String color;
    private float price;
    private int quantity;
    private String image;
	public Product(int productID, String productName, int categoryID, String description, String size, String color,
			float price, int quantity, String image) {
		super();
		this.productID = productID;
		this.productName = productName;
		this.categoryID = categoryID;
		this.description = description;
		this.size = size;
		this.color = color;
		this.price = price;
		this.quantity = quantity;
		this.image = image;
	}
	public Product(String productName, int categoryID, String description, String size, String color, float price,
			int quantity, String image) {
		super();
		this.productName = productName;
		this.categoryID = categoryID;
		this.description = description;
		this.size = size;
		this.color = color;
		this.price = price;
		this.quantity = quantity;
		this.image = image;
	}
	public Product() {
		super();
	}
	public int getProductID() {
		return productID;
	}
	public void setProductID(int productID) {
		this.productID = productID;
	}
	public String getProductName() {
		return productName;
	}
	public void setProductName(String productName) {
		this.productName = productName;
	}
	public int getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getSize() {
		return size;
	}
	public void setSize(String size) {
		this.size = size;
	}
	public String getColor() {
		return color;
	}
	public void setColor(String color) {
		this.color = color;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public String getImage() {
		return image;
	}
	public void setImage(String image) {
		this.image = image;
	}
    
    
}
