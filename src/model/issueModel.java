package model;

public class issueModel {
    private String titolo;
    private String descrizione;
    private String tipologia;
    private String priorita;
    private String stato;
    private byte[] immagine;
    
    private utenteModel autore; 
    private utenteModel assegnatario;

    public issueModel(String titolo, String descrizione, String tipologia, String priorita, byte[] immagine, String autoreEmail, String assegnatario) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.tipologia = tipologia;
        this.priorita = priorita;
        this.stato = "TODO";
        this.immagine = immagine;
        
        this.autore = new utenteModel();
        this.autore.setEmail(autoreEmail);
        this.assegnatario = null;
    }
    
    public String getTitolo() { return titolo; }
    public String getDescrizione() { return descrizione; }
    public String getTipologia() { return tipologia; }
    public String getPriorita() { return priorita; }
    public String getStato() { return stato; }
    public byte[] getImmagine() { return immagine; }
    public utenteModel getAutore() { return autore; }
    public utenteModel getAssegnatario() { return assegnatario; }

}