package com.tal.tal.todolist21;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

//an activity displaying the author, the version, and enables contact.
public class aboutActivity extends ActionBarActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
    }

    public  void closeButtonClicked (View v) {

        finish();
    }
//Send an email to the app creator
    public  void emailButtonClicked (View v) {
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{"coen.tal@gmail.com"});
        i.putExtra(Intent.EXTRA_SUBJECT, "Tal's TodoList App");
        i.putExtra(Intent.EXTRA_TEXT, "");
        try {
            startActivity(Intent.createChooser(i, "Sending mail..."));
            Toast.makeText(aboutActivity.this, "Send your email", Toast.LENGTH_LONG).show();


        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(aboutActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }


}
