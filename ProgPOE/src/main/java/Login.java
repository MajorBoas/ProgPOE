public class Login {
    private String registeredUsername;
    private String registeredPassword;
    private String registeredCell;

    public void setRegisteredDetails(String username, String password, String cell) {
        this.registeredUsername = username;
        this.registeredPassword = password;
        this.registeredCell = cell;
    }

    public boolean loginUser(String username, String password) {
        if (registeredUsername == null || registeredPassword == null) return false;
        return registeredUsername.equals(username) && registeredPassword.equals(password);
    }

    public String returnLoginStatus(boolean loggedIn) {
        if (loggedIn) {
            String first = "";
            String last = "";
            if (registeredUsername != null && registeredUsername.contains("_")) {
                String[] parts = registeredUsername.split("_", 2);
                first = parts[0];
                last = parts.length > 1 ? parts[1] : "";
            } else if (registeredUsername != null) {
                first = registeredUsername;
            }
            return String.format("Welcome %s %s, it is great to see you.", first, last).trim();
        } else {
            return "Username or password incorrect, please try again.";
        }
    }
}
