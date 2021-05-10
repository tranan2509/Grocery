package Model;

import android.database.Cursor;

import java.io.Serializable;

public class Account implements Serializable {
    private String id;
    private String email;
    private String password;
    private int role;
    private boolean state;
    private boolean isEmail;

    public Account() {
    }

    public Account(final String id, final String email, final String password, final boolean isEmail, final int role, final boolean state) {
        this.id = id;
        this.email = email;
        this.password = password;
        this.isEmail= isEmail;
        this.role = role;
        this.state = state;
    }

    public Account(Cursor cursor){
        this.id = cursor.getString(cursor.getColumnIndex("id"));
        this.email = cursor.getString(cursor.getColumnIndex("email"));
        this.password = cursor.getString(cursor.getColumnIndex("password"));
        this.isEmail = cursor.getString(cursor.getColumnIndex("isEmail")).equals("1");
        this.role = Integer.valueOf(cursor.getString(cursor.getColumnIndex("role")));
        this.state = cursor.getString(cursor.getColumnIndex("state")).equals("1");
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

    public boolean isEmail() {
        return this.isEmail;
    }

    public void setEmail(final boolean email) {
        this.isEmail = email;
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
