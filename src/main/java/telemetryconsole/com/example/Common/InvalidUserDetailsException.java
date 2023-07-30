package telemetryconsole.com.example.Common;

import telemetryconsole.com.example.Util.StringHelper;

public class InvalidUserDetailsException extends Exception {

    String username;
    String password;

    @Override
    public String getMessage() {

        StringBuilder sb = new StringBuilder();

        sb.append("Invalid user details:");

        if (StringHelper.isStringNullOrEmpty(username)) {
            sb.append("\r\nUsername is null or empty");
        }

        if (StringHelper.isStringNullOrEmpty(password)) {
            sb.append("\r\nPassword is null or empty");
        }

        return sb.toString();
    }
    
    public InvalidUserDetailsException(String username, String password) {
        this.username = username;
        this.password = password;
    }
}
