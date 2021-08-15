package com.kt.kafkatransmitter.client;

import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

@Log4j2
class UdpSocket extends AbstractSocket {

    UdpSocket() {
    }

    @Override
    public void send(String message) {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress IPAddress = InetAddress.getByName(host);
            byte[] sendData = (message + "\r\n").getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            clientSocket.send(sendPacket);
            byte[] buf = new byte[256];
            DatagramPacket packet = new DatagramPacket(buf, buf.length);
            clientSocket.receive(packet);
            String received = new String(packet.getData(), 0, packet.getLength());
            System.out.println("Quote of the Moment: " + received);
        } catch (IOException e) {
            log.error("Error during sending: {}; Cause: {}", e.getMessage(), e.getCause());
            log.debug("The error is going to be re-thrown");
        }
    }

}
