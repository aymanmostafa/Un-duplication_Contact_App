package com.example.ayman.task2ayman;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;

import java.util.HashMap;
import java.util.Map;

public class dupActivity extends AppCompatActivity implements View.OnClickListener {

    Button del,dup;
    DataBaseHandle my;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dup);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        del=(Button) findViewById(R.id.delallid);
        del.setOnClickListener(this);
        dup=(Button) findViewById(R.id.dupid);
        dup.setOnClickListener(this);
        my=new DataBaseHandle(this);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onClick(View v) {
        if(v==del){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            alertBuilder.setTitle("Deleting");
            alertBuilder.setMessage("Are you sure?");

            DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                    for(int i=0;i<dataActivity.mylist2.size();i++)
                        my.deleteContactByID(dataActivity.mylist2.get(i).getId());
                    Intent gotoback=new Intent(dupActivity.this,dataActivity.class);
                    gotoback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(gotoback);
                    finish();

                }
            };
            DialogInterface.OnClickListener negatuveListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            };

            alertBuilder.setPositiveButton("OK", positiveListener);
            alertBuilder.setNegativeButton("Cancel", negatuveListener);

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
        else if(v==dup){
            AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);

            alertBuilder.setTitle("Deleting");
            alertBuilder.setMessage("We will delete duplication in contacts by remove old one(s),OK?");

            DialogInterface.OnClickListener positiveListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Map m = new HashMap();
                    for(int i=0;i<dataActivity.mylist2.size();i++){
                        String k=dataActivity.mylist2.get(i).getPhone();
                        if(m.get(k)==null){
                            m.put(k,dataActivity.mylist2.get(i).getId());
                        }
                        else{
                            int x= (int) m.get(k);
                            if(x<dataActivity.mylist2.get(i).getId())
                                m.put(k,dataActivity.mylist2.get(i).getId());
                        }
                    }

                    for(int i=0;i<dataActivity.mylist2.size();i++){
                        if((int)m.get(dataActivity.mylist2.get(i).getPhone())!=dataActivity.mylist2.get(i).getId())
                            my.deleteContactByID(dataActivity.mylist2.get(i).getId());
                    }
                    Intent gotoback=new Intent(dupActivity.this,dataActivity.class);
                    gotoback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(gotoback);
                    finish();

                }
            };
            DialogInterface.OnClickListener negatuveListener = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {

                }
            };

            alertBuilder.setPositiveButton("OK", positiveListener);
            alertBuilder.setNegativeButton("Cancel", negatuveListener);

            AlertDialog alertDialog = alertBuilder.create();
            alertDialog.show();
        }
    }
}
