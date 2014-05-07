package edu.berkeley.cs160.lasercats.PastActivityFragments;


import android.app.Fragment;
import android.os.Bundle;
import android.app.FragmentTransaction;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidListener;

import java.sql.Date;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.berkeley.cs160.lasercats.Models.Exercise;
import edu.berkeley.cs160.lasercats.Models.ExerciseSet;
import edu.berkeley.cs160.lasercats.R;

public class ExerciseCalendarFragment extends Fragment{
    private String exerciseName;

    public ExerciseCalendarFragment() {
    }

    public ExerciseCalendarFragment(String aExercise) {
        super();
        exerciseName = aExercise;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_calendar, container, false);

        CaldroidFragment calendarView = new CaldroidFragment();
        Bundle args = new Bundle();
        Calendar cal = Calendar.getInstance();
        args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
        args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
        calendarView.setArguments(args);

        populateCalendarWorkoutDays(calendarView);
        FragmentTransaction transaction = getChildFragmentManager().beginTransaction();
        transaction.add(R.id.child, calendarView).commit();

        final CaldroidListener listener = new CaldroidListener() {
            @Override
            public void onSelectDate(java.util.Date date, View view) {
                dateSelected(date);
            }
        };
        calendarView.setCaldroidListener(listener);

        return rootView;
    }

    private void populateCalendarWorkoutDays(CaldroidFragment calendarView) {
        List<Date> datesOfExercise = ExerciseSet.getUniqueDates(Exercise.getExerciseByName(exerciseName).get(0));
        HashMap <java.util.Date, Integer> textColorForDateMap = new HashMap <java.util.Date, Integer>();
        for (Iterator<Date> iter = datesOfExercise.iterator(); iter.hasNext(); ) {
            Date currentDate = iter.next();
            textColorForDateMap.put(new java.util.Date(currentDate.getTime()), R.color.fishfood);
        }
        calendarView.setBackgroundResourceForDates(textColorForDateMap);
    }

    private void dateSelected(java.util.Date date) {
        Exercise e = Exercise.getExerciseByName(exerciseName).get(0);
        java.sql.Date d = new Date(date.getTime());
        List<ExerciseSet> sets = ExerciseSet.getAllByExerciseAndDate(e, d);
        System.out.println("DATE from Calendar: " + date + ", " + date.getTime());

        System.out.println(sets);
    }
}
