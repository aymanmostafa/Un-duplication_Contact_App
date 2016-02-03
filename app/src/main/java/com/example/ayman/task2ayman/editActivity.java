package com.example.ayman.task2ayman;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import java.util.regex.Pattern;

public class editActivity extends AppCompatActivity implements View.OnClickListener {

    DataBaseHandle my;
    Contact t;
    Button save,delete,cancel;
    EditText name,phone,email;
    TextView warnn,warnp,warne;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        my=new DataBaseHandle(this);
        Intent come=getIntent();
        t=my.getContactByID(Integer.parseInt(come.getStringExtra("edi")));
        save =(Button) findViewById(R.id.savetxtid);
        save.setOnClickListener(this);
        delete=(Button) findViewById(R.id.deletetxtid);
        delete.setOnClickListener(this);
        cancel=(Button) findViewById(R.id.canceltxtid);
        cancel.setOnClickListener(this);
        name=(EditText) findViewById(R.id.nametxtid);
        phone=(EditText) findViewById(R.id.phonetxtid);
        email=(EditText) findViewById(R.id.emailtxtid);
        name.setText(t.getName());
        phone.setText(t.getPhone());
        if(t.getEmail().matches(" "))
            email.setText("");
        else email.setText(t.getEmail());
        warnn=(TextView) findViewById(R.id.warnname);
        warnp=(TextView) findViewById(R.id.warnphone);
        warne=(TextView) findViewById(R.id.warnemail);

    }
    @Override
    public void onClick(View v) {
        if(v==cancel){
            //super.onBackPressed();
            Intent gotoback=new Intent(this,dataActivity.class);
            gotoback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(gotoback);
            finish();
        }
        else if(v==save){
            String n=name.getText().toString();
            String p=phone.getText().toString();
            String e=email.getText().toString();

            if(Pattern.matches("[0]{1}[1]{1}[012]{1}[0-9]{8}", p)&&!n.matches("")&&(e.matches("")||(Pattern.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$",e)))){
                {
                    if(e.matches("")) e=" ";
                    Contact newContact=new Contact(t.getId(),n,p,e);
                    my.updateContactByID(newContact);
                    Intent gotoback=new Intent(this,dataActivity.class);
                    gotoback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(gotoback);
                    finish();
                }
            }
            else {
                warnn.setText("");
                warnp.setText("");
                warne.setText("");
                if(!Pattern.matches("[0]{1}[1]{1}[012]{1}[0-9]{8}",p)){
                    warnp.setText("Enter valid phone");
                }
                if(n.matches("")){
                    warnn.setText("Enter valid name");
                }
                if(!e.matches("")&&!Pattern.matches("^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\])|(([a-zA-Z\\-0-9]+\\.)+[a-zA-Z]{2,}))$",e)){
                    warne.setText("Enter valid Email");
                }
            }
        }
        else if(v==delete){
            my.deleteContactByID(t.getId());
            Intent gotoback=new Intent(this,dataActivity.class);
            gotoback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(gotoback);
            finish();
        }
    }

    @Override
    public void onBackPressed() {
        Intent gotoback=new Intent(this,dataActivity.class);
        startActivity(gotoback);
        finish();
    }
}
