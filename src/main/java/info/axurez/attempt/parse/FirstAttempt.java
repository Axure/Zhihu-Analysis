package info.axurez.attempt.parse;

import javax.xml.parsers.*;

import info.axurez.network.http.ZhihuCrawler;
import javafx.util.Pair;
import org.ccil.cowan.tagsoup.jaxp.SAXParserImpl;
import org.xml.sax.*;
import org.xml.sax.helpers.*;

import java.util.*;
import java.io.*;
/**
 * Created by axurez on 2016/2/24.
 */

/**
 *
 */
public class FirstAttempt {
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
        SAXParserImpl.newInstance(null).parse(new InputSource(
            new StringReader(result)), new MyXmlHandler());
    }

}

class MyXmlHandler extends DefaultHandler {
    private Hashtable<String, Integer> tagCount;
    private Hashtable<String, Integer> tagPointer;
    private StringBuilder currentText;
    private Hashtable<Pair<String, Integer>, ArrayList<String>> innerTexts;

    @Override
    public void startDocument() throws SAXException {
        tagCount = new Hashtable<>();
        tagPointer = new Hashtable<>();
        currentText = new StringBuilder();
        innerTexts = new Hashtable<>();
    }

    @Override
    public void startElement(String namespaceURI,
                             String localName,
                             String qName,
                             Attributes atts)
        throws SAXException {
        /**
         * Get the tag name.
         */
        String tagName = localName;
        Object countObject = this.tagCount.get(tagName);
        /**
         *
         */
        if (countObject == null) {
            this.tagCount.put(tagName, 1);
        } else {
            int count = ((Integer) countObject).intValue();
            count++;
            this.tagCount.put(tagName, new Integer(count));
        }
        /**
         * Put that count into the tag pointer.
         */
        Integer count = this.tagCount.get(tagName);
        tagPointer.put(tagName, new Integer(count));
        /**
         *
         */
    }

    @Override
    public void characters(char ch[], int start, int length) throws SAXException {
        currentText.append(ch, start, length);
    }

    @Override
    public void endElement(String uri, String localName, String qName)
        throws SAXException {
        /**
         * Get the basic information.
         */
        String tagName = localName;
        Integer pointer = tagPointer.get(tagName);
        /**
         * Create the node and find the contents under that key.
         */
        Pair<String, Integer> node = new Pair<>(tagName, pointer);
        Object textObject = innerTexts.get(node);
        if (textObject == null) {
            innerTexts.put(node, new ArrayList<>());
        }
        innerTexts.get(node).add(currentText.toString());
        /**
         *
         */
        currentText.setLength(0);
        tagPointer.put(tagName, pointer - 1);
    }

    @Override
    public void endDocument() throws SAXException {
        Enumeration e = tagCount.keys();
        while (e.hasMoreElements()) {
            String tag = (String) e.nextElement();
            int count = ((Integer) tagCount.get(tag)).intValue();
            System.out.println("Local Name \"" + tag + "\" occurs "
                + count + " times");
        }

        e = innerTexts.keys();
        while (e.hasMoreElements()) {
            Pair<String, Integer> countedTag = (Pair<String, Integer>) e.nextElement();
            ArrayList<String> innerText = (ArrayList<String>) innerTexts.get(countedTag);
            System.out.printf("%dth <%s>: ", countedTag.getValue(), countedTag.getKey());
            for (String text: innerText) {
                System.out.printf("%s, ", text);
            }
            System.out.println();
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
