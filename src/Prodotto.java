import java.io.Serializable;

public class Prodotto implements Comparable<Prodotto> , Serializable {
    String nomeprodotto;
    String scadenza;
    Double prezzo;
    String reparto;
    Double peso;
    Integer quantita;

    public Prodotto (String nomeprodotto, String scadenza, Double prezzo, String reparto, Double peso, Integer quantita){
        this.setNomeprodotto(nomeprodotto);
        this.setScadenza(scadenza);
        this.setPrezzo(prezzo);
        this.setReparto(reparto);
        this.setPeso(peso);
        this.setQuantita(quantita);
    }

    public String getNomeprodotto() {
        return nomeprodotto;
    }

    public void setNomeprodotto(String nomeprodotto) {
        this.nomeprodotto = nomeprodotto;
    }

    public String getScadenza() {
        return scadenza;
    }

    public void setScadenza(String scadenza) {
        this.scadenza = scadenza;
    }

    public Double getPrezzo() {
        return prezzo;
    }

    public void setPrezzo(Double prezzo) {
        this.prezzo = prezzo;
    }

    public String getReparto() {
        return reparto;
    }

    public Integer getQuantita() {
        return quantita;
    }

    public void setQuantita(Integer quantita) {
        this.quantita = quantita;
    }

    public void setReparto(String reparto) {
        this.reparto = reparto;
    }

    public Double getPeso() {
        return peso;
    }

    public void setPeso(Double peso) {
        this.peso = peso;
    }

    public String toString() {
        return
                "Nome del Prodotto: " + getNomeprodotto() +
                        "  Scadenza: " + getScadenza() +
                        "  Prezzo: €" + getPrezzo() +
                        "  Reparto: " + getReparto() +
                        "  Peso: " + getPeso() +"kg" +
                        "  Quantità in magazzino: " + getQuantita();
    }

    @Override
    public int compareTo(Prodotto p) {

        return getReparto().compareTo(p.getReparto());
    }
}

