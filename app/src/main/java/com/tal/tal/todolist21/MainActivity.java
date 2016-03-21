package com.tal.tal.todolist21;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

// an activity for logging in to the app - as a manager or as an employee. sign up if none.
public class MainActivity extends ActionBarActivity {

    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    EditText inputUser;
    EditText inputPassword;
    private ListView mDrawerList;
    private DrawerLayout mDrawerLayout;
    private ArrayAdapter<String> mAdapter;
    private ActionBarDrawerToggle mDrawerToggle;
    private String mActivityTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //create drawer menu
        mDrawerList = (ListView)findViewById(R.id.navList);mDrawerLayout = (DrawerLayout)findViewById(R.id.drawer_layout);
        mActivityTitle = getTitle().toString();

        addDrawerItems();
        setupDrawer();

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);



    }
    //get values from input.
    public void loginButtonClicked (View v) {

        Button loginButton = (Button) v;

        inputUser = (EditText) findViewById(R.id.inputUser);
        inputPassword = (EditText) findViewById(R.id.inputPassword);

        String username = inputUser.getText().toString();
        String password = inputPassword.getText().toString();


        Log.d(username, "loginButtonClicked: ");
        Log.d(password, "loginButtonClicked: ");

        // send to "waiting task list acticity"
try {
    Intent myIntent = new Intent(this, employeeWaitingTasksActivity.class);
    myIntent.putExtra("username", username);
    myIntent.putExtra("password", password);
    startActivity(myIntent);
    finish();

}
catch (Exception e) {

}
    }

    //login for manager activity
    public void managerLoginButtonClicked (View v) {

        Button loginButton = (Button) v;

        inputUser = (EditText) findViewById(R.id.inputUser);
        inputPassword = (EditText) findViewById(R.id.inputPassword);

        String username = inputUser.getText().toString();
        String password = inputPassword.getText().toString();



        try {
            Intent myIntent = new Intent(this, managerWaitingTasksActivity.class);
            myIntent.putExtra("username", username);
            myIntent.putExtra("password", password);
            startActivity(myIntent);


        }
        catch (Exception e) {

        }
    }
    //sign up
    public void signupButtonClicked (View v) {

        Button btnCreateProduct = (Button) v;

        startActivity(new Intent(getApplication(), signupActivity.class));
    }
    //Set up drawer
    private void addDrawerItems() {
        String[] osArray = { "About", "Invite" };
        mAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, osArray);
        mDrawerList.setAdapter(mAdapter);

        mDrawerList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                if (position == 0) {
                    startActivity(new Intent(getApplication(), aboutActivity.class));
                }

                if (position == 1) {
                    startActivity(new Intent(getApplication(), inviteTeamMembersActivity.class));
                }
            }
        });
    }

    private void setupDrawer() {
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, R.string.drawer_open, R.string.drawer_close) {

            /** Called when a drawer has settled in a completely open state. */
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
                getSupportActionBar().setTitle("What now?");
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }

            /** Called when a drawer has settled in a completely closed state. */
            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                getSupportActionBar().setTitle(mActivityTitle);
                invalidateOptionsMenu(); // creates call to onPrepareOptionsMenu()
            }
        };

        mDrawerToggle.setDrawerIndicatorEnabled(true);
        mDrawerLayout.setDrawerListener(mDrawerToggle);
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        mDrawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        mDrawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
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

        // Activate the navigation drawer toggle
        if (mDrawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
