package com.kt.kafkatransmitter.listener.udp;

import com.kt.kafkatransmitter.util.ConfigurationReader;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.dsl.TcpInboundGatewaySpec;
import org.springframework.integration.ip.dsl.TcpServerConnectionFactorySpec;
import org.springframework.integration.ip.dsl.Udp;
import org.springframework.integration.ip.tcp.serializer.TcpCodecs;
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
        switch (communicationConfiguration.getType()) {
            case "udp":
                return IntegrationFlows
                        .from(Udp.inboundAdapter(communicationConfiguration.getPort()))
                        .handle(ServerHandlerMultiTon.getServerHandler(HANDLER_PREFIX + communicationConfiguration.getId()))
                        .get();
            case "tcp":
                TcpServerConnectionFactorySpec connectionFactory =
                        Tcp.netServer(communicationConfiguration.getPort())
                                .deserializer(TcpCodecs.lf())
                                .serializer(TcpCodecs.lf())
                                .soTcpNoDelay(true);

                TcpInboundGatewaySpec inboundGateway =
                        Tcp.inboundGateway(connectionFactory);
                return IntegrationFlows
                        .from(inboundGateway)
                        .handle(ServerHandlerMultiTon.getServerHandler(HANDLER_PREFIX + communicationConfiguration.getId()))
                        .get();
            default:
                return null;
        }
    }
}
