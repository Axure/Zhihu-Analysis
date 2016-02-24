package info.axurez.attempt.parse;
import javax.xml.parsers.*;

import info.axurez.network.http.ZhihuCrawler;
import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.xml.sax.*;
import org.xml.sax.helpers.*;
import org.ccil.cowan.tagsoup.*;

import java.util.*;
import java.io.*;
/**
 * Created by axurez on 2016/2/24.
 */

/**
 *
 */
public class FirstAttempt  {
    static public void main(String[] args) throws Exception {
        SAXParserFactory spf = SAXParserFactory.newInstance();
        spf.setNamespaceAware(true);
        SAXParser saxParser = spf.newSAXParser();

        XMLReader xmlReader = saxParser.getXMLReader();
        xmlReader.setErrorHandler(new MyErrorHandler(System.err));
        xmlReader.setContentHandler(new MyXmlHandler());
        xmlReader.parse(new InputSource(
            new StringReader("<a>" +
                "?<b>c</b><c><d>f</d>fs</c>" +
                "</a>")));
        ZhihuCrawler crawler = new ZhihuCrawler();
        String result = crawler.getQuestionHtml("28943259");
//        xmlReader.parse(new InputSource(
//            new StringReader(result)));
        SAXParserImpl.newInstance(null).parse(new InputSource(
            new StringReader(result)), new MyXmlHandler());
    }


}

class MyXmlHandler extends DefaultHandler {
    private Hashtable tags;

    @Override
    public void startDocument() throws SAXException {
        tags = new Hashtable();
    }

    @Override
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts)
        throws SAXException {

        String key = localName;
        Object value = tags.get(key);

        if (value == null) {
            tags.put(key, new Integer(1));
        }
        else {
            int count = ((Integer)value).intValue();
            count++;
            tags.put(key, new Integer(count));
        }
    }

    @Override
    public void endDocument() throws SAXException {
        Enumeration e = tags.keys();
        while (e.hasMoreElements()) {
            String tag = (String)e.nextElement();
            int count = ((Integer)tags.get(tag)).intValue();
            System.out.println("Local Name \"" + tag + "\" occurs "
                + count + " times");
        }
    }
}

class MyErrorHandler implements ErrorHandler {
    private PrintStream out;

    MyErrorHandler(PrintStream out) {
        this.out = out;
    }

    private String getParseExceptionInfo(SAXParseException spe) {
        String systemId = spe.getSystemId();

        if (systemId == null) {
            systemId = "null";
        }

        String info = "URI=" + systemId + " Line="
            + spe.getLineNumber() + ": " + spe.getMessage();

        return info;
    }

    public void warning(SAXParseException spe) throws SAXException {
        out.println("Warning: " + getParseExceptionInfo(spe));
    }

    public void error(SAXParseException spe) throws SAXException {
        String message = "Error: " + getParseExceptionInfo(spe);
        throw new SAXException(message);
    }

    public void fatalError(SAXParseException spe) throws SAXException {
        String message = "Fatal Error: " + getParseExceptionInfo(spe);
        throw new SAXException(message);
    }
}
