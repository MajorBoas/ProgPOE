public class Register {
    private String registeredUsername;
    private String registeredPassword;
    private String registeredCell;

    // Username must contain underscore and be <= 5 characters
    public boolean checkUserName(String username) {
        if (username == null) return false;
        return username.contains("_") && username.length() <= 5;
    }

    // Password complexity: >=8 chars, 1 uppercase, 1 digit, 1 special
    public boolean checkPasswordComplexity(String password) {
        if (password == null) return false;
        if (password.length() < 8) return false;
        boolean hasUpper=false, hasDigit=false, hasSpecial=false;
        for (char c : password.toCharArray()) {
            if (Character.isUpperCase(c)) hasUpper = true;
            else if (Character.isDigit(c)) hasDigit = true;
            else if (!Character.isLetterOrDigit(c)) hasSpecial = true;
        }
        return hasUpper && hasDigit && hasSpecial;
    }

    // Cell must start with +27 or other +country and digits count 9..12 after '+'
    public boolean checkCellPhoneNumber(String cell) {
        if (cell == null) return false;
        if (!cell.startsWith("+")) return false;
        String digits = cell.substring(1);
        if (digits.length() < 9 || digits.length() > 12) return false;
        for (char c : digits.toCharArray()) {
            if (!Character.isDigit(c)) return false;
        }
        return true;
    }

    public String registerUser(String username, String password, String cell) {
        if (!checkUserName(username)) return "Username is not correctly formatted";
        if (!checkPasswordComplexity(password)) return "Password does not meet complexity";
        if (!checkCellPhoneNumber(cell)) return "Cell number is not correct";
        this.registeredUsername = username;
        this.registeredPassword = password;
        this.registeredCell = cell;
        return "User successfully registered.";
    }

    public String getRegisteredUsername() { return registeredUsername; }
    public String getRegisteredPassword() { return registeredPassword; }
    public String getRegisteredCell() { return registeredCell; }
}
