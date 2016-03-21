package com.tal.tal.todolist21;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.app.ListActivity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;



// an activity for viewing all of the current user's tasks
public class employeeAllTasksActivity extends ListActivity {


    // Progress Dialog
    private ProgressDialog pDialog;

    // Creating JSON Parser object
    JSONParser jParser = new JSONParser();

    ArrayList<HashMap<String, String>> TasksList;

    // url to get all Tasks list
    private static String url_all_Tasks = "http://kerenpeles.net/android_connect/get_all_employee_tasks.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_TASKS = "tasks";
    private static final String TAG_PID = "id";
    private static final String TAG_NAME = "title";

    public String username;
    public String password;

    // Tasks JSONArray
    JSONArray Tasks = null;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_all_tasks);

        Intent myIntent = getIntent(); // gets the previously created intent
        username = myIntent.getStringExtra("username"); // will return userName
        password= myIntent.getStringExtra("password"); // will return password

        Log.d(username, "onCreate: ");

        // Hashmap for ListView
        TasksList = new ArrayList<HashMap<String, String>>();

        // Loading Tasks in Background Thread
        new LoadAllTasks().execute(username,password);

        // Get listview
        ListView lv = getListView();

        // on selecting single task
        // launching task details Screen
        lv.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // getting values from selected ListItem
                String pid = ((TextView) view.findViewById(R.id.pid)).getText()
                        .toString();

                Log.d("onItemClick: ", pid);
                // Starting new intent
                Intent in = new Intent(getApplicationContext(),
                        viewTaskDetailsActivity.class);
                // sending id to next activity
                in.putExtra(TAG_PID, pid);

                // starting new activity and expecting some response back
                startActivityForResult(in, 100);
            }
        });

    }


    /**
     * Background Async Task to Load all tasks by making HTTP Request
     * */
    class LoadAllTasks extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(employeeAllTasksActivity.this);
            pDialog.setMessage("Loading Tasks. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        /**
         * getting All Tasks from url
         * */
        protected String doInBackground(String... args) {
            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            try {

                params.add(new BasicNameValuePair("username", args[0]));
                params.add(new BasicNameValuePair("password", args[1]));



                // getting JSON string from URL
                JSONObject json = jParser.makeHttpRequest(url_all_Tasks, "POST", params);

                // Check your log cat for JSON reponse
                Log.d("All Tasks: ", json.toString());

                try {
                    // Checking for SUCCESS TAG
                    int success = json.getInt(TAG_SUCCESS);

                    if (success == 1) {
                        // Tasks found
                        // Getting Array of Tasks
                        Tasks = json.getJSONArray(TAG_TASKS);

                        // looping through All Tasks
                        for (int i = 0; i < Tasks.length(); i++) {
                            JSONObject c = Tasks.getJSONObject(i);

                            // Storing each json item in variable
                            String id = c.getString(TAG_PID);
                            String name = c.getString(TAG_NAME);

                            // creating new HashMap
                            HashMap<String, String> map = new HashMap<String, String>();

                            // adding each child node to HashMap key => value
                            map.put(TAG_PID, id);
                            map.put(TAG_NAME, name);

                            // adding HashList to ArrayList
                            TasksList.add(map);
                        }
                    } else {
                        //Error Toast
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
            catch (Exception  e) {
                finish();
            }
            return null;
        }

        /**
         * After completing background task Dismiss the progress dialog
         * **/
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after getting all Tasks
            pDialog.dismiss();
            // updating UI from Background Thread
            runOnUiThread(new Runnable() {
                public void run() {
                    /**
                     * Updating parsed JSON data into ListView
                     * */
                    ListAdapter adapter = new SimpleAdapter(
                            employeeAllTasksActivity.this, TasksList,
                            R.layout.list_item, new String[] { TAG_PID,
                            TAG_NAME},
                            new int[] { R.id.pid, R.id.name });
                    // updating listview
                    setListAdapter(adapter);
                }
            });

        }

    }


    //refresh current page
    public void refreshButtonClicked (View v) {

        Button refreshButton = (Button) v;
        ((Button) v).setText("Refreshing...");

        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    //Open about activity
    public  void aboutButtonClicked (View v) {

        startActivity(new Intent(getApplication(), aboutActivity.class));
    }

    //Open waiting tasks activity
    public void waitingTasksButtonClicked (View v) {


        Intent myIntent = new Intent(this, employeeWaitingTasksActivity.class);
        myIntent.putExtra("username", username);
        myIntent.putExtra("password", password);

        startActivity(myIntent);
        finish();
    }
}