package edu.berkeley.cs160.lasercats;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import java.sql.Date;

import edu.berkeley.cs160.lasercats.Models.Exercise;
import edu.berkeley.cs160.lasercats.Models.ExerciseSet;
import edu.berkeley.cs160.lasercats.PastActivityFragments.ExerciseCalendarFragment;
import edu.berkeley.cs160.lasercats.PastActivityFragments.ExerciseCallbacks;
import edu.berkeley.cs160.lasercats.PastActivityFragments.ExerciseHistoryFragment;
import edu.berkeley.cs160.lasercats.PastActivityFragments.SelectExerciseFragment;

public class HistoryActivity extends BaseNavigationDrawerActivity implements ExerciseCallbacks {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        positionOfActivityInList = 1;
        mainLayoutId = R.layout.activity_history;
        super.onCreate(savedInstanceState);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        switchToSelectExercise();
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

    public void switchToExerciseHistory(int pos, String exerciseName) {
        // Create new fragment and transaction
        Fragment newFragment = new ExerciseHistoryFragment(exerciseName, this);
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.content_frame, newFragment);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();

    }

    public void switchToSelectExercise() {
        Fragment fragment = new SelectExerciseFragment(this);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

    public void switchToCalendarFor(String exercise) {
        ExerciseCalendarFragment fragment = new ExerciseCalendarFragment(exercise);
        FragmentManager fragmentManager = getFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.content_frame, fragment).commit();
    }

}
