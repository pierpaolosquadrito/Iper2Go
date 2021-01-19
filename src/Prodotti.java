import java.io.*;
import java.util.*;


public class  Prodotti implements Serializable{

    ArrayList<Prodotto> lista_prodotti = new ArrayList<Prodotto>();
    Prodotto prodotto_trovato = null;
    String last_modification;

    public  synchronized void inserisciProdotto(Prodotto p) {
        last_modification = new Date().toString();
        lista_prodotti.add(p);
        salvaSer();
    }


    //RESTITUISCE Boolean
    public synchronized boolean cercaProdotto(String nomeprodotto) {
        for (Prodotto p : lista_prodotti) {
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
        return lista_prodotti.size();
    }



    public synchronized void incrementaQuantita(String nomeprodotto){
        for (Prodotto p : lista_prodotti) {
            if (p.getNomeprodotto().equalsIgnoreCase(nomeprodotto)) {
                prodotto_trovato = p;
                Integer b = prodotto_trovato.getQuantita();
                b++;
                prodotto_trovato.setQuantita(b);
                last_modification = new Date().toString();
                salvaSer();
            }
        }
        last_modification = new Date().toString();
    }



    //RESTITUISCE OGGETTO
    public synchronized Prodotto restituisciProdotto(String nomeprodotto){
        for (Prodotto p : lista_prodotti) {
            if (p.getNomeprodotto().contains(nomeprodotto)) {
                //prodotto_trovato = p;
                last_modification = new Date().toString();
                return p;
            }
        }
        return null;
    }

    public synchronized String aggiornaQuantita(String nomeprodotto, Integer quantita) {
        prodotto_trovato = restituisciProdotto(nomeprodotto);
        if(prodotto_trovato!=null){
        Integer temp = prodotto_trovato.getQuantita();
        String ritorno;
        temp += quantita;
        if(temp <= 0){
            eliminaProdotto(prodotto_trovato);
            return "CANC";
        }else{
            prodotto_trovato.setQuantita(temp);
            salvaSer();
            return ritorno= temp.toString();
        }
        }
        last_modification = new Date().toString();
        return "VUOTO";
    }


    public void ordinaListaProdotti() {
        Collections.sort(lista_prodotti);
    }

    public ArrayList visualizzaListaProdotti() throws IOException, ClassNotFoundException {
        caricaFile();
        ordinaListaProdotti();
        return lista_prodotti;
    }

    public synchronized void eliminaProdotto(Prodotto p) {
        last_modification = new Date().toString();
        lista_prodotti.remove(p);
        salvaSer();
    }


    public synchronized void salvaSuFile() {
        try {
            if(quantitaProdotti()==0){
                System.out.println("Elenco vuoto,il salvataggio su file non verrà eseguito");
            }
            else {
                ordinaListaProdotti();
                last_modification = new Date().toString();
                FileWriter fw = new FileWriter("Iper2go_Listaprodotti_.txt");
                BufferedWriter bw = new BufferedWriter(fw);
                for (Prodotto p : lista_prodotti) {
                    bw.write(p.toString());
                    bw.newLine();
                }
                bw.flush();
                bw.close();
                salvaSer();
            }
        } catch (IOException e) {
            System.out.println("Errore durante il salvataggio");
            e.printStackTrace();
        }
    }

    public synchronized void salvaSer() {
        try {
            if (quantitaProdotti() == 0) {
                System.out.println("Elenco vuoto, impossibile serializzare l'oggetto arraylist");
            } else {
                ordinaListaProdotti();
                last_modification = new Date().toString();
                FileOutputStream filestream = new FileOutputStream("src/Iper2Go_Magazzino.ser");
                ObjectOutputStream os = new ObjectOutputStream(filestream);
                os.writeObject(lista_prodotti);
                os.close();
            }
        } catch (Exception e) {

        }
    }
    public synchronized void caricaFile() throws IOException, ClassNotFoundException {
        FileInputStream caricastream = new FileInputStream("src/Iper2Go_Magazzino.ser");
        ObjectInputStream os = new ObjectInputStream(caricastream);
        Object l = os.readObject();
        ArrayList<Prodotto> lista = (ArrayList<Prodotto>) l;
        lista_prodotti = lista;
    }
}