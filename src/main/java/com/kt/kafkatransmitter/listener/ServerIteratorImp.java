package com.kt.kafkatransmitter.listener;

import com.kt.kafkatransmitter.client.AbstractSocket;
import com.kt.kafkatransmitter.configuration.CommunicationConfiguration;
import com.kt.kafkatransmitter.configuration.ConfigurationGetter;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.dsl.TcpInboundGatewaySpec;
import org.springframework.integration.ip.dsl.TcpServerConnectionFactorySpec;
import org.springframework.integration.ip.dsl.Udp;
import org.springframework.integration.ip.tcp.serializer.TcpCodecs;
import org.springframework.stereotype.Service;

@Service
public class ServerIteratorImp implements ServerIterator {

    private final static String HANDLER_PREFIX = "SERVER_HANDLER_";

    private int currentIndex = 0;

    @Override
    public boolean hasNext() {
        return (currentIndex < ConfigurationGetter.getServerConfigurations().size());
    }

    @Override
    public IntegrationFlow getNext() {
        CommunicationConfiguration communicationConfiguration = ConfigurationGetter.getServerConfigurations().get(currentIndex);
        currentIndex++;
        switch (communicationConfiguration.getType()) {
            case AbstractSocket.UDP_TYPE:
                return IntegrationFlows
                        .from(Udp.inboundAdapter(communicationConfiguration.getPort()))
                        .handle(ServerHandlerMultiTon.getServerHandler(HANDLER_PREFIX + communicationConfiguration.getId()))
                        .get();
            case AbstractSocket.TCP_TYPE:
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
