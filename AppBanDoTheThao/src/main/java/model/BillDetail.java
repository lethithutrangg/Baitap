package model;

public class BillDetail {
	private int billDetailID;
    private int invoiceID;
    private int productID;
    private int quantity;
    private float price;
	public BillDetail(int billDetailID, int invoiceID, int productID, int quantity, float price) {
		super();
		this.billDetailID = billDetailID;
		this.invoiceID = invoiceID;
		this.productID = productID;
		this.quantity = quantity;
		this.price = price;
	}
	public BillDetail(int productID, int quantity, float price) {
		super();
		this.productID = productID;
		this.quantity = quantity;
		this.price = price;
	}
	public int getBillDetailID() {
		return billDetailID;
	}
	public void setBillDetailID(int billDetailID) {
		this.billDetailID = billDetailID;
	}
	public int getInvoiceID() {
		return invoiceID;
	}
	public void setInvoiceID(int invoiceID) {
		this.invoiceID = invoiceID;
	}
	public int getProductID() {
		return productID;
	}
	public void setProductID(int productID) {
		this.productID = productID;
	}
	public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}

    
}
