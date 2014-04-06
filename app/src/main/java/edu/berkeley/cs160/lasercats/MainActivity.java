package edu.berkeley.cs160.lasercats;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MainActivity extends BaseNavigationDrawerActivity {

    String[] exercises;
    EditText searchBar;
    ArrayAdapter currentAdapter;
    ListView exerciseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        positionOfActivityInList = 0;
        mainLayoutId = R.layout.activity_main;
        super.onCreate(savedInstanceState);

        android.app.ActionBar ab = getActionBar();
        ab.setTitle("Choose a Workout");
        
        exerciseList = (ListView) findViewById(R.id.listView);

        exercises = getResources().getStringArray(R.array.exercise_items_array);
        currentAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.excercise_list_item, exercises);
        exerciseList.setAdapter(currentAdapter);

        exerciseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                intent.putExtra("exercise", exercises[i]);
                startActivity(intent);
            }
        });

        setupSearchBar();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void setupSearchBar() {
        searchBar = (EditText) findViewById(R.id.editText);

        searchBar.setHint("Search for an Exercise");
        searchBar.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null || s.toString().equals("")) {
                    currentAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.excercise_list_item, exercises);
                    exerciseList.setAdapter(currentAdapter);
                    return;
                }
                String userInput = s.toString();
                String exercise;

                Pattern regexFromUserInput = Pattern.compile(".*" + userInput + ".*", Pattern.CASE_INSENSITIVE);
                Matcher patternMatch;
                ArrayList<String> newNavigationItems = new ArrayList<String>();
                for (int i = 0; i < exercises.length; i++) {
                    exercise = exercises[i];
                    patternMatch = regexFromUserInput.matcher(exercise);
                    if(patternMatch.matches()) {
                        newNavigationItems.add(exercise);
                    }
                }
                currentAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.excercise_list_item, newNavigationItems);
                exerciseList.setAdapter(currentAdapter);

            }
        });
    }


}
