
package net.webservicex;

import javax.jws.WebMethod;
import javax.jws.WebParam;
import javax.jws.WebResult;
import javax.jws.WebService;
import javax.jws.soap.SOAPBinding;
import javax.xml.bind.annotation.XmlSeeAlso;


/**
 * This class was generated by the JAX-WS RI.
 * JAX-WS RI 2.2.4-b01
 * Generated source version: 2.2
 * 
 */
@WebService(name = "StockQuoteHttpGet", targetNamespace = "http://www.webserviceX.NET/")
@SOAPBinding(parameterStyle = SOAPBinding.ParameterStyle.BARE)
@XmlSeeAlso({
    ObjectFactory.class
})
public interface StockQuoteHttpGet {


    /**
     * Get Stock quote for a company Symbol
     * 
     * @param symbol
     * @return
     *     returns java.lang.String
     */
    @WebMethod(operationName = "GetQuote")
    @WebResult(name = "string", targetNamespace = "http://www.webserviceX.NET/", partName = "Body")
    public String getQuote(
        @WebParam(name = "string", targetNamespace = "http://www.w3.org/2001/XMLSchema", partName = "symbol")
        String symbol);

}
