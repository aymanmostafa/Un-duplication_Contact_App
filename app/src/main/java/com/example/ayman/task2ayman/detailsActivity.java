package com.example.ayman.task2ayman;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class detailsActivity extends AppCompatActivity implements View.OnClickListener {

    DataBaseHandle my;
    Button call, sms, emailbtn, edit, delete;
    TextView name, phone, emailview;
    Contact temp;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        call = (Button) findViewById(R.id.callid);
        call.setOnClickListener(this);
        sms = (Button) findViewById(R.id.smsid);
        sms.setOnClickListener(this);
        emailbtn = (Button) findViewById(R.id.mailid);
        emailbtn.setOnClickListener(this);
        edit = (Button) findViewById(R.id.editid);
        edit.setOnClickListener(this);
        delete = (Button) findViewById(R.id.deleteid);
        delete.setOnClickListener(this);
        name = (TextView) findViewById(R.id.nameviewid);
        phone = (TextView) findViewById(R.id.phoneviewid);
        emailview = (TextView) findViewById(R.id.mailviewid);
        Intent come = getIntent();
        my = new DataBaseHandle(this);
        temp = my.getContactByID(Integer.parseInt(come.getStringExtra("res")));
        name.setText(temp.getName());
        phone.setText(temp.getPhone());
        emailview.setText(temp.getEmail());
    }

    @Override
    public void onClick(View v) {
        if (v == call) {
            Intent call = new Intent(Intent.ACTION_CALL);
            call.setData(Uri.parse("tel:" + temp.getPhone()));
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {
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
        else if(v==sms){
            Intent intent = new Intent(Intent.ACTION_SENDTO);
            intent.setData(Uri.parse("smsto:" + Uri.encode(temp.getPhone())));
            startActivity(intent);
        }
        else if(v==emailbtn){
            if(temp.getEmail().matches(" ")){
                emailview.setText("This Contact hasn't an email");
            }
            else {
                Intent email = new Intent(Intent.ACTION_SEND);
                email.setData(Uri.parse("mailto:"));
                email.setType("text/plain");
                email.putExtra(Intent.EXTRA_EMAIL, new String[]{temp.getEmail()});
                email.putExtra(Intent.EXTRA_SUBJECT, "subject");
                email.putExtra(Intent.EXTRA_TEXT, "Email Body");
                startActivity(Intent.createChooser(email, "Send mail..."));
            }
        }
        else if(v==edit){
            Intent gotoedit=new Intent(this,editActivity.class);
            Integer x=temp.getId();
            gotoedit.putExtra("edi",x.toString());
            startActivity(gotoedit);
        }
        else if(v==delete){
            my.deleteContactByID(temp.getId());
            Intent gotoback=new Intent(this,dataActivity.class);
            gotoback.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(gotoback);
            finish();
        }
    }
}
