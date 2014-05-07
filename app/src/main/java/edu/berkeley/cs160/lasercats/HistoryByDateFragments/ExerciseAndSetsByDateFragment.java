package edu.berkeley.cs160.lasercats.HistoryByDateFragments;

import android.app.DialogFragment;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

import edu.berkeley.cs160.lasercats.Models.Exercise;
import edu.berkeley.cs160.lasercats.Models.ExerciseSet;
import edu.berkeley.cs160.lasercats.R;

/**
 * Created by stpham on 5/7/14.
 */
public class ExerciseAndSetsByDateFragment extends Fragment {
    List<ExerciseSet> workingSets;
    private ListView mListView;
    private ArrayAdapter<String> adapter;

    public ExerciseAndSetsByDateFragment(List<ExerciseSet> argumentSet) {
        workingSets = argumentSet;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_and_sets_by_date_fragment, container, false);

        ArrayList<String> setStrings = new ArrayList<String>();
        for (ExerciseSet set : workingSets) {
            setStrings.add(set.toString());
        }

        mListView = (ListView) rootView.findViewById(R.id.listOfExerciseSets);
        adapter = new ArrayAdapter<String>(getActivity(), R.layout.excercise_list_item, setStrings);
        mListView.setAdapter(adapter);

        return rootView;
    }
}
