package edu.berkeley.cs160.lasercats;

import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.roomorama.caldroid.CaldroidListener;

import java.util.Date;
import java.util.List;

import edu.berkeley.cs160.lasercats.HistoryByDateFragments.ExerciseAndSetsByDateFragment;
import edu.berkeley.cs160.lasercats.HistoryByDateFragments.SetsCalendarFragment;
import edu.berkeley.cs160.lasercats.Models.ExerciseSet;


public class ViewOverallActivity extends BaseNavigationDrawerActivity {
    SetsCalendarFragment currentCalendarFrag;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        positionOfActivityInList = 2;
        mainLayoutId = R.layout.activity_history_by_date;
        super.onCreate(savedInstanceState);


        // Create new fragment and transaction
        currentCalendarFrag = new SetsCalendarFragment();
        FragmentTransaction transaction = getFragmentManager().beginTransaction();

        // Replace whatever is in the fragment_container view with this fragment,
        // and add the transaction to the back stack
        transaction.replace(R.id.content_frame, currentCalendarFrag);
        transaction.addToBackStack(null);

        // Commit the transaction
        transaction.commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.help, menu);
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

}
