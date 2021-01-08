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
            FileWriter fw = new FileWriter("Iper2go_Listaprodotti"+last_modification+".txt");
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