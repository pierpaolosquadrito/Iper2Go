import java.io.*;
import java.net.Socket;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.Scanner;
import static java.lang.System.exit;



public class Client {

    public static void main(String[] args) throws IOException {
        String nomeprodotto;
        String scadenza;
        double prezzo;
        String nomereparto="";
        double peso;
        String nomeutente = null;
        DateFormat df = DateFormat.getDateInstance(DateFormat.SHORT, Locale.ITALY);



        if (args.length != 2) {
            System.out.println("Inserire l'indirizzo e il numero di porta");
            exit(-1);
        }
        String address = args[0];
        int port = Integer.parseInt(args[1]);
        Socket client;
        try {
            client = new Socket(address, port);
            System.out.println("Connessione stabilita all'indirizzo " + client.getRemoteSocketAddress() +
                    " sulla porta numero " + port);
        } catch (IOException e) {
            System.out.println("Errore nella creazione del socket client all'indirizzo " +
                    address + " sulla porta numero " + port);
            e.printStackTrace();
            return;
        }
        // PROCESSO DI AUTENTICAZIONE
        boolean autenticazione = true;
        boolean go = false;
        while (autenticazione){
            InputStreamReader is = new InputStreamReader(client.getInputStream());
            BufferedReader br = new BufferedReader(is);
            PrintWriter pw = new PrintWriter(client.getOutputStream());
            Scanner aut_scanner = new Scanner(System.in);
            String msg_to_send;
            String msg_received;

            System.out.println("****** Benvenuto su Iper2Go ******");
            System.out.println("\n-Inserisci nome utente per l'autenticazione");
            nomeutente= aut_scanner.next();
            System.out.println("-Inserisci password");
            String password= aut_scanner.next();

            //INVIO DELLE CREDENZIALI PER IL CONTROLLO E L'ACCESSO
            msg_to_send= "CHECK "+nomeutente+" "+password;
            //System.out.println("DEBUG: Invio credenziali per il controllo ");
            pw.println(msg_to_send);
            pw.flush();

            msg_received=br.readLine();
            //System.out.println("DEBUG: Messaggio ricevuto dal server: " +msg_received);
            if (msg_received.equals("CHECK_OK")){
                System.out.println("\n Autenticazione Avvenuta con successo!");
                System.out.println("\n Benvenuto "+nomeutente);
                go=true;
                autenticazione=false;
            }
            else if (msg_received.equals("CHECK_ERROR")) {
                System.out.println("\nErrore nell'autenticazione");
            }
            else {
                System.out.println("ERROR: comando non riconosciuto->"+msg_received);
            }
        }
        //AUTENTICAZIONE OK
        PrintWriter pw = new PrintWriter(client.getOutputStream());
        BufferedReader br = new BufferedReader(new InputStreamReader(client.getInputStream()));
        Scanner input = new Scanner(System.in);
        while (go) {
            try {
                System.out.println("*********************************");
                System.out.println("-Seleziona una voce per iniziare");
                System.out.println("1-Aggiungi prodotto");
                System.out.println("2-Ricerca prodotto ");
                System.out.println("3-Rimuovi prodotto");
                System.out.println("4-Visualizza elenco prodotti");
                System.out.println("5-Salva su File");
                System.out.println("0-Uscita dal programma");
                System.out.println("*********************************");
                System.out.println("\nScegliere un'opzione: ");

                int scelta = Integer.parseInt(input.nextLine());
                pw.println(scelta);
                pw.flush();

                switch (scelta) {
                    case 1: {  // AGGIUNTA
                        System.out.println("\nInserisci il nome del prodotto: ");
                        nomeprodotto = input.nextLine();
                        pw.println(nomeprodotto);
                        pw.flush();
                        String conferma_prodotto = br.readLine();
                        System.out.println(conferma_prodotto);
                        if (conferma_prodotto.equalsIgnoreCase("Impossibili inserire il prodotto nel sistema")
                                || conferma_prodotto.equalsIgnoreCase("Prodotto precedentemente inserito nel sistema"))
                            break;
                        else {
                            int sceltareparto;
                            boolean ciclo = true;
                            while (ciclo) {
                                System.out.println("\nSeleziona il Reparto in cui inserire il prodotto: ");
                                System.out.println("1- Bevande");
                                System.out.println("2- Conserve");
                                System.out.println("3- Dolci");
                                System.out.println("4- Formaggi");
                                System.out.println("5- Ortofrutta");
                                System.out.println("6- Pescheria");
                                System.out.println("7- Macelleria");
                                System.out.println("8- Salumeria");
                                System.out.println("9- Salati");
                                System.out.println("10- Surgelati");
                                sceltareparto = Integer.parseInt(input.nextLine());
                                switch (sceltareparto) {
                                    case 1: {
                                        nomereparto = "Bevande";
                                        ciclo = false;
                                        break;
                                    }
                                    case 2: {
                                        nomereparto = "Conserve";
                                        ciclo = false;
                                        break;
                                    }
                                    case 3: {
                                        nomereparto = "Dolci";
                                        ciclo = false;
                                        break;
                                    }
                                    case 4: {
                                        nomereparto = "Formaggi";
                                        ciclo = false;
                                        break;
                                    }
                                    case 5: {
                                        nomereparto = "Ortofrutta";
                                        ciclo = false;
                                        break;
                                    }
                                    case 6: {
                                        nomereparto = "Pescheria";
                                        ciclo = false;
                                        break;
                                    }
                                    case 7: {
                                        nomereparto = "Macelleria";
                                        ciclo = false;
                                        break;
                                    }
                                    case 8: {
                                        nomereparto = "Salumeria";
                                        ciclo = false;
                                        break;
                                    }
                                    case 9: {
                                        nomereparto = "Salati";
                                        ciclo = false;
                                        break;
                                    }
                                    case 10: {
                                        nomereparto = "Surgelati";
                                        ciclo = false;
                                        break;
                                    }
                                    default: {
                                        System.out.println("Errore: Devi inserire un valore tra 1 e 10");

                                    }
                                }
                            }
                            pw.println(nomereparto);
                            pw.flush();
                            //INSERIMENTO PREZZO
                            ciclo = true;
                            while (ciclo) {
                                try {
                                    System.out.println("\nInserisci il prezzo del prodotto[x.x][€]: ");
                                    prezzo = Double.parseDouble(input.nextLine());
                                    pw.println(prezzo);
                                    pw.flush();
                                    ciclo = false;
                                } catch (Exception e) {
                                    System.out.println("Errore: Formato non corretto, inserisci il prezzo nel formato di Esempio '1.5'");
                                }
                            }

                            // CONTROLLO INSERIMENTO SCADENZA
                            ciclo = true;
                            //INSERIMENTO SCADENZA
                            while (ciclo) {
                                System.out.println("\nInserisci scadenza prodotto [GG/MM/AA]: ");
                                scadenza = input.nextLine();
                                String dataodierna = df.format(new Date());
                                SimpleDateFormat format = new SimpleDateFormat("dd/mm/yy");
                                try {
                                    Date date1 = format.parse(scadenza);
                                    Date date2 = format.parse(dataodierna);

                                    if (date1.compareTo(date2) <= 0) {
                                        System.out.println("\nErrore: La data inserita è precedente alla data odierna, per favore reinserisci la data di scadenza");
                                    } else {
                                        pw.println(scadenza);
                                        pw.flush();
                                        ciclo = false;
                                    }
                                } catch (ParseException w) {
                                    System.out.println("Errore: Inserisci la data di scadenza nel formato dd/mm/yy");

                                }
                            }
                            //INSERIMENTO PESO
                            ciclo = true;
                            while (ciclo) {
                                try {
                                    System.out.println("\nInserisci peso prodotto [x.x][kg]: ");
                                    peso = Double.parseDouble(input.nextLine());
                                    pw.println(peso);
                                    pw.flush();
                                    String conferma_inserimento = br.readLine();
                                    System.out.println("\n"+conferma_inserimento);
                                    ciclo = false;
                                    break;
                                } catch (Exception e) {
                                    System.out.println("Errore: Formato non corretto, inserisci il peso nel formato di Esempio '1.5'");
                                }
                            }
                        }
                        break;
                    }

                    case 2: {
                        System.out.println("\nInserisci il nome del prodotto da cercare: ");
                        nomeprodotto = input.nextLine();
                        pw.println(nomeprodotto);
                        pw.flush();
                        br = new BufferedReader(new InputStreamReader(client.getInputStream()));
                        String conferma = br.readLine();
                        if (conferma.equalsIgnoreCase("Iper2Go: Prodotto non trovato")){
                            System.out.println("\nIper2Go: Prodotto non trovato");
                            break;
                        }
                        System.out.println("\nProdotto trovato : ");
                        System.out.println(conferma);
                        break;
                    }

                    case 3: {
                        System.out.println("\nNome del prodotto da eliminare: ");
                        nomeprodotto = input.nextLine();
                        pw.println(nomeprodotto);
                        pw.flush();
                        String conferma = br.readLine();
                        System.out.println(conferma);
                        break;
                    }

                    case 4: {

                        boolean go1 = true;
                        Scanner sc = new Scanner(client.getInputStream());
                        String msg;
                        System.out.println("Elenco dei prodotti : ");
                        while (go1) {
                            msg = sc.nextLine();
                            if (msg.equals("STOP")) {
                                System.out.println("Fine lista Prodotti\n");
                                go1 = false;
                            } else {
                                System.out.println(msg);
                            }
                        }
                        break;
                    }

                    case 5:{
                        System.out.println("\nSalvataggio su file in corso");
                        System.out.println(br.readLine());
                        break;
                    }

                    case 0: {
                        System.out.println("\nChiusura del programma...");
                        System.out.println("Arrivederci");
                        client.close();
                        exit(-1);
                        break;
                    }
                    default: {
                        System.out.println("Attenzione, puoi scegliere una voce del menù tra 0 e 5");
                    }

                }
            } catch (NumberFormatException e) {
                System.out.println("Seleziona una voce del menù inserendone il relativo numero");

            }
        }
    }
}