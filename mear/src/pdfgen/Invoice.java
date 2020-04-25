package pdfgen;

public class Invoice {
	private int customer;
	private String lpo;
	private String total;
	private String vat;
	private String net;
	private String terms;
	private String createdDate;
	private String quatationNo;
	private String amtInWords;
	private String discount;
	private String dueDate;

	public String getDueDate() {
		return dueDate;
	}
	public void setDueDate(String dueDate) {
		this.dueDate = dueDate;
	}
	public String getDiscount() {
		return discount;
	}
	public void setDiscount(String discount) {
		this.discount = discount;
	}
	public String getAmtInWords() {
		return amtInWords;
	}
	public void setAmtInWords(String amtInWords) {
		this.amtInWords = amtInWords;
	}
	public String getQuatationNo() {
		return quatationNo;
	}
	public void setQuatationNo(String quatationNo) {
		this.quatationNo = quatationNo;
	}
	public int getCustomer() {
		return customer;
	}
	public void setCustomer(int customer) {
		this.customer = customer;
	}
	public String getLpo() {
		return lpo;
	}
	public void setLpo(String lpo) {
		this.lpo = lpo;
	}
	public String getTotal() {
		return total;
	}
	public void setTotal(String total) {
		this.total = total;
	}
	public String getVat() {
		return vat;
	}
	public void setVat(String vat) {
		this.vat = vat;
	}
	public String getNet() {
		return net;
	}
	public void setNet(String net) {
		this.net = net;
	}
	public String getTerms() {
		return terms;
	}
	public void setTerms(String terms) {
		this.terms = terms;
	}
	public String getCreatedDate() {
		return createdDate;
	}
	public void setCreatedDate(String created_date) {
		this.createdDate = created_date;
	}
	
}
