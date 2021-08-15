package com.kt.kafkatransmitter.configuration;

import lombok.Getter;
import lombok.extern.log4j.Log4j2;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.XMLConstants;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

@Log4j2
class ConfigurationReader {

    @Getter
    private Document document;

    private static final String CONFIG_FILE = "/server_configuration.xml";

    private static final ConfigurationReader configurationReader = new ConfigurationReader();

    static ConfigurationReader getConfigurationReader() {
        return configurationReader;
    }

    private ConfigurationReader() {
        try {
            InputStream is = ConfigurationReader.class.getResourceAsStream(CONFIG_FILE);
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(is);
            document.getDocumentElement().normalize();
        } catch (ParserConfigurationException | IOException | SAXException e) {
            log.error("Error during configuration file reading {}", e.getMessage());
        }
    }

}
