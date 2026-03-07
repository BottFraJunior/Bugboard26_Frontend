package model;

public class utenteModel {

    private String nome;
    private String email;
    private String password;
    private String ruolo;
    private String identificativo;

    public utenteModel() {}

    public utenteModel(String nome, String email, String password, String ruolo, String id) {
        this.nome = nome;
        this.email = email;
        this.password = password;
        this.ruolo = ruolo;
        this.identificativo = id;
    }
    
    
    public String getNome() { return nome; }
    public String getEmail() { return email; }
    public String getPassword() { return password; }	
    public String getRuolo() { return ruolo; }
    
    public void setEmail(String email) { this.email = email; } 
}