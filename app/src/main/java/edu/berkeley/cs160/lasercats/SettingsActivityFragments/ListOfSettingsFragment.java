package edu.berkeley.cs160.lasercats.SettingsActivityFragments;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.Preference;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.sql.Date;
import java.util.Random;

import edu.berkeley.cs160.lasercats.Models.Exercise;
import edu.berkeley.cs160.lasercats.Models.ExerciseSet;
import edu.berkeley.cs160.lasercats.R;

/**

 */
public class ListOfSettingsFragment extends PreferenceFragment implements Preference.OnPreferenceClickListener {
    private Preference sampleDataGeneratePref;
    private Preference eraseSetsTablePref;

    public ListOfSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // here we add the preferences xml
        addPreferencesFromResource(R.xml.preferences);

        sampleDataGeneratePref = findPreference("pref_gen_sample_data");
        sampleDataGeneratePref.setOnPreferenceClickListener(this);
        eraseSetsTablePref = findPreference("pref_clear_sets_db");
        eraseSetsTablePref.setOnPreferenceClickListener(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public boolean onPreferenceClick(Preference preference) {
        String key = preference.getKey();
        // do what ever you want with this key
        if (key.equals("pref_gen_sample_data")) {
            generateSampleData();
        } else if (key.equals("pref_clear_sets_db")) {
            AlertDialog.Builder alertDialog = new AlertDialog.Builder(getActivity());
            alertDialog.setMessage("Erase Sets?");
            alertDialog.setCancelable(true);
            alertDialog.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    ExerciseSet.eraseTable();
                }
            });
            alertDialog.setNegativeButton("No", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int which) {
                    dialog.cancel();
                }
            });
            alertDialog.show();
            return false;
        }
        return true;
    }

    public void generateSampleData() {
        int[] listOfExerciseId = {1, 2, 3, 4, 5};
        long[] dateOrigins = {1398038400000l, 1397779200000l, 1397865600000l}; // 4/21/2014 00:00:00, 4/18/2014 00:00:00, 4/19/2014 00:00:00
        long[] daysOffset = {1, 3, 7, 17, 20, 40, 50};
        float[] weights = {5, 10, 15, 20, 25, 30, 35, 40, 45, 50, 55, 60};
        int[] reps = {6, 7, 10, 10};

        Random randomGen = new Random();
        for (int i = 0; i < listOfExerciseId.length; i++) {
            Exercise exercise = Exercise.getExerciseById(listOfExerciseId[i]).get(0);
            long dateOrigin = dateOrigins[randomGen.nextInt(dateOrigins.length)];
            for (int offset = 0; offset < daysOffset.length; offset++) {
                Date currentDate = new Date(dateOrigin - (daysOffset[offset] * 24l * 3600000l));
                for (int repIdx = 0; repIdx < reps.length; repIdx++) {
                    float currentWeight = weights[randomGen.nextInt(weights.length)];
                    ExerciseSet temp = new ExerciseSet(reps[repIdx], currentWeight);
                    temp.exercise = exercise;
                    temp.dateOfSet = currentDate;
                    temp.save();
                }

            }

        }
    }

}
