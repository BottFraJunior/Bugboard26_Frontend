package model;

public class issueModel {
    private String titolo;
    private String descrizione;
    private String tipologia;
    private String priorita;
    private String stato;
    private byte[] immagine;
    
    private utenteModel autore; 

    public issueModel(String titolo, String descrizione, String tipologia, String priorita, byte[] immagine, String autoreEmail) {
        this.titolo = titolo;
        this.descrizione = descrizione;
        this.tipologia = tipologia;
        this.priorita = priorita;
        this.stato = "TODO";
        this.immagine = immagine;
        
        this.autore = new utenteModel();
        this.autore.setEmail(autoreEmail);
    }

}