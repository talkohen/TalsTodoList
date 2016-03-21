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
import android.widget.Spinner;


//and activity for adding a new task to the database by the manager
public class createTaskActivity extends Activity {

    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText inputTitle;
    Spinner inputCategory;
    Spinner inputPriority;
    EditText inputLocation;
    EditText inputDate;
    EditText inputMember;

    // url to php file containing the insert query
    private static String url_create_task = "http://kerenpeles.net/android_connect/create_task.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

        // input fields
        inputTitle = (EditText) findViewById(R.id.inputTitle);
        inputCategory = (Spinner) findViewById(R.id.inputCategory);
        inputPriority = (Spinner) findViewById(R.id.inputPriority);
        inputLocation = (EditText) findViewById(R.id.inputLocation);
        inputDate = (EditText) findViewById(R.id.inputDate);
        inputMember = (EditText) findViewById(R.id.inputMember);

        // Create button
        Button createButton = (Button) findViewById(R.id.createButton);

        // button click event
        createButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                String title = inputTitle.getText().toString();
                String category = Integer.toString(inputCategory.getSelectedItemPosition());
                String priority = Integer.toString(inputPriority.getSelectedItemPosition());
                String location = inputLocation.getText().toString();
                String date = inputDate.getText().toString();
                String member = inputMember.getText().toString();
                new CreateNewTask().execute(title, category, priority, location, date, member);
                returnToWaitingTaskList (view);
            }
        });
    }

    /**
     * Background Async Task to Create new task
     * */
    class CreateNewTask extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(createTaskActivity.this);
            pDialog.setMessage("Creating Task..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

        }

        /**
         * Creating task
         * */
        protected String doInBackground(String... args) {


            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("title",args[0]));
            params.add(new BasicNameValuePair("category", args[1]));
            params.add(new BasicNameValuePair("priority", args[2]));
            params.add(new BasicNameValuePair("location", args[3]));
            params.add(new BasicNameValuePair("date", args[4]));
            params.add(new BasicNameValuePair("member", args[5]));

            // getting JSON Object
            // Note that create task url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_task,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created task
                    Intent i = new Intent(getApplicationContext(), employeeWaitingTasksActivity.class);
                    startActivity(i);

                    // closing this screen
                    finish();
                } else {
                    // failed to create task
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

    public void returnToWaitingTaskList (View v) {

        Button btnCreateTask = (Button) v;
        startActivity(new Intent(getApplication(), employeeWaitingTasksActivity.class));
    }
}