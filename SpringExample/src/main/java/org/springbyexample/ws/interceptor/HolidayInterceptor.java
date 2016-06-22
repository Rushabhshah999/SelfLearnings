package org.springbyexample.ws.interceptor;

import java.util.Iterator;
import javax.xml.transform.dom.DOMResult;
import javax.xml.transform.dom.DOMSource;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.oxm.jaxb.Jaxb2Marshaller;
import org.springframework.ws.context.MessageContext;
import org.springframework.ws.soap.SoapHeader;
import org.springframework.ws.soap.SoapHeaderElement;
import org.springframework.ws.soap.SoapMessage;
import org.springframework.ws.soap.server.SoapEndpointInterceptor;
import org.w3c.dom.Node;

import org.springbyexample.person.schema.beans.RequestHeader;

public class HolidayInterceptor implements SoapEndpointInterceptor {

	private static final Log logger = LogFactory.getLog(HolidayInterceptor.class);
	private Jaxb2Marshaller marshaller;
	
	public boolean understands(SoapHeaderElement header) {
		boolean u = header.getName().getNamespaceURI()
				.equalsIgnoreCase("http://www.springbyexample.org/person/schema/beans");
		return u;
	}
	
	public boolean handleRequest(MessageContext messageContext, Object endpoint)
			throws Exception {

		System.out.println("HolidayInterceptor inside handleRequest ....................");
		
		SoapMessage soapMsg = (SoapMessage) messageContext.getRequest();
		SoapHeader soapHeader = soapMsg.getSoapHeader();
		Iterator<SoapHeaderElement> iter = soapHeader.examineAllHeaderElements();
		boolean found= false;
		while (iter.hasNext() & (!found)) {
			SoapHeaderElement she = (SoapHeaderElement) iter.next();
			
			
			System.out.println("HolidayInterceptor inside Process the header"+ she);
			
			if (understands(she)) {
				
				
				
				// Process the header
				process(she);	
				found =true;		
			}
		}
		if(found)
		{
			return true;
		}
		else
		{
			Exception ex= new Exception();
			throw ex;
		}  
		
		
	}

	private void process(SoapHeaderElement she) {
		
		System.out.println("process starts");
		
		DOMResult result = (DOMResult)she.getResult();
		Node node=  result.getNode();	
		
		System.out.println("node"+ node);
		
		DOMSource ds= new DOMSource(node);
		
		System.out.println("ds"+ ds);
		
		RequestHeader requestHeader = (RequestHeader)marshaller.unmarshal(ds);
		logger.info("request time: "+requestHeader.getUsername());
		
		System.out.println("process completes");
		
	}
	
	public boolean handleResponse(MessageContext messageContext, Object endpoint)
	throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	public boolean handleFault(MessageContext messageContext, Object endpoint)
		throws Exception {
		// TODO Auto-generated method stub
		return true;
	}

	public void afterCompletion(MessageContext messageContext, Object endpoint,
			Exception ex) {
		// TODO Auto-generated method stub

	}

	public Jaxb2Marshaller getMarshaller() {
		return marshaller;
	}

	public void setMarshaller(Jaxb2Marshaller marshaller) {
		this.marshaller = marshaller;
	}


}