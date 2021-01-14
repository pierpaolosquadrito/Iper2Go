import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import java.io.*;
import java.net.*;
@SuppressWarnings("unchecked")

public class ClientManager implements Runnable {
    private Socket client_socket;
    Integer client_id;
    private Prodotti prodotti;
    //private Autenticazione aut;
    String nomeprodotto;
    String scadenza;
    Double prezzo;
    String reparto;
    Double peso;
    Integer quantita;
    DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);

    public ClientManager(Socket myclient, Prodotti prodotti,Integer client_id) {
        client_socket= myclient;
        this.prodotti= prodotti;
        this.client_id=client_id;
    }
    @Override
    public void run() {
        String tid = Thread.currentThread().getName();
        System.out.println(tid + "-> Connessione Accettata da " + client_socket.getRemoteSocketAddress() + " il " + df.format(new Date()));
        Scanner client_scanner;
        PrintWriter pw;
        try {
            client_scanner = new Scanner(client_socket.getInputStream());
            pw = new PrintWriter(client_socket.getOutputStream());
        } catch (IOException e) {
            System.out.println("Errore nella creazione del ServerSocket su ClientManager");
            e.printStackTrace();
            return;
        }
        // PROCESSO DI AUTENTICAZIONE
        boolean check = true;
        boolean go= true;
        do {
            String message = client_scanner.nextLine();
            System.out.println("Messaggio Ricevuto: " + message);
            Scanner msg_scanner = new Scanner(message);
            String cmd = msg_scanner.next();
            System.out.println("Comando Ricevuto: " + cmd);
            if (cmd.equals("CHECK")) {
                String nomeutente = msg_scanner.next();
                System.out.println("Stampa nomeutente ricevuto:" + nomeutente);
                String password = msg_scanner.next();
                System.out.println("Stampa password ricevuta:" + password);
                if (nomeutente.equals("superuser") && password.equals("1234")) {
                    System.out.println("Controllo avvenuto con Successo");
                    pw.println("CHECK_OK");
                    pw.flush();
                    check = false;
                    System.out.println("Avvio Menù per Client"+client_id);
                } else {
                    System.out.println("Iper2Go: Utente amministratore non trovato");
                    pw.println("CHECK_ERROR");
                    pw.flush();
                }
            }
        } while(check);
            //FUNZIONI SUPERUSER
        while (go) {
                try {
                    int scelta = Integer.parseInt(client_scanner.nextLine());
                    System.out.println("Operazione da eseguire: " + scelta);
                    switch (scelta){
                        //AGGIUNTA
                        case 1: {
                            nomeprodotto = client_scanner.nextLine();
                            System.out.println("Nome prodotto ricevuto:" + nomeprodotto);
                            if(prodotti.cercaProdotto(nomeprodotto)){
                                pw.println("Iper2Go: Prodotto già inserito nel sistema. Verrà aggiunta una nuova unità");
                                pw.flush();
                                prodotti.incrementaQuantita(nomeprodotto);
                            }else{
                                pw.println("Iper2Go: Avvio procedura inserimento di un nuovo prodotto");
                                pw.flush();
                                reparto= client_scanner.nextLine();
                                System.out.println("Reparto inserito: " + reparto);
                                prezzo = Double.parseDouble(client_scanner.nextLine()); // DA VERIFICARE FUNZIONAMENTO
                                System.out.println("Prezzo inserito: "+ prezzo);
                                scadenza = client_scanner.nextLine();
                                System.out.println("Scadenza inserita: "+ scadenza);
                                peso = Double.parseDouble(client_scanner.nextLine());
                                System.out.println("Peso inserito: "+ peso);
                                quantita = Integer.parseInt(client_scanner.nextLine());
                                System.out.println("Quantità inserita: "+ quantita);
                                Prodotto p = new Prodotto(nomeprodotto,scadenza,prezzo,reparto,peso,quantita);
                                prodotti.inserisciProdotto(p);
                                pw.println("Iper2Go: Prodotto inserito correttamente");
                                pw.flush();
                            }
                            break;
                        }
                        //Prodotto da cercare
                        case 2:{
                            nomeprodotto= client_scanner.nextLine();
                            System.out.println("\nNome prodotto inserito: " + nomeprodotto);
                            Prodotto p = prodotti.restituisciProdotto(nomeprodotto);
                            if(p!=null){
                                System.out.println(p);
                                pw.println(p);
                            }else{
                                pw.println("Iper2Go: Prodotto non trovato");
                            }
                            pw.flush();
                            break;
                        }
                        //Rimozione prodotto
                        case 3: {
                            nomeprodotto= client_scanner.nextLine();
                            System.out.println("\nNome del prodotto inserito: " + nomeprodotto);
                            if (prodotti.cercaProdotto(nomeprodotto)){
                                prodotti.eliminaProdotto(prodotti.prodotto_trovato);
                                pw.println("Iper2Go: Prodotto ->" +nomeprodotto+ "<- eliminato dal sistema");
                            }else{
                                pw.println("Iper2Go: Prodotto non presente nel magazzino. Impossibile eliminarlo");
                            }
                            pw.flush();
                            break;
                        }
                        //Aggiorna quantità prodotto
                        case 4: {
                            nomeprodotto=client_scanner.nextLine();
                            System.out.println("\nNome del prodotto inserito: " + nomeprodotto);
                            quantita=Integer.parseInt(client_scanner.nextLine());
                            System.out.println("Quantità inserita: " + quantita);
                            String ritorno = prodotti.aggiornaQuantita(nomeprodotto,quantita);
                            if(ritorno.equalsIgnoreCase("CANC")){
                                pw.println("Iper2Go: Prodotto rimosso dal magazzino");
                                pw.flush();
                                break;
                            }
                            else if (ritorno.equalsIgnoreCase("VUOTO")){
                                pw.println("Iper2Go: Prodotto non presente in magazzino");
                                pw.flush();
                                break;
                            }
                            else{
                                pw.println("Iper2Go: Quantità in magazzino aggiornata, attualmente in magazzino sono presenti: " + ritorno + " unità di " +nomeprodotto);
                                pw.flush();
                                break;
                            }
                        }
                        //VISUALIZZA elenco
                        case 5: {
                            if (prodotti.quantitaProdotti()==0) {
                                pw.println("Iper2Go: Il magazzino è vuoto, impossibile visualizzare elenco prodotti, inserisci un nuovo prodotto");
                                pw.println("STOP");
                                pw.flush();
                            }else {
                                ArrayList<Prodotto> prod = prodotti.visualizzaListaProdotti();
                                for (Prodotto p : prod) {
                                    pw.println(p);
                                    pw.flush();
                                }
                                pw.println("Prodotti totali: " + prodotti.quantitaProdotti());
                                pw.println("STOP");
                                pw.flush();
                                break;
                            }
                            break;
                        }
                        case 6: {
                            System.out.println("\nSalvataggio su file");
                            if (prodotti.quantitaProdotti()==0){
                                pw.println("Iper2Go: Lista vuota, impossibile salvare");
                            }
                            else {
                                prodotti.salvaSuFile();
                                pw.println("Iper2Go: Salvataggio eseguito con successo");
                            }
                            pw.flush();
                            break;
                        }
                        //USCITA
                        case  0: {
                            System.out.println("\nChiusura di Iper2Go per il client :" + client_id);
                            System.out.println(new Date().toString());
                            prodotti.salvaSuFile();
                            client_socket.close();
                            go=false;
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.out.println("Client_"+client_id+" si è disconnesso");
                    e.printStackTrace();
                }
            }
        }
    }