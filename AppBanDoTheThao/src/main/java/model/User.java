package model;

public class User {
    private int userID;
    private String email;
    private String password;
    private String fullname;
    private String phone;
    private String gender;
    private int age;
    private String address;
    private int roleID;
	public User(int userID, String email, String password, String fullname, String phone, String gender, int age,
			String address, int roleID) {
		super();
		this.userID = userID;
		this.email = email;
		this.password = password;
		this.fullname = fullname;
		this.phone = phone;
		this.gender = gender;
		this.age = age;
		this.address = address;
		this.roleID = roleID;
	}
	public User(String email, String password, String fullname, String phone, String gender, int age, String address,
			int roleID) {
		super();
		this.email = email;
		this.password = password;
		this.fullname = fullname;
		this.phone = phone;
		this.gender = gender;
		this.age = age;
		this.address = address;
		this.roleID = roleID;
	}
	public User() {
		super();
	}
	public int getUserID() {
		return userID;
	}
	public void setUserID(int userID) {
		this.userID = userID;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getFullname() {
		return fullname;
	}
	public void setFullname(String fullname) {
		this.fullname = fullname;
	}
	public String getPhone() {
		return phone;
	}
	public void setPhone(String phone) {
		this.phone = phone;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getAddress() {
		return address;
	}
	public void setAddress(String address) {
		this.address = address;
	}
	public int getRoleID() {
		return roleID;
	}
	public void setRoleID(int roleID) {
		this.roleID = roleID;
	}
	
    
}
