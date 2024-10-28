package it.viligiardi;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class Main {
    public static void main(String[] args) throws IOException{
        ServerSocket ss1 = new ServerSocket(3000);
        System.out.println("Il server è in ascolto");

        do {
            Socket s1 = ss1.accept();
            System.out.println("Qualcuno si è collegato");
            ArrayList<String> list = new ArrayList<>();
            MyThread mt = new MyThread(s1, list);
            mt.start();
        } while (true);

    }
}