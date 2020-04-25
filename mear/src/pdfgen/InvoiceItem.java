package pdfgen;

public class InvoiceItem {
	public int id;
	public String quoNo;
	public String inventoryItemCode;
	public String inventoryDescription;
	public String uom;
	public String inventoryPrice;
	public String quantity;
	public String vat;
	public String total;
	public String discount;
	
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getQuoNo() {
		return quoNo;
	}
	public void setQuoNo(String quoNo) {
		this.quoNo = quoNo;
	}
	public String getInventoryItemCode() {
		return inventoryItemCode;
	}
	public void setInventoryItemCode(String inventoryItemCode) {
		this.inventoryItemCode = inventoryItemCode;
	}
	public String getInventoryDescription() {
		return inventoryDescription;
	}
	public void setInventoryDescription(String inventoryDescription) {
		this.inventoryDescription = inventoryDescription;
	}
	public String getUom() {
		return uom;
	}
	public void setUom(String uom) {
		this.uom = uom;
	}
	public String getInventoryPrice() {
		return inventoryPrice;
	}
	public void setInventoryPrice(String inventoryPrice) {
		this.inventoryPrice = inventoryPrice;
	}
	public String getQuantity() {
		return quantity;
	}
	public void setQuantity(String quantity) {
		this.quantity = quantity;
	}
	public String getVat() {
		return vat;
	}
	public void setVat(String vat) {
		this.vat = vat;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
}
