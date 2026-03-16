package model;

public class sessionManager {

	//Pattern Singleton
    private static sessionManager instance;
    private utenteModel currentUser;

    private sessionManager() {}	

    public static sessionManager getInstance() {
        if (instance == null) {
            instance = new sessionManager();
        }
        return instance;
    }

    public void setCurrentUser(utenteModel user) {
        this.currentUser = user;
    }

    public utenteModel getCurrentUser() {
        return currentUser;
    }
    
  
}