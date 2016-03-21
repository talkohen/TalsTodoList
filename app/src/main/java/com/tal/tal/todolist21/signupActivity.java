package com.tal.tal.todolist21;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


// an activity for adding an new account, "employee" by default.
public class signupActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputName;
    EditText inputUsername;
    EditText inputPassword;


    // url to create new employee
    private static String url_create_employee = "http://kerenpeles.net/android_connect/create_employee.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Edit Text
        inputName = (EditText) findViewById(R.id.inputName);
        inputUsername = (EditText) findViewById(R.id.inputUsername);
        inputPassword = (EditText) findViewById(R.id.inputPassword);


        // Create button
        Button sendButton = (Button) findViewById(R.id.sendButton);

        // button click event
        sendButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String name = inputName.getText().toString();
                String username = inputUsername.getText().toString();
                String password = inputPassword.getText().toString();

                new CreateNewEmployee().execute(name, username, password);
                returnToSignIn(view);
            }
        });
    }

    /**
     * Background Async Task to Create new employee
     * */
    class CreateNewEmployee extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(signupActivity.this);
            pDialog.setMessage("Creating Account..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Creating employee
         * */
        protected String doInBackground(String... args) {


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name",args[0]));
            params.add(new BasicNameValuePair("username", args[1]));
            params.add(new BasicNameValuePair("password", args[2]));


            // getting JSON Object
            // Note that create employee url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_employee,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created employee
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(i);


                    // closing this screen
                    finish();
                } else {
                    // failed to create employee
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
        }

    }

    public void returnToSignIn (View v) {


        Toast.makeText(signupActivity.this, "Sign Up Successful !", Toast.LENGTH_LONG).show();

        startActivity(new Intent(getApplication(), MainActivity.class));
    }
}