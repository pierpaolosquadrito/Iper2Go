import java.io.*;
import java.util.*;


public class Prodotti {

    ArrayList<Prodotto> prodotti = new ArrayList<Prodotto>();
    Prodotto prodotto_trovato = null;
    String last_modification;

    public void inserisciProdotto(Prodotto p) {
        last_modification = new Date().toString();
        prodotti.add(p);
    }
    //RESTITUISCE SOLO TRUE O FALSE
    public boolean cercaProdotto(String nomeprodotto) {
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

    public  void incrementaQuantita(String nomeprodotto){
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
    public Prodotto restituisciProdotto(String nomeprodotto){
        for (Prodotto p : prodotti) {
            if (p.getNomeprodotto().equalsIgnoreCase(nomeprodotto)) {
                prodotto_trovato = p;
                last_modification = new Date().toString();
                return prodotto_trovato;
            }
        }
        last_modification = new Date().toString();
        return null;
    }

    public int aggiornaQuantita(String nomeprodotto, Float quantita) {
        prodotto_trovato = restituisciProdotto(nomeprodotto);
        if(prodotto_trovato!=null){
        Float temp = prodotto_trovato.getQuantita();
        temp += quantita;
        if(temp <= 0){
            eliminaProdotto(prodotto_trovato);
            return 1;
        }else{
            prodotto_trovato.setQuantita(temp);
            return 2;
        }
        }
        last_modification = new Date().toString();
        return 3;
    }

    public void ordinaListaProdotti() {
        Collections.sort(prodotti);
    }

    public ArrayList visualizzaListaProdotti() throws IOException {
        return  prodotti;
    }

    public void eliminaProdotto(Prodotto p) {
        last_modification = new Date().toString();
        prodotti.remove(p);
    }

    public synchronized void salvaSuFile() {
        try {
            ordinaListaProdotti();
            last_modification = new Date().toString();
            FileWriter fw = new FileWriter("Iper2go_Listaprodotti_"+last_modification+".txt");
            BufferedWriter bw = new BufferedWriter(fw);
            for (Prodotto p : prodotti) {
                bw.write(p.toString());
                bw.newLine();
            }
            bw.flush();
            bw.close();
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio");
            e.printStackTrace();
        }
    }
}