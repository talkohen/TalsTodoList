
package com.tal.tal.todolist21;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;


// an activity for viewing all of a single task attributes.
public class managerViewTaskDetailsActivity extends Activity {

    EditText txtTitle;
    Spinner txtCategory;
    Spinner txtPriority;
    EditText txtLocation;
    EditText txtDate;
    EditText txtMember;
    Spinner txtAcceptance;
    Spinner txtProgress;
    ImageView Image;
    Button btnSave;
    Button btnDelete;
    String pid;
    JSONObject globalJson;
    int editButtonflag=0;


    // Progress Dialog
    private ProgressDialog pDialog;

    // JSON parser class
    JSONParser jsonParser = new JSONParser();

    // single task url
    private static final String url_task_detials = "http://kerenpeles.net/android_connect/get_task_details.php";

    // url to update task
    private static final String url_update_task = "http://kerenpeles.net/android_connect/update_task.php";

    // url to delete task
    private static final String url_delete_task = "http://kerenpeles.net/android_connect/delete_task.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TASK = "product";
    private static final String TAG_PID = "id";


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manager_view_task_details);

        // save button
        btnSave = (Button) findViewById(R.id.editButton);
        btnDelete = (Button) findViewById(R.id.deleteButton);

        // getting task details from intent
        Intent i = getIntent();

        // getting task id (pid) from intent
        pid = i.getStringExtra(TAG_PID);
        Log.d("ongetstringextra: ", pid);

        // Getting complete task details in background thread
        new GetTaskDetails().execute();





        // Delete button click event
        btnDelete.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                // deleting task in background thread
                new DeleteTask().execute();
            }
        });

    }

    public  void editButtonClicked (View v){

                ((Button) v).setText("Edit");
                editButtonflag=0;

                // getting updated data from EditTexts
                txtTitle = (EditText) findViewById(R.id.inputTitle);
                txtCategory = (Spinner) findViewById(R.id.inputCategory);
                txtPriority = (Spinner) findViewById(R.id.inputPriority);
                txtLocation = (EditText) findViewById(R.id.inputLocation);
                txtDate = (EditText) findViewById(R.id.inputDate);
                txtMember = (EditText) findViewById(R.id.inputMember);
                txtAcceptance = (Spinner) findViewById(R.id.inputAcceptance);
                txtProgress = (Spinner) findViewById(R.id.inputProgress);
                Image = (ImageView) findViewById(R.id.mImageView);


        String title = txtTitle.getText().toString();
                String category = Integer.toString(txtCategory.getSelectedItemPosition());
        String priority = Integer.toString(txtPriority.getSelectedItemPosition());
                String location = txtLocation.getText().toString();
                String date = txtDate.getText().toString();
        String member = txtMember.getText().toString();
                String acceptanceStatus = Integer.toString (txtAcceptance.getSelectedItemPosition());
                String progressStatus = Integer.toString (txtProgress.getSelectedItemPosition());


                new SaveTaskDetails().execute(title,category,priority,location,date,
                        member,acceptanceStatus,progressStatus);


    }
    private void setValues(JSONObject json ) throws JSONException {



        txtTitle = (EditText) findViewById(R.id.inputTitle);
        txtCategory = (Spinner) findViewById(R.id.inputCategory);
        txtPriority = (Spinner) findViewById(R.id.inputPriority);
        txtLocation = (EditText) findViewById(R.id.inputLocation);
        txtDate = (EditText) findViewById(R.id.inputDate);
        txtMember = (EditText) findViewById(R.id.inputMember);
        txtAcceptance = (Spinner) findViewById(R.id.inputAcceptance);
        txtProgress = (Spinner) findViewById(R.id.inputProgress);
        Image = (ImageView) findViewById(R.id.mImageView);


        //display task data in EditText
        txtTitle.setText(json.getString("title"));
        txtCategory.setSelection(json.getInt("category"));
        txtPriority.setSelection(json.getInt("priority"));
        txtLocation.setText(json.getString("location"));
        txtDate.setText(json.getString("date"));
        txtMember.setText(json.getString("member"));
        txtAcceptance.setSelection(json.getInt("acceptanceStatus"));
        txtProgress.setSelection(json.getInt("progressStatus"));

        txtAcceptance.setEnabled(false);
        txtProgress.setEnabled(false);


    }
    /**
     * Background Async Task to Get complete task details
     * */
    class GetTaskDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            Log.d("onPreExecute: ", pid);
            super.onPreExecute();
            pDialog = new ProgressDialog(managerViewTaskDetailsActivity.this);
            pDialog.setMessage("Loading task details. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();

            Log.d("onPreExecute2: ", pid);
        }

        /**
         * Getting task details in background thread
         * */
        protected String doInBackground(String... params) {

            // updating UI from Background Thread


            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params1 = new ArrayList<NameValuePair>();
                params1.add(new BasicNameValuePair("id", pid));
                Log.d("Building Parameters: ", pid);

                // getting task details by making HTTP request
                // Note that task details url will use GET request
                JSONObject json = jsonParser.makeHttpRequest(url_task_detials, "GET_DETAILS", params1);
                Log.d("GETParameters: ", pid);

                // check your log for json response
                Log.d("Single task Details", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);

                // successfully received task details
                JSONArray taskObj = json
                        .getJSONArray(TAG_TASK); // JSON Array

                // get first task object from JSON Array

                try {globalJson = taskObj.getJSONObject(0);}
                catch (JSONException e) {
                    e.printStackTrace();
                }
                // task with this pid found
                // Edit Text


            }
            catch (JSONException e) {

                e.printStackTrace();
            }


            return null;
        }



        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url ) {
            // dismiss the dialog once got all details
            Log.d( "onPostExecute: ",globalJson.toString());
            pDialog.dismiss();
            try {
                setValues(globalJson);
            }
            catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }

    /**
     * Background Async Task to  Save task Details
     * */
    class SaveTaskDetails extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(managerViewTaskDetailsActivity.this);
            pDialog.setMessage("Saving task ...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Saving task
         * */
        protected String doInBackground(String... args) {



            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", pid));
            params.add(new BasicNameValuePair("title", args[0]));
            params.add(new BasicNameValuePair("category", args[1]));
            params.add(new BasicNameValuePair("priority", args[2]));
            params.add(new BasicNameValuePair("location", args[3]));
            params.add(new BasicNameValuePair("date", args[4]));
            params.add(new BasicNameValuePair("member", args[5]));
            params.add(new BasicNameValuePair("acceptanceStatus", args[6]));
            params.add(new BasicNameValuePair("progressStatus", args[7]));


            // sending modified data through http request
            // Notice that update task url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_update_task,
                    "POST", params);

            // check json success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully updated
                    Intent i = getIntent();
                    // send result code 100 to notify about task update
                    setResult(100, i);
                    finish();
                } else {
                    // failed to update task
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
            // dismiss the dialog once task updated
            pDialog.dismiss();
        }
    }

    /*****************************************************************
     * Background Async Task to Delete task
     * */
    class DeleteTask extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(managerViewTaskDetailsActivity.this);
            pDialog.setMessage("Deleting task...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        /**
         * Deleting task
         * */
        protected String doInBackground(String... args) {

            // Check for success tag
            int success;
            try {
                // Building Parameters
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("id", pid));

                // getting task details by making HTTP request
                JSONObject json = jsonParser.makeHttpRequest(
                        url_delete_task, "POST", params);

                // check your log for json response
                Log.d("Delete task", json.toString());

                // json success tag
                success = json.getInt(TAG_SUCCESS);
                if (success == 1) {
                    // task successfully deleted
                    // notify previous activity by sending code 100
                    Intent i = getIntent();
                    // send result code 100 to notify about task deletion
                    setResult(100, i);
                    finish();
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
            // dismiss the dialog once task deleted
            pDialog.dismiss();

        }

    }
}