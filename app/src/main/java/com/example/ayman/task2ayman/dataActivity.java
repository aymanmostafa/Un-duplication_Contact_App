package com.example.ayman.task2ayman;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Collections;

public class dataActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, View.OnClickListener, AdapterView.OnItemLongClickListener {

    DataBaseHandle my;
    ListView list;
    Button add, del;
    EditText search;
    ArrayAdapter ada;
    protected static ArrayList<Contact> mylist2 = new ArrayList<Contact>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_data);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        my = new DataBaseHandle(this);
        mylist2 = my.getAllContacts();
        list = (ListView) findViewById(R.id.viewid);
        list.setOnItemClickListener(this);
        list.setOnItemLongClickListener(this);


        for (int i = 0; i < mylist2.size(); i++) {
            for (int k = 0; k < mylist2.size(); k++) {
                if (mylist2.get(i).getName().toLowerCase().compareTo(mylist2.get(k).getName().toLowerCase()) < 0)
                    Collections.swap(mylist2, i, k);
            }
        }


        ada = new ArrayAdapter(this, android.R.layout.simple_list_item_1, mylist2);
        list.setAdapter(ada);
        add = (Button) findViewById(R.id.addid);
        add.setOnClickListener(this);
        search = (EditText) findViewById(R.id.searchid);

        del = (Button) findViewById(R.id.delid);
        del.setOnClickListener(this);
        search.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                dataActivity.this.ada.getFilter().filter(s);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });


        //ada.notifyDataSetChanged(); to be notified from adapter


    }

    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


        Contact a = (Contact) parent.getAdapter().getItem(position);
        Intent gotoview = new Intent(this, detailsActivity.class);
        Integer x = a.getId();
        gotoview.putExtra("res", x.toString());
        startActivity(gotoview);
    }

    @Override
    public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {


        final Contact a = (Contact) parent.getAdapter().getItem(position);

        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

        alertBuilder.setTitle("Options");
        alertBuilder.setMessage("Choose one option");

        DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent gotoback = new Intent(dataActivity.this, editActivity.class);
                Integer x = a.getId();
                gotoback.putExtra("edi", x.toString());
                dataActivity.this.startActivity(gotoback);
                finish();


            }
        };

        DialogInterface.OnClickListener neuListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent call = new Intent(Intent.ACTION_CALL);
                call.setData(Uri.parse("tel:" + a.getPhone()));
                if (ActivityCompat.checkSelfPermission(dataActivity.this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                startActivity(call);

            }
        };

        DialogInterface.OnClickListener negatuveListener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                my.deleteContactByID(a.getId());
                Intent gotoback=new Intent(dataActivity.this,dataActivity.class);
                gotoback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                dataActivity.this.startActivity(gotoback);
                finish();

            }
        };

        alertBuilder.setPositiveButton("EDIT", positiveListener);
        alertBuilder.setNegativeButton("DELETE", negatuveListener);
        alertBuilder.setNeutralButton("CALL", neuListener);
        AlertDialog alertDialog = alertBuilder.create();
        alertDialog.show();



        return true;
    }


    @Override
    public void onClick(View v) {
        if(v==add){
            Intent gotoadd=new Intent(this,addActivity.class);
            startActivity(gotoadd);
        }
        else if(v==del){
            Intent gotodup=new Intent(this,dupActivity.class);
            startActivity(gotodup);

        }
    }


}
