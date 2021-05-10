package Model;

import android.database.Cursor;

import java.io.Serializable;

public class Account implements Serializable {
    private String id;
    private String email;
    private String password;
    private int role;
    private boolean state;

    public Account() {
    }

    public Account(final String id, final String email, final String password, final int role, final boolean state) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.role = role;
        this.state = state;
    }

    public Account(Cursor cursor){
        this.id = String.valueOf(cursor.getColumnIndex("id"));
        this.email = String.valueOf(cursor.getColumnIndex("email"));
        this.password = String.valueOf(cursor.getColumnIndex("password"));
        this.role = Integer.valueOf(cursor.getColumnIndex("role"));
        this.state = String.valueOf(cursor.getColumnIndex("state")) == "1";
    }

    public String getId() {
        return this.id;
    }

    public void setId(final String id) {
        this.id = id;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(final String email) {
        this.email = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }

    public int getRole() {
        return this.role;
    }

    public void setRole(final int role) {
        this.role = role;
    }

    public boolean isState() {
        return this.state;
    }

    public void setState(final boolean state) {
        this.state = state;
    }
}
