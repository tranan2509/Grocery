package Model;

import android.database.Cursor;

public class Branch {
    private int id;
    private String name;
    private String address;
    private boolean state;

    public Branch() {
    }

    public Branch(final String name, final String address, final boolean state) {
        this.name = name;
        this.address = address;
        this.state = state;
    }

    public Branch(Cursor cursor){
        id = cursor.getInt(cursor.getColumnIndex("id"));
        name = cursor.getString(cursor.getColumnIndex("name"));
        address = cursor.getString(cursor.getColumnIndex("address"));
        state = cursor.getString(cursor.getColumnIndex("state")).equals("1");
    }

    public int getId() {
        return this.id;
    }

    public void setId(final int id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(final String address) {
        this.address = address;
    }

    public boolean isState() {
        return this.state;
    }

    public void setState(final boolean state) {
        this.state = state;
    }
}
