public class Prodotto implements Comparable<Prodotto> {
    String nomeprodotto;
    String scadenza;
    Double prezzo;
    String reparto;
    Double peso;

    public Prodotto (String nomeprodotto, String scadenza, Double prezzo, String reparto, Double peso){
        this.setNomeprodotto(nomeprodotto);
        this.setScadenza(scadenza);
        this.setPrezzo(prezzo);
        this.setReparto(reparto);
        this.setPeso(peso);
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
                        "\nScadenza: " + getScadenza() +
                        "\nPrezzo: €" + getPrezzo() +
                        "\nReparto: " + getReparto() +
                        "\nPeso: " + getPeso() +"kg" +
                        "\n***********************";
    }

    @Override
    public int compareTo(Prodotto p) {
        return getNomeprodotto().compareTo(p.getNomeprodotto());
    }
}

