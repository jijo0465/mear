package pdfgen;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;




public class DoGenerator extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private List<InvoiceItem> invoiceList;

	public void doGet(HttpServletRequest req,HttpServletResponse res)  
			throws ServletException,IOException  
			{ 
		String doDisplayNo;
		String doNo = req.getParameter("do_no");
		String rowId = req.getParameter("row_id");
		String appName = req.getParameter("app");
		String targetURL = "http://34.68.126.118/do/print/?id="+doNo+"&row_id="+rowId;
		HttpURLConnection connection = null;
		URL url = new URL(targetURL);
		connection = (HttpURLConnection) url.openConnection();
		connection.setRequestMethod("GET");
	    InputStream is = connection.getInputStream();
	    BufferedReader rd = new BufferedReader(new InputStreamReader(is));
	    StringBuilder response = new StringBuilder(); 
	    String line;
	    while ((line = rd.readLine()) != null) {
	      response.append(line);
	      response.append('\r');
	    }
	    String payloadRequest = response.toString();
	    JSONParser parser = new JSONParser();
		JSONObject obj = new JSONObject();
		JSONObject customerObj = new JSONObject();
		JSONObject invoiceObj = new JSONObject();
		JSONObject trnObj = new JSONObject();
		JSONArray objArray = new JSONArray();
		
		Customer customer = new Customer();
		Invoice invoice = new Invoice();
		AppTrn appTrn = new AppTrn();
		
		invoiceList = new ArrayList<>();
		try {
			obj = (JSONObject)parser.parse(payloadRequest);
			String customerDetails = new StringBuilder(obj.get("customer").toString())
					.deleteCharAt(obj.get("customer").toString().length()-1)
					.deleteCharAt(0)
					.toString();
			
			String doData = new StringBuilder(obj.get("do_details").toString())
					.deleteCharAt(obj.get("do_details").toString().length()-1)
					.deleteCharAt(0)
					.toString();
			String doItems = obj.get("do_items").toString();
			String trnData = new StringBuilder(obj.get("settings").toString())
					.deleteCharAt(obj.get("settings").toString().length()-1)
					.deleteCharAt(0)
					.toString();

			customerObj = (JSONObject)parser.parse(customerDetails);
			invoiceObj = (JSONObject)parser.parse(doData);
			trnObj = (JSONObject)parser.parse(trnData);
			objArray = (JSONArray)parser.parse(doItems);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		customer.setCustomerProfileConPer(customerObj.get("customerprofile__con_per").toString());
		customer.setCustomerProfileEmail(customerObj.get("customerprofile__email").toString());
		customer.setCustomerProfileInvAddr(customerObj.get("customerprofile__inv_addr").toString());
		customer.setCustomerProfilePhone(customerObj.get("customerprofile__phone").toString());
		customer.setCustomerProfileProAddr(customerObj.get("customerprofile__pro_addr").toString());
		customer.setName(customerObj.get("name").toString());
		customer.setTrn(customerObj.get("trn").toString());
		
		invoice.setCustomer(Integer.parseInt(invoiceObj.get("customer").toString()));
		invoice.setLpo(invoiceObj.get("lpo").toString());
		invoice.setTotal(invoiceObj.get("total").toString());
		invoice.setNet(invoiceObj.get("net").toString());
		invoice.setTerms(invoiceObj.get("terms").toString());
		invoice.setCreatedDate(invoiceObj.get("quo_date").toString());
		invoice.setQuatationNo(invoiceObj.get("quo_no").toString());
		invoice.setAmtInWords(invoiceObj.get("words").toString());
		invoice.setDiscount(invoiceObj.get("discount").toString());
		invoice.setVat(invoiceObj.get("vat").toString());
		doDisplayNo=invoiceObj.get(rowId).toString().replace('-', '/');
		
		appTrn.setTrn(trnObj.get("format").toString());
		for(int i = 0; i<objArray.size();i++) {
			System.out.println(i);
			System.out.println(rowId);
			JSONObject object = new JSONObject();
			object = (JSONObject)objArray.get(i);
			InvoiceItem invoiceItem = new InvoiceItem();
			invoiceItem.setId(Integer.parseInt(object.get("id").toString()));
			invoiceItem.setQuoNo(object.get("quo_no").toString().replace('-', '/'));
			invoiceItem.setInventoryItemCode(object.get("inventory__item_code").toString());
			invoiceItem.setInventoryDescription(object.get("inventory__description").toString());
			invoiceItem.setUom(object.get("uom").toString());
			invoiceItem.setQuantity(object.get(rowId).toString());
			invoiceList.add(invoiceItem);
		}
		CreateDo ci =new CreateDo(appName,"/app/"+appName+"/DO_"+doNo+rowId+".pdf",customer,invoice,appTrn,doDisplayNo);
		ci.create(invoiceList);
		CreateHeaderlessDo cii =new CreateHeaderlessDo(appName,"/app/"+appName+"/DO_"+doNo+rowId+"_no_header.pdf",customer,invoice,appTrn,doDisplayNo);
		cii.create(invoiceList);
	    rd.close();
			}
	public void doPost(HttpServletRequest request,HttpServletResponse res) throws ServletException,IOException {
//		String payloadRequest = getBody(request);
//		JSONParser parser = new JSONParser();
//		JSONObject obj = new JSONObject();
//		JSONObject customerObj = new JSONObject();
//		JSONObject invoiceObj = new JSONObject();
//		JSONArray objArray = new JSONArray();
//		Customer customer = new Customer();
//		Invoice invoice = new Invoice();
//		invoiceList = new ArrayList<>();
//		try {
//			obj = (JSONObject)parser.parse(payloadRequest);
//			String customerDetails = obj.get("customer").toString();
//			String invoiceItems = obj.get("quo_items").toString();
//			String invoiceData = obj.get("quo_details").toString();
//			customerObj = (JSONObject)parser.parse(customerDetails);
//			invoiceObj = (JSONObject)parser.parse(invoiceData);
//			System.out.println(invoiceItems);
//			objArray = (JSONArray)parser.parse(invoiceItems);
//		} catch (ParseException e) {
//			e.printStackTrace();
//		}
//		customer.setCustomerProfileConPer(customerObj.get("customerprofile__con_per").toString());
//		customer.setCustomerProfileEmail(customerObj.get("customerprofile__email").toString());
//		customer.setCustomerProfileInvAddr(customerObj.get("customerprofile__inv_addr").toString());
//		customer.setCustomerProfilePhone(customerObj.get("customerprofile__phone").toString());
//		customer.setCustomerProfileProAddr(customerObj.get("customerprofile__pro_addr").toString());
//		customer.setName(customerObj.get("name").toString());
//		customer.setTrn(invoiceObj.get("trn").toString());
//		
//		invoice.setCustomer(Integer.parseInt(invoiceObj.get("customer").toString()));
//		invoice.setLpo(invoiceObj.get("lpo").toString());
//		invoice.setTotal(invoiceObj.get("total").toString());
//		invoice.setNet(invoiceObj.get("net").toString());
//		invoice.setTerms(invoiceObj.get("terms").toString());
//		invoice.setCreatedDate(invoiceObj.get("created_date").toString());
//		invoice.setQuatationNo(invoiceObj.get("quo_no").toString());
//		
//		for(int i = 0; i<objArray.size();i++) {
//			JSONObject object = new JSONObject();
//			object = (JSONObject)objArray.get(i);
//			InvoiceItem invoiceItem = new InvoiceItem();
//			invoiceItem.setId(Integer.parseInt(object.get("id").toString()));
//			invoiceItem.setQuoNo(object.get("quo_no").toString());
//			invoiceItem.setInventoryItemCode(object.get("inventory__item_code").toString());
//			invoiceItem.setInventoryDescription(object.get("inventory__description").toString());
//			invoiceItem.setUom(object.get("uom").toString());
//			invoiceItem.setInventoryPrice(object.get("inventory__price").toString());
//			invoiceItem.setQuantity(object.get("quantity").toString());
//			invoiceItem.setVat(object.get("vat").toString());
//			invoiceItem.setTotal(object.get("total").toString());
//			invoiceList.add(invoiceItem);
//		}
//		CreateInvoice ci =new CreateInvoice("/Invoices/test.pdf",customer,invoice);
//		ci.create(invoiceList);
		
	}



}
