package org.n3r.sandbox.jaxb;


import com.sun.xml.internal.bind.marshaller.CharacterEscapeHandler;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathFactory;
import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.StringReader;
import java.io.StringWriter;

public class XmlUtil {
    public static String marshal(Object bean) {
        return marshal(bean, bean.getClass());
    }

    public static String marshal(Object bean, Class... types) {
        StringWriter sw = new StringWriter();

        try {
            JAXBContext carContext = JAXBContext.newInstance(types);
            Marshaller marshaller = carContext.createMarshaller();
//            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);
//            marshaller.setProperty(Marshaller.JAXB_FRAGMENT, true); //  without xml declaration
            marshaller.setProperty(Marshaller.JAXB_ENCODING, "UTF-8");
            marshaller.setProperty(CharacterEscapeHandler.class.getName(), OnDemandCDataEscapeHandler.instance);
            marshaller.marshal(bean, sw);
            return sw.toString();
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T unmarshal(String xml, Class<T> beanClass) {
        StringReader reader = new StringReader(xml);
        return JAXB.unmarshal(reader, beanClass);
    }

    public static String prettyXml(String xml) {
        final boolean omitXmlDeclaration = !xml.startsWith("<?xml");

        try {
            TransformerFactory transformerFactory = TransformerFactory.newInstance();
            transformerFactory.setAttribute("indent-number", 2);
            Transformer transformer = transformerFactory.newTransformer();
            transformer.setOutputProperty(OutputKeys.INDENT, "yes");
            transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, omitXmlDeclaration ? "yes" : "no");
            StreamResult result = new StreamResult(new StringWriter());
            DOMSource source = new DOMSource(parseXmlFile(xml));
            transformer.transform(source, result);
            String xmlString = result.getWriter().toString();
            return xmlString;
        } catch (TransformerException e) {
            throw new RuntimeException(e);
        }
    }

    public static void main(String[] args) {
        String s = prettyXml("<root><child>aaa</child><child/></root>");
        System.out.println(s);
    }

    public static Document parseXmlFile(String xml) {
        try {
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            InputSource is = new InputSource(new StringReader(xml));
            return db.parse(is);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static String extractValue(String xml, String xpathExpression) {
        String actual;
        try {
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setNamespaceAware(true);
            documentBuilderFactory.setIgnoringElementContentWhitespace(true);
            DocumentBuilder docBuilder = documentBuilderFactory.newDocumentBuilder();

            byte[] bytes = xml.getBytes("UTF-8");
            InputStream inputStream = new ByteArrayInputStream(bytes);
            Document doc = docBuilder.parse(inputStream);
            XPathFactory xPathFactory = XPathFactory.newInstance();
            XPath xpath = xPathFactory.newXPath();

            actual = xpath.evaluate(xpathExpression, doc, XPathConstants.STRING).toString();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        return actual;
    }
}