package edu.berkeley.cs160.lasercats.HistoryByDateFragments;


import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.sql.Date;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.berkeley.cs160.lasercats.Models.Exercise;
import edu.berkeley.cs160.lasercats.Models.ExerciseSet;
import edu.berkeley.cs160.lasercats.R;

public class SetsCalendarFragment extends Fragment{
    private String exerciseName;
    CaldroidFragment calendarView;

    public SetsCalendarFragment() {
    }

    public SetsCalendarFragment(String aExercise) {
        super();
        exerciseName = aExercise;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_calendar, container, false);

        calendarView = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        calendarView.setArguments(args);

        populateCalendarWorkoutDays(calendarView);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.child, calendarView).commit();

        setupListener();

        return rootView;
    }

    private void populateCalendarWorkoutDays(CaldroidFragment calendarView) {
        List<Date> datesOfExercise = ExerciseSet.getUniqueDatesForAllSets();
        HashMap <java.util.Date, Integer> textColorForDateMap = new HashMap <java.util.Date, Integer>();
        for (Iterator<Date> iter = datesOfExercise.iterator(); iter.hasNext(); ) {
            Date currentDate = iter.next();
            textColorForDateMap.put(new java.util.Date(currentDate.getTime()), R.color.fishfood);
        }
        calendarView.setBackgroundResourceForDates(textColorForDateMap);
    }

    public void setupListener() {
        final CaldroidListener listener = new CaldroidListener() {

            @Override
            public void onSelectDate(java.util.Date date, View view) {
                List<ExerciseSet> workingSet = ExerciseSet.getAllSetsByDate(new java.sql.Date(date.getTime()));

                ExerciseAndSetsByDateFragment newFrag = new ExerciseAndSetsByDateFragment(workingSet);
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // Replace whatever is in the fragment_container view with this fragment,
                // and add the transaction to the back stack
                transaction.replace(R.id.content_frame, newFrag);
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        };
        calendarView.setCaldroidListener(listener);
    }
}
