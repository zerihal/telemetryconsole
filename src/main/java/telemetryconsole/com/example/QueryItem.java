package telemetryconsole.com.example;

import java.util.Date;

public abstract class QueryItem {
    private Date _dateLogged;

    public Date get_dateLogged() {
        return _dateLogged;
    }

    public void set_dateLogged(Date _dateLogged) {
        this._dateLogged = _dateLogged;
    }
}
