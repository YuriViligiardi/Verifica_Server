package it.viligiardi;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class MyThread extends Thread{
    //attributes
    private Socket s;
    private ArrayList<String> list;

    //methods and constructions
    public MyThread(Socket s, ArrayList<String> l){
        this.s = s;
        this.list = l;
    }

    @Override
    public void run() {
        int chiusura = 0;
        do {
            try {
                BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                DataOutputStream out = new DataOutputStream(s.getOutputStream());
    
                String stringaRicevuta = in.readLine();
                System.out.println("Client invia: " + stringaRicevuta);
    
                switch (stringaRicevuta) {
                    //funzioni del server per l'invio della lista 
                    case "?":
                        for (int i = 0; i < list.size(); i++) {
                            String stringaInviare = list.get(i);
                            out.writeBytes(stringaInviare + "\n");
                            System.out.println("Server invia:" + stringaInviare);
                        }
                        System.out.println("Server invia: @");
                        out.writeBytes("@" + "\n");
                        break;
                    
                    //funzioni del server per la disconessione del client
                    case "!":
                        chiusura = 1;
                        System.out.println("CLIENT DISCONESSO");
                        s.close();
                    break;

                    //funzioni del server per l'eliminazione di una nota
                    case "-":
                        out.writeBytes("Invia la nota da eliminare" + "\n");
                        System.out.println("server invia: richiesta nota da elim.");
                        String notaDelete = in.readLine();
                        System.out.println("client invia: " + notaDelete);
                        boolean control = searchNota(notaDelete);
                        if (control == false) {
                            out.writeBytes("FALLITA" + "\n");
                            System.out.println("server invia: FALLITA");
                        } else {
                            list.remove(notaDelete);
                            out.writeBytes("RIUSCITA" + "\n");
                            System.out.println("server invia: RIUSCITA");
                        }
                        
                    break;
                    
                    //funzioni del server per la memorizzazione di una nota
                    default:
                        list.add(stringaRicevuta);
                        out.writeBytes("OK" + "\n");
                        System.out.println("Server invia: OK");
                        break;
                }
            } catch (Exception e) {
                System.out.println("Errore, comunicazione fallita");
            }
        } while (chiusura != 1);
    }

    //funzioni del server per vedere se è presente la nota
    public boolean searchNota(String s){
        for (int i = 0; i < list.size(); i++) {
                if (list.get(i).equals(s)) {
                    return true;
                }
        }
        return false;
    }


}
