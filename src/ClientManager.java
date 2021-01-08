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
                if (nomeutente.equals("superuser") && password.equals("1")) {
                    System.out.println("Controllo avvenuto con Successo");
                    pw.println("CHECK_OK");
                    pw.flush();
                    check = false;
                    System.out.println("Avvio Menù");
                } else {
                    System.out.println("Iper2Go: Utente amministratore non trovato");
                    pw.println("CHECK_ERROR");
                    pw.flush();
                }
                // SE C'È IL MATCH
                /*if(aut.cercaUtente(nomeutente,password)){
                    System.out.println("Controllo avvenuto con successo");
                    pw.println("CHECK_OK");
                    pw.flush();
                    autenticazione = false;
                    go = true;
                }else{
                    System.out.println("Utente non trovato");
                    pw.println("CHECK_ERROR");
                    pw.flush();
                }*/
            }
        } while(check);
            //FUNZIONI SUPERUSER
        while (go) {
                try {
                    InputStreamReader is = new InputStreamReader(client_socket.getInputStream());
                    BufferedReader br = new BufferedReader(is);
                    int scelta = Integer.parseInt(br.readLine());
                    System.out.println("Operazione da eseguire: " + scelta);

                    switch (scelta){
                        //AGGIUNTA
                        case 1: {
                            nomeprodotto = br.readLine();
                            System.out.println("Nome prodotto ricevuto:" + nomeprodotto);
                            pw = new PrintWriter(client_socket.getOutputStream());
                            if(prodotti.cercaProdotto(nomeprodotto)){
                                pw.println("Iper2Go: Prodotto già inserito nel sistema. Impossibile inserire");
                                pw.flush();
                                break;
                            }else{
                                pw.println("OK");
                                pw.flush();
                                reparto= br.readLine();
                                System.out.println("Reparto inserito: " + reparto);
                                prezzo = Double.parseDouble(br.readLine()); // DA VERIFICARE FUNZIONAMENTO
                                System.out.println("Prezzo inserito: "+ prezzo);
                                scadenza = br.readLine();
                                System.out.println("Scadenza inserita: "+ scadenza);
                                peso = Double.parseDouble(br.readLine());
                                System.out.println("Peso inserito: "+ peso);
                                Prodotto p = new Prodotto(nomeprodotto,scadenza,prezzo,reparto,peso);
                                prodotti.inserisciProdotto(p);
                                pw.println("Iper2Go: Prodotto inserito correttamente");
                                pw.flush();
                                break;
                            }
                        }
                        //Prodotto da cercare
                        case 2:{
                            nomeprodotto= br.readLine();
                            System.out.println("\nNome prodotto inserito: " + nomeprodotto);
                            pw = new PrintWriter(client_socket.getOutputStream());
                            if(prodotti.cercaProdotto(nomeprodotto)){
                                System.out.println(prodotti.prodotto_trovato);
                                pw.println(prodotti.prodotto_trovato);
                                pw.flush();
                                break;
                            }else{
                                pw.println("Iper2Go: Prodotto non trovato");
                                pw.flush();
                                break;
                            }
                        }
                        //Rimozione prodotto
                        case 3: {
                            nomeprodotto= br.readLine();
                            System.out.println("\nNome del prodotto inserito: " + nomeprodotto);
                            pw = new PrintWriter(client_socket.getOutputStream());
                            if (prodotti.cercaProdotto(nomeprodotto)){
                                prodotti.eliminaProdotto(prodotti.prodotto_trovato);
                                pw.println("Iper2Go: Prodotto ->" +nomeprodotto+ "<- eliminato dal sistema");
                                pw.flush();
                                break;
                            }else{
                                pw.println("\nIper2Go: Prodotto non presente nel magazzino. Impossibile eliminarlo");
                                pw.flush();
                                break;
                            }
                        }
                        //VISUALIZZA elenco
                        case 4: {
                            ArrayList<Prodotto> prod = prodotti.visualizzaListaProdotti();
                            for (Prodotto p : prod) {
                                System.out.println(p);
                                pw.println(p.toString());
                                pw.flush();
                            }
                            pw.println("STOP");
                            pw.flush();
                            break;
                        }

                        case 5: {
                            System.out.println("\nSalvataggio su file");
                            prodotti.salvaSuFile();
                                pw.println("Iper2Go: Salvataggio eseguito con successo");
                                pw.flush();
                                break;
                        }
                        //USCITA
                        case  0: {
                            System.out.println("\nChiusura di Iper2Go per il client :" + client_id);
                            System.out.println(new Date().toString());
                            client_socket.close();
                            go=false;
                            break;
                        }
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }