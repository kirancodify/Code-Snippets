package Xmlread;

import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.DocumentBuilder;
import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.w3c.dom.Node;
import org.w3c.dom.Element;
import java.io.File;

import java.io.IOException;

import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
 
import org.xml.sax.SAXException;

public class xmlValidateList {
   public static void main(String args[]) {
	   
	   if((args == null) || (args.length == 0)|| (args.length == 1)){
		   
		   System.out.println("Please provide the arguments in the correct usage");
    	   System.out.println("Usage: <Filename> <option=validate | list>");
		   
	   }
	   else {
         	String file1=args[0];
         	String option=args[1];
         	String function1="validate";
         	String function2="list";
         	
       
       if (option.equals(function1)){ 	
        
              System.out.println(file1+" validates against bookshelf.xsd - "+validateXMLSchema("bookshelf.xsd", file1));
       }
       
       else if (option.equals(function2))
       {
       if (validateXMLSchema("bookshelf.xsd", file1) == true){
      
       try {
        File fXmlFile = new File(file1);
        DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
        DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
        Document doc = dBuilder.parse(fXmlFile); 
        
        doc.getDocumentElement().normalize(); 
       // System.out.println("Root element :" + doc.getDocumentElement().getNodeName());
        NodeList nList = doc.getElementsByTagName("MyBooks");
        System.out.println("----------------------------");

            for (int temp = 0; temp < nList.getLength(); temp++) {
        Node nNode = nList.item(temp);
               // System.out.println("\nCurrent Element :" + nNode.getNodeName());
        if (nNode.getNodeType() == Node.ELEMENT_NODE) {
        Element eElement = (Element) nNode; 
        //System.out.println("Book Shelf : " + eElement.getElementsByTagName("BookShelf"));
        System.out.println("Book Number : " + eElement.getElementsByTagName("Id").item(0).getTextContent());
        System.out.println("Title : " + eElement.getElementsByTagName("Title").item(0).getTextContent());
        System.out.println("Author : " + eElement.getElementsByTagName("Author").item(0).getTextContent());
        System.out.println("Year : " + eElement.getElementsByTagName("Date").item(0).getTextContent());
        System.out.println("Type : " + eElement.getElementsByTagName("Type").item(0).getTextContent());
        System.out.println("Publisher : " + eElement.getElementsByTagName("Publisher").item(0).getTextContent());
        System.out.println("Own Book : " + eElement.getElementsByTagName("isOWN").item(0).getTextContent());
        System.out.println("Book has CD : " + eElement.getElementsByTagName("hasCD").item(0).getTextContent());
        System.out.println("-------------Next Book-------------");

            }
        }
       } catch (Exception e) {
        e.printStackTrace();
       }
    }
       }
       }
       }
   
    /**
     * @param xsdPath
     * @param xmlPath
     * @return
     */
    public static boolean validateXMLSchema(String xsdPath, String xmlPath){
                 
                try {
                    SchemaFactory factory = 
                            SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
                    Schema schema = factory.newSchema(new File(xsdPath));
                    Validator validator = schema.newValidator();
                    validator.validate(new StreamSource(new File(xmlPath)));
                } catch (IOException e) {
                    System.out.println("Exception: "+e.getMessage());
                    System.out.println( "invalid");
                    return false;
                }catch (SAXException e) {
                    System.out.println("Exception: "+e.getMessage());
                    System.out.println( "invalid");
                    return false;
                }
                
                System.out.println( "valid");
                return true;
            }
    }


