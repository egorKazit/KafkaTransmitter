package com.kt.kafkatransmitter.configuration;

import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.util.LinkedList;

class ConfigurationHandler {
    private static final String ID = "Id";
    private static final String ENABLED = "Enabled";
    private static final String PORT = "Port";
    private static final String TYPE = "Type";
    private static final String HOST = "Host";

    static LinkedList<CommunicationConfiguration> getCommunicationConfigurations(String configs, String config) {
        LinkedList<CommunicationConfiguration> communicationConfigurations = new LinkedList<>();
        NodeList internalConfigs = ((Element) ConfigurationReader.getConfigurationReader()
                .getDocument().getElementsByTagName(configs).item(0)).getElementsByTagName(config);
        for (int configurationPosition = 0; configurationPosition < internalConfigs.getLength(); configurationPosition++) {
            Element internalConfig = (Element) internalConfigs.item(configurationPosition);
            boolean isEnabled = Boolean.parseBoolean(internalConfig.getElementsByTagName(ENABLED).item(0).getTextContent());
            if (isEnabled) {
                CommunicationConfiguration communicationConfiguration = new CommunicationConfiguration();
                communicationConfiguration.Id = Integer.parseInt(internalConfig.getElementsByTagName(ID).item(0).getTextContent());
                communicationConfiguration.port = Integer.parseInt(internalConfig.getElementsByTagName(PORT).item(0).getTextContent());
                Node typeNode = internalConfig.getElementsByTagName(TYPE).item(0);
                if (typeNode != null)
                    communicationConfiguration.type = typeNode.getTextContent();
                Node hostNode = internalConfig.getElementsByTagName(HOST).item(0);
                if (hostNode != null)
                    communicationConfiguration.host = hostNode.getTextContent();
                communicationConfigurations.add(communicationConfiguration);
            }
        }
        return communicationConfigurations;
    }
}
