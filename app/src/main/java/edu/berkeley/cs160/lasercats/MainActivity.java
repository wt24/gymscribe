package edu.berkeley.cs160.lasercats;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.berkeley.cs160.lasercats.Models.Exercise;

public class MainActivity extends BaseNavigationDrawerActivity {

    Exercise[] fullListOfExercises;
    Exercise[] currentlyUsedListOfExercises;
    EditText searchBar;
    ArrayAdapter currentAdapter;
    ListView exerciseList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        positionOfActivityInList = 0;
        mainLayoutId = R.layout.activity_main;
        super.onCreate(savedInstanceState);

        android.app.ActionBar ab = getActionBar();
        ab.setTitle("Select a Workout");
        
        exerciseList = (ListView) findViewById(R.id.listView);

        List<Exercise> listOfAllExercises = Exercise.getAll();
        fullListOfExercises = new Exercise[listOfAllExercises.size()];
        listOfAllExercises.toArray(fullListOfExercises);
        currentlyUsedListOfExercises = fullListOfExercises;

        currentAdapter = new ArrayAdapter<Exercise>(MainActivity.this, R.layout.excercise_list_item, fullListOfExercises);
        exerciseList.setAdapter(currentAdapter);

        exerciseList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                intent.putExtra("exercise", currentlyUsedListOfExercises[i].name);
                intent.putExtra("exerciseObj", currentlyUsedListOfExercises[i]);
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
                    currentAdapter = new ArrayAdapter<Exercise>(MainActivity.this, R.layout.excercise_list_item, fullListOfExercises);
                    exerciseList.setAdapter(currentAdapter);
                    return;
                }
                String userInput = s.toString();
                String exercise;

                Pattern regexFromUserInput = Pattern.compile(".*" + userInput + ".*", Pattern.CASE_INSENSITIVE);
                Matcher patternMatch;
                ArrayList<Exercise> newNavigationItems = new ArrayList<Exercise>();
                for (int i = 0; i < fullListOfExercises.length; i++) {
                    exercise = fullListOfExercises[i].name;
                    patternMatch = regexFromUserInput.matcher(exercise);
                    if(patternMatch.matches()) {
                        newNavigationItems.add(fullListOfExercises[i]);
                    }
                }
                currentlyUsedListOfExercises = new Exercise[newNavigationItems.size()];
                newNavigationItems.toArray(currentlyUsedListOfExercises);
                currentAdapter = new ArrayAdapter<Exercise>(MainActivity.this, R.layout.excercise_list_item, newNavigationItems);
                exerciseList.setAdapter(currentAdapter);

            }
        });
    }


}
