package com.kt.kafkatransmitter.util;

import lombok.Getter;
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

public class ConfigurationReader {

    private static Document document;
    private static final String SERVERS = "Servers";
    private static final String SERVER = "Server";
    private static final String CLIENTS = "Clients";
    private static final String CLIENT = "Client";
    private static final String ID = "Id";
    private static final String ENABLED = "Enabled";
    private static final String PORT = "Port";
    private static final String TYPE = "Type";
    private static final String HOST = "Host";

    private static ArrayList<CommunicationConfiguration> serverConfigurations;
    private static ArrayList<CommunicationConfiguration> clientConfigurations;

    static {
        try {
            InputStream is = ConfigurationReader.class.getResourceAsStream("/server_configuration.xml");
            DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
            documentBuilderFactory.setFeature(XMLConstants.FEATURE_SECURE_PROCESSING, true);
            DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
            document = documentBuilder.parse(is);
            document.getDocumentElement().normalize();
            serverConfigurations = getCommunicationConfigurations(SERVERS, SERVER);
            clientConfigurations = getCommunicationConfigurations(CLIENTS, CLIENT);
        } catch (ParserConfigurationException | IOException | SAXException e) {
            e.printStackTrace();
        }
    }

    public static ArrayList<CommunicationConfiguration> getServerConfigurations() {
        return serverConfigurations;
    }

    public static ArrayList<CommunicationConfiguration> getClientConfigurations() {
        return clientConfigurations;
    }

    private static ArrayList<CommunicationConfiguration> getCommunicationConfigurations(String clients, String client) {
        ArrayList<CommunicationConfiguration> communicationConfigurations = new ArrayList<>();
        NodeList servers = ((Element) document.getElementsByTagName(clients).item(0)).getElementsByTagName(client);
        for (int serverConfigurationPosition = 0; serverConfigurationPosition < servers.getLength(); serverConfigurationPosition++) {
            Element server = (Element) servers.item(serverConfigurationPosition);
            boolean isEnabled = Boolean.parseBoolean(server.getElementsByTagName(ENABLED).item(0).getTextContent());
            if (isEnabled) {
                CommunicationConfiguration communicationConfiguration = new CommunicationConfiguration();
                communicationConfiguration.Id = Integer.parseInt(server.getElementsByTagName(ID).item(0).getTextContent());
                communicationConfiguration.port = Integer.parseInt(server.getElementsByTagName(PORT).item(0).getTextContent());
                Node typeNode = server.getElementsByTagName(TYPE).item(0);
                if (typeNode != null)
                    communicationConfiguration.type = typeNode.getTextContent();
                Node hostNode = server.getElementsByTagName(HOST).item(0);
                if (hostNode != null)
                    communicationConfiguration.host = hostNode.getTextContent();
                communicationConfigurations.add(communicationConfiguration);
            }
        }
        return communicationConfigurations;
    }

    public static class CommunicationConfiguration {
        private CommunicationConfiguration() {
        }

        @Getter
        int Id;
        @Getter
        String host;
        @Getter
        int port;
        @Getter
        String type;
    }

}
