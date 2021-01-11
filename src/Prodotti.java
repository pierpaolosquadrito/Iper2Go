import java.io.*;
import java.util.*;


public class  Prodotti {

    ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
    Prodotto prodotto_trovato = null;
    String last_modification;

    public  synchronized void inserisciProdotto(Prodotto p) {
        last_modification = new Date().toString();
        prodotti.add(p);
    }
    //RESTITUISCE SOLO TRUE O FALSE
    public synchronized boolean cercaProdotto(String nomeprodotto) {
        for (Prodotto p : prodotti) {
            if (p.getNomeprodotto().equalsIgnoreCase(nomeprodotto)) {
                prodotto_trovato = p;
                last_modification = new Date().toString();
                return true;
            }
        }
        last_modification = new Date().toString();
        return false;
    }

    public int quantitaProdotti() {
        last_modification = new Date().toString();
        return prodotti.size();
    }

    public synchronized void incrementaQuantita(String nomeprodotto){
        for (Prodotto p : prodotti) {
            if (p.getNomeprodotto().equalsIgnoreCase(nomeprodotto)) {
                prodotto_trovato = p;
                Float b = prodotto_trovato.getQuantita();
                b++;
                prodotto_trovato.setQuantita(b);
                last_modification = new Date().toString();

            }
        }
        last_modification = new Date().toString();
    }
    //RESTITUISCE OGGETTO
    public synchronized Prodotto restituisciProdotto(String nomeprodotto){
        for (Prodotto p : prodotti) {
            if (p.getNomeprodotto().equalsIgnoreCase(nomeprodotto)) {
                //prodotto_trovato = p;
                last_modification = new Date().toString();
                return p;
            }
        }
        return null;
    }

    public synchronized float aggiornaQuantita(String nomeprodotto, Float quantita) {
        prodotto_trovato = restituisciProdotto(nomeprodotto);
        if(prodotto_trovato!=null){
        Float temp = prodotto_trovato.getQuantita();
        temp += quantita;
        if(temp <= 0){
            eliminaProdotto(prodotto_trovato);
            return (float) 1.50;
        }else{
            prodotto_trovato.setQuantita(temp);
            return temp;
        }
        }
        last_modification = new Date().toString();
        return (float) 3.50;
    }

    public void ordinaListaProdotti() {
        Collections.sort(prodotti);
    }

    public ArrayList visualizzaListaProdotti() throws IOException {
        ordinaListaProdotti();
        return  prodotti;
    }

    public synchronized void eliminaProdotto(Prodotto p) {
        last_modification = new Date().toString();
        prodotti.remove(p);
    }

    public synchronized void salvaSuFile() {
        try {
            if(quantitaProdotti()==0){
                System.out.println("Elenco vuoto,il salvataggio su file non verrà eseguito");
            }
            else {
                ordinaListaProdotti();
                last_modification = new Date().toString();
                FileWriter fw = new FileWriter("Iper2go_Listaprodotti_" + last_modification + ".txt");
                BufferedWriter bw = new BufferedWriter(fw);
                for (Prodotto p : prodotti) {
                    bw.write(p.toString());
                    bw.newLine();
                }
                bw.flush();
                bw.close();
            }
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio");
            e.printStackTrace();
        }
    }
}