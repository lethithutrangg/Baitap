package model;

import java.util.Date;

public class Invoice {
	private int invoiceID;
    private int userID;
    private Date createdDate;
    private float totalPrice;
	public Invoice(int invoiceID, int userID, Date createdDate, float totalPrice) {
		super();
		this.invoiceID = invoiceID;
		this.userID = userID;
		this.createdDate = createdDate;
		this.totalPrice = totalPrice;
	}
	public Invoice(int userID, Date createdDate, float totalPrice) {
		super();
		this.userID = userID;
		this.createdDate = createdDate;
		this.totalPrice = totalPrice;
	}
	public Invoice() {
		super();
	}
	public int getInvoiceID() {
		return invoiceID;
	}
	public void setInvoiceID(int invoiceID) {
		this.invoiceID = invoiceID;
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public Date getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(Date createdDate) {
		this.createdDate = createdDate;
	}
	public float getTotalPrice() {
		return totalPrice;
	}
	public void setTotalPrice(float totalPrice) {
		this.totalPrice = totalPrice;
	}
    
    
}
