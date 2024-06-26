package model;

public class Category {
	private int categoryID;
    private String categoryName;
	public Category(int categoryID, String categoryName) {
		super();
		this.categoryID = categoryID;
		this.categoryName = categoryName;
	}
	public Category(String categoryName) {
		super();
		this.categoryName = categoryName;
	}
	public Category() {
		super();
	}
	public int getCategoryID() {
		return categoryID;
	}
	public void setCategoryID(int categoryID) {
		this.categoryID = categoryID;
	}
	public String getCategoryName() {
		return categoryName;
	}
	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
	}
	@Override
	public String toString() {
		return this.categoryName;
	}
    
    
}
