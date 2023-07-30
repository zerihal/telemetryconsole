package telemetryconsole.com.example;

import java.util.Date;

public abstract class QueryItem {
    private Date dateLogged;

    public Date getDateLogged() {
        return dateLogged;
    }

    public void setDateLogged(Date dateLogged) {
        this.dateLogged = dateLogged;
    }
}
