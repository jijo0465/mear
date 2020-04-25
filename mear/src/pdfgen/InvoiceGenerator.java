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




public class InvoiceGenerator extends HttpServlet{
	private static final long serialVersionUID = 1L;
	private List<InvoiceItem> invoiceList;

	public void doGet(HttpServletRequest req,HttpServletResponse res)  
			throws ServletException,IOException  
			{ 
		String doDisplayNo;
		String invoiceNo = req.getParameter("id");
		String rowId = req.getParameter("row_id");
		String invDisp = req.getParameter("inv_dis");
		String appName = req.getParameter("app");
		System.out.println("App Name"+appName);
		String[] values = rowId.split(",");
		String targetURL = "http://34.68.126.118/in/print/?id="+invoiceNo+"&inv_dis="+invDisp+"&row_id="+rowId;
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
//		JSONArray invoiceArray = new JSONArray();
		JSONObject invoiceNewObj = new JSONObject();
		
		Customer customer = new Customer();
		Invoice invoice = new Invoice();
		AppTrn appTrn = new AppTrn();
		InvoiceNew invoiceNew = new InvoiceNew();
		
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
			String invoiceNewData = new StringBuilder(obj.get("invoice").toString())
					.deleteCharAt(obj.get("invoice").toString().length()-1)
					.deleteCharAt(0)
					.toString();
			String trnData = new StringBuilder(obj.get("settings").toString())
					.deleteCharAt(obj.get("settings").toString().length()-1)
					.deleteCharAt(0)
					.toString();
			System.out.println(payloadRequest);
			customerObj = (JSONObject)parser.parse(customerDetails);
			invoiceObj = (JSONObject)parser.parse(doData);
			invoiceNewObj = (JSONObject)parser.parse(invoiceNewData);
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
		invoice.setTerms(invoiceNewObj.get("terms").toString());
		invoice.setCreatedDate(invoiceObj.get("quo_date").toString());
		invoice.setQuatationNo(invoiceObj.get("quo_no").toString());
		invoice.setAmtInWords(invoiceObj.get("words").toString());
		invoice.setDiscount(invoiceObj.get("discount").toString());
		invoice.setVat(invoiceObj.get("vat").toString());
		StringBuilder sb = new StringBuilder();
		for(String s:values) {
			sb.append(invoiceObj.get(s).toString());
			sb.append(',');
		}
		doDisplayNo=sb.deleteCharAt(sb.length()-1).toString();
		invoiceNew.setInvoiceNo(invoiceNewObj.get("in_no").toString());
		invoiceNew.setInvoiceDate(invoiceNewObj.get("in_date").toString());
		invoiceNew.setTerms(invoiceNewObj.get("terms").toString());
		
		appTrn.setTrn(trnObj.get("format").toString());
		for(int i = 0; i<objArray.size();i++) {
			JSONObject object = new JSONObject();
			object = (JSONObject)objArray.get(i);
			InvoiceItem invoiceItem = new InvoiceItem();
			invoiceItem.setId(Integer.parseInt(object.get("id").toString()));
			invoiceItem.setQuoNo(object.get("quo_no").toString());
			invoiceItem.setInventoryItemCode(object.get("inventory__item_code").toString());
			invoiceItem.setInventoryDescription(object.get("inventory__description").toString());
			invoiceItem.setUom(object.get("uom").toString());
			invoiceItem.setInventoryPrice(object.get("inventory__price").toString());
			int qty=0;
			for(String s:values) {
				qty+=Integer.parseInt(object.get(s).toString());
				
			}
			invoiceItem.setQuantity(Integer.toString(qty));
			invoiceItem.setVat(object.get("vat").toString());
			invoiceItem.setTotal(object.get("total").toString());
			invoiceItem.setDiscount(object.get("discount").toString());
			invoiceList.add(invoiceItem);
		}
		CreateTaxInvoice ci =new CreateTaxInvoice(appName,"/app/"+appName+"/IN_"+invoiceNo+".pdf",customer,invoice,appTrn,doDisplayNo,invoiceNew);
		ci.create(invoiceList);
		CreateHeaderlessTaxInvoice cii =new CreateHeaderlessTaxInvoice(appName,"/app/"+appName+"/IN_"+invoiceNo+"_no_header.pdf",customer,invoice,appTrn,doDisplayNo,invoiceNew);
		cii.create(invoiceList);
	    rd.close();
			}
	public void doPost(HttpServletRequest request,HttpServletResponse res) throws ServletException,IOException {

		
	}



}
