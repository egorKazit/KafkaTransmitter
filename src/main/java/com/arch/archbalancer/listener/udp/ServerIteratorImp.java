package com.arch.archbalancer.listener.udp;

import com.arch.archbalancer.util.ConfigurationReader;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.dsl.Udp;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
public class ServerIteratorImp implements ServerIterator {

    private final static String HANDLER_PREFIX = "SERVER_HANDLER_";

    private int currentIndex = 0;
    ArrayList<ConfigurationReader.CommunicationConfiguration> communicationConfigurations;


    public ServerIteratorImp() {
        communicationConfigurations = ConfigurationReader.getServerConfigurations();
    }

    @Override
    public boolean hasNext() {
        return (currentIndex < communicationConfigurations.size());
    }

    @Override
    public IntegrationFlow getNext() {
        ConfigurationReader.CommunicationConfiguration communicationConfiguration = communicationConfigurations.get(currentIndex);
        currentIndex++;
        return IntegrationFlows
                .from(Udp.inboundAdapter(communicationConfiguration.getPort()))
                .handle(ServerHandlerMultiTon.getServerHandler(HANDLER_PREFIX + communicationConfiguration.getId()))
                .get();
    }
}
