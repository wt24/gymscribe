package edu.berkeley.cs160.lasercats;

import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.app.AlertDialog;

import com.activeandroid.query.Delete;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import edu.berkeley.cs160.lasercats.Models.Exercise;
import edu.berkeley.cs160.lasercats.Models.ExerciseSet;


public class RecordActivity extends BaseNavigationDrawerActivity {

    private boolean isRecording = false;
    private Button mEditButton;
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<ExerciseSet> sets;
    private ArrayList<String> setStrings;
    private int exerciseID;
    private Exercise exercise;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainLayoutId = R.layout.activity_record;
        super.onCreate(savedInstanceState);

        exerciseID = Integer.valueOf(getIntent().getExtras().get("exercise").toString());
        exercise = Exercise.getExercise(exerciseID).get(0);

        android.app.ActionBar ab = getActionBar();
        ab.setTitle(exercise.name);

        loadSets();
    }

    /**
     * Function: Load sets from database for current exercise
     */
    protected void loadSets() {
        sets = new ArrayList<ExerciseSet>(ExerciseSet.getAllForExercise(exercise));
        System.out.println(">>>>>>>>>>>>>>> SETS: " + sets);

        setStrings = new ArrayList<String>();
        for (ExerciseSet set : sets) {
            setStrings.add(set.toString());
        }

        mListView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, R.layout.row, setStrings);
        mListView.setAdapter(adapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                removeItemFromList(i);
                return true;
            }
        });
    }

    /**
     * Function: add new set to database
     * @param reps
     * @param weight
     */
    protected void addSet(int reps, float weight) {
        ExerciseSet set = new ExerciseSet(reps, weight);
        set.exercise = exercise;
        set.dateOfSet = new java.sql.Date(new Date().getTime());
        set.save();
        loadSets();
    }

    /**
     * Function: delete set from database
     * @param setID
     */
    protected void deleteSet(int setID) {
        new Delete()
                .from(ExerciseSet.class)
                .where("Id = ?", setID)
                .execute();
    }

    /**
     * Function: delete selected log from list & database
     * @param i
     */
    protected void removeItemFromList(int i) {
        final int deletePosition = i;
        AlertDialog.Builder alert = new AlertDialog.Builder(
                RecordActivity.this);
        alert.setTitle("Delete");
        alert.setMessage("Do you want delete this item?");

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                // main code on after clicking yes
                ExerciseSet e = sets.get(deletePosition);
                deleteSet(Integer.valueOf(e.getId().toString()));
                loadSets();

            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        alert.show();

    }

        //Start Editing Button
    /*
        mEditButton = (Button) findViewById(R.id.editButton);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do shit
            }
        });
    */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.record, menu);
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

    public void recordButtonPressed(View view) {
        Button b = (Button) findViewById(R.id.recordButton);
        isRecording = !isRecording;

        if (isRecording) {
            b.setText("Stop");
            b.setBackground(getResources().getDrawable(R.drawable.cancel_button_style));
            //DO STUFF HERE
            addSet(1000,4000);
            loadSets();
        }
        else {
            b.setText("Record");
            b.setBackground(getResources().getDrawable(R.drawable.record_button_style));
        }
    }

}
