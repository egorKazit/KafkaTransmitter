package com.kt.kafkatransmitter.listener;

import com.kt.kafkatransmitter.client.AbstractSocket;
import com.kt.kafkatransmitter.configuration.CommunicationConfiguration;
import com.kt.kafkatransmitter.configuration.ConfigurationGetter;
import lombok.extern.log4j.Log4j2;
import org.springframework.integration.dsl.IntegrationFlow;
import org.springframework.integration.dsl.IntegrationFlows;
import org.springframework.integration.ip.dsl.Tcp;
import org.springframework.integration.ip.dsl.TcpInboundGatewaySpec;
import org.springframework.integration.ip.dsl.TcpServerConnectionFactorySpec;
import org.springframework.integration.ip.dsl.Udp;
import org.springframework.integration.ip.tcp.serializer.TcpCodecs;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ServerIteratorImp implements ServerIterator {

    private final static String HANDLER_PREFIX = "SERVER_HANDLER_";

    private int currentIndex = 0;

    @Override
    public boolean hasNext() {
        return (currentIndex < ConfigurationGetter.getServerConfigurations().size());
    }

    @Override
    public IntegrationFlow getNext() {
        if (currentIndex >= ConfigurationGetter.getServerConfigurations().size()) return null;
        CommunicationConfiguration communicationConfiguration = ConfigurationGetter.getServerConfigurations().get(currentIndex);
        currentIndex++;
        switch (communicationConfiguration.getType()) {
            case AbstractSocket.UDP_TYPE:
                log.debug("Udp listener(id {}) initialization...", communicationConfiguration.getId());
                return IntegrationFlows
                        .from(Udp.inboundAdapter(communicationConfiguration.getPort()))
                        .handle(ServerHandlerMultiTon.getServerHandler(HANDLER_PREFIX + communicationConfiguration.getId()))
                        .get();
            case AbstractSocket.TCP_TYPE:
                log.debug("Tcp listener(id {}) initialization...", communicationConfiguration.getId());
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
                log.error("Incorrect listener type {} for configuration {}", communicationConfiguration.getType(), communicationConfiguration.getId());
                return null;
        }
    }
}
