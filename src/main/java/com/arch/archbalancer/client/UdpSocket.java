package com.arch.archbalancer.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

public class UdpSocket extends AbstractSocket{

    UdpSocket() {
    }

    public void send(String message) {
        try (DatagramSocket clientSocket = new DatagramSocket()) {
            InetAddress IPAddress = InetAddress.getByName(host);
            byte[] sendData = (message + "\r\n").getBytes();
            DatagramPacket sendPacket = new DatagramPacket(sendData, sendData.length, IPAddress, port);
            clientSocket.send(sendPacket);
        } catch (IOException e) {

        }
    }

}
