package edu.berkeley.cs160.lasercats;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.os.Build;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.SimpleAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends BaseNavigationDrawerActivity {

    List<Map<String,String>> exerciseList = new ArrayList<Map<String, String>>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        positionOfActivityInList = 0;
        mainLayoutId = R.layout.activity_main;
        super.onCreate(savedInstanceState);
        //startActivity(new Intent(this, RecordActivity.class));

        initList();
        ListView listView = (ListView) findViewById(R.id.listView);

        SimpleAdapter simpleAdapter = new SimpleAdapter(this, exerciseList, R.layout.exercise_list_cell, new String[] {"exercise"}, new int[] {R.id.cellTextView});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(getApplicationContext(), RecordActivity.class);
                intent.putExtra("exercise", exerciseList.get(i).get("exercise"));
                startActivity(intent);
            }
        });
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

    /**
     * ListView Methods
     */

    private void initList() {
        exerciseList.add(createExercise("exercise", "Bench Press"));
        exerciseList.add(createExercise("exercise", "Pushups"));
        exerciseList.add(createExercise("exercise", "Pullups"));
        exerciseList.add(createExercise("exercise", "Chest Press"));
    }

    private HashMap<String, String> createExercise(String key, String value) {
        HashMap<String, String> map = new HashMap<String, String>();
        map.put(key, value);
        return map;
    }

}
