package telemetryconsole.com.example.Common;

import telemetryconsole.com.example.Util.StringHelper;

public class InvalidUserDetailsException extends Exception {

    String _username;
    String _password;

    @Override
    public String getMessage() {

        StringBuilder sb = new StringBuilder();

        sb.append("Invalid user details:");

        if (StringHelper.IsStringNullOrEmpty(_username)) {
            sb.append("\r\nUsername is null or empty");
        }

        if (StringHelper.IsStringNullOrEmpty(_password)) {
            sb.append("\r\nPassword is null or empty");
        }

        return sb.toString();
    }
    
    public InvalidUserDetailsException(String username, String password) {
        _username = username;
        _password = password;
    }
}
