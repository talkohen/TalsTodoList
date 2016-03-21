package com.tal.tal.todolist21;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


// an activity for inviting other members to join the app
public class inviteTeamMembersActivity extends ActionBarActivity {

    EditText member1Input;
    EditText member2Input;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_invite_team_members);

        member1Input = (EditText) findViewById(R.id.member1Input);
        member2Input = (EditText) findViewById(R.id.member2Input);
    }

    public void inviteButtonClicked (View v) {

        Button inviteButton = (Button) v;

        //Get email addresses from input and start email app with built in content
        Intent i = new Intent(Intent.ACTION_SEND);
        i.setType("message/rfc822");
        i.putExtra(Intent.EXTRA_EMAIL, new String[]{member1Input.getText().toString(), member2Input.getText().toString(),});
        i.putExtra(Intent.EXTRA_SUBJECT, "TodoList Invitation");
        i.putExtra(Intent.EXTRA_TEXT, "youv'e been invited to my group on Tal's Todo List !. Check out your tasks." +
                " click on this link to download : " +
                "https://play.google.com/store/apps/details?id=com.tal.tal.todolist21&hl=en");
        try {
            startActivity(Intent.createChooser(i, "Sending mail..."));
            Toast.makeText(inviteTeamMembersActivity.this, "Send your email",Toast.LENGTH_LONG).show();


        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(inviteTeamMembersActivity.this, "There are no email clients installed.", Toast.LENGTH_SHORT).show();
        }

    }

    public void cancelButtonClicked (View v) {

        Button loginButton = (Button) v;

        finish();
    }

}
