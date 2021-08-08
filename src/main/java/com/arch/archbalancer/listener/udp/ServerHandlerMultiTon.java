package com.arch.archbalancer.listener.udp;

import com.arch.archbalancer.client.ClientSocketFactory;
import org.springframework.messaging.MessageHeaders;
import org.springframework.stereotype.Service;

import javax.naming.CommunicationException;
import java.util.HashMap;

@Service
public class ServerHandlerMultiTon {
    private static final HashMap<String, ServerHandler> serverHandlers = new HashMap<>();

    static ServerHandler getServerHandler(String handlerId) {
        if (!serverHandlers.containsKey(handlerId)) {
            serverHandlers.put(handlerId, new ServerHandlerImp());
        }
        return serverHandlers.get(handlerId);
    }

    public static class ServerHandlerImp implements ServerHandler {

        private ServerHandlerImp() {
        }

        @Override
        public void handleMessage(byte[] message, MessageHeaders messageHeaders) throws CommunicationException {
            ClientSocketFactory.createSocketForDestination().sendData(new String(message), messageHeaders);
        }
    }
}
