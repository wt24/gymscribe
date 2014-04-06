package edu.berkeley.cs160.lasercats.PastActivityFragments;


import android.app.Fragment;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.text.format.Time;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

import edu.berkeley.cs160.lasercats.PastActivityFragments.extendedcalendarview.CalendarProvider;
import edu.berkeley.cs160.lasercats.PastActivityFragments.extendedcalendarview.Event;
import edu.berkeley.cs160.lasercats.PastActivityFragments.extendedcalendarview.ExtendedCalendarView;
import edu.berkeley.cs160.lasercats.R;

public class ExerciseCalendarFragment extends Fragment {
    private String exerciseName;

    public ExerciseCalendarFragment(String aExercise) {
        super();
        exerciseName = aExercise;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_calendar, container, false);
        ExtendedCalendarView calendar = (ExtendedCalendarView) rootView.findViewById(R.id.calendar);
        return rootView;
    }
}
