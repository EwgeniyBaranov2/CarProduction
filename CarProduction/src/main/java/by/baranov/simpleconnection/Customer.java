package by.baranov.simpleconnection;

public class Customer {
	private int id;
	private String name;
	private String date;
	private int addressId;
	public Customer(int id, String name, String date, int addressId){
		this.id = id;
		this.name = name;
		this.date = date;
		this.addressId = addressId;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getDate() {
		return date;
	}
	public void setDate(String date) {
		this.date = date;
	}	
	public int getAddressId() {
		return addressId;
	}
	public void setAddressId(int addressId) {
		this.addressId = addressId;
	}
	@Override
	public String toString() {
		return "Customer [id=" + id + ", name=" + name + ", date=" + date + ", addressId=" + addressId + "]";
	}
	
	
}
