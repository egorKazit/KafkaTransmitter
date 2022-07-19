package com.kt.kafkatransmitter.client;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

/**
 * UDP socket
 */
@NoArgsConstructor(access = AccessLevel.MODULE)
@Log4j2
class UdpSocket extends AbstractSocket {

    @Override
    public boolean send(String message) {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress inetAddress = InetAddress.getByName(host);
            byte[] sendData = (message + "\r\n").getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, inetAddress, port);
            clientSocket.send(sendPacket);
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            clientSocket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            messageBuilder.append(received,0,packet.getLength());
        } catch (IOException e) {
            log.error("Error during sending: {}; Cause: {}", e.getMessage(), e.getCause());
            log.debug("The error is going to be re-thrown");
            return false;
        }
        return true;
    }

}
