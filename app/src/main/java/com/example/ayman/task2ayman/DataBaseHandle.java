package com.example.ayman.task2ayman;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;

/**
 * Created by Ayman on 08-Dec-15.
 */
public class DataBaseHandle extends SQLiteOpenHelper {

    public DataBaseHandle(Context context) {
        super(context, "ayman", null, 1);
    }

    @Override

    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_CONTACTS_TABLE = "CREATE TABLE Contacts ( ID INTEGER PRIMARY KEY autoincrement,Name TEXT not null,Phone TEXT not null,Email Text)";
        db.execSQL(CREATE_CONTACTS_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS Contacts");
        // Create tables again
        onCreate(db);
    }

    public void addContact(Contact oneContact)
    {
        SQLiteDatabase mydb = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Name", oneContact.getName());
        values.put("Phone", oneContact.getPhone());
        values.put("Email",oneContact.getEmail());

        mydb.insert("Contacts", null, values);
        mydb.close();
    }

    public Contact getContactByID(int contactID)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query("Contacts", new String[] {"Name","Phone","Email"},
                "ID=?", new String[] { String.valueOf(contactID) }
                , null, null, null, null);

        if (cursor != null)
            cursor.moveToFirst();

        String retName = cursor.getString(0);
        String retPhone = cursor.getString(1);
        String retEmail = cursor.getString(2);

        Contact myContact = new Contact(contactID,retName,retPhone,retEmail);
        Contact oneCont = new Contact();
        return myContact;
    }

    public String getPhoneByName(String name)
    {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query("Contacts", new String[] {"Phone"},"Name =?", new String[] { name }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        String phoneStr = cursor.getString(0);
        // return contact
        return phoneStr;
    }

    public int updateContactByID(Contact oneContact)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put("Name", oneContact.getName());
        values.put("Phone", oneContact.getPhone());
        values.put("Email",oneContact.getEmail());

        int updateItems = db.update("Contacts", values,"ID = ?", new String[] {String.valueOf(oneContact.getId())} );
        db.close();
        return updateItems;

    }

    public ArrayList<Contact> getAllContacts()
    {
        ArrayList<Contact> contactsList = new ArrayList<Contact>();
        // Select All Query
        String selectQuery = "SELECT  * FROM Contacts";

        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do
            {
                int id = cursor.getInt(0);
                String oneName = cursor.getString(1);
                String phone = cursor.getString(2);
                String email = cursor.getString(3);

                Contact oneContact = new Contact(id,oneName,phone,email);

                contactsList.add(oneContact);
            } while (cursor.moveToNext());
        }
        db.close();
        // return contact list
        return contactsList;
    }

    public int getContactsCount()
    {
        String countQuery = "SELECT  * FROM Contacts";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);

        db.close();
        return cursor.getCount();
    }

    public void deleteContactByID(int snapshotID)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("Contacts", "ID = ?", new String[]{String.valueOf(snapshotID)});
        db.close();
    }
}
