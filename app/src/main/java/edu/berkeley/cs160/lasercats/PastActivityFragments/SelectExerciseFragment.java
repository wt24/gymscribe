package edu.berkeley.cs160.lasercats.PastActivityFragments;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.berkeley.cs160.lasercats.Models.Exercise;
import edu.berkeley.cs160.lasercats.R;

public class SelectExerciseFragment extends Fragment {
    private DrawerLayout mDrawerLayout;
    private ListView mExerciseList;
    private Exercise[] mNavigationItems;
    private Exercise[] currentlyUsedList;
    private ExerciseCallbacks selectCallback;
    private ArrayAdapter<Exercise> currentAdapter;
    private EditText searchBar;

    public SelectExerciseFragment(ExerciseCallbacks callBackClass) {
        selectCallback = callBackClass;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_exercise_inner, container, false);

        List<Exercise> listOfAllExercises = Exercise.getAll();
        mNavigationItems = new Exercise[listOfAllExercises.size()];
        listOfAllExercises.toArray(mNavigationItems);
        currentlyUsedList = mNavigationItems;
        //mNavigationItems = getResources().getStringArray(R.array.exercise_items_array);
        mExerciseList = (ListView) rootView.findViewById(R.id.listOfExercises);
        currentAdapter = new ArrayAdapter<Exercise>(getActivity(), R.layout.excercise_list_item, mNavigationItems);
        currentAdapter.sort(new Comparator<Exercise>() {
            @Override
            public int compare(Exercise lhs, Exercise rhs) {
                return lhs.name.compareTo(rhs.name);   //or whatever your sorting algorithm
            }
        });

        mExerciseList.setAdapter(currentAdapter);

        mExerciseList.setOnItemClickListener(new DrawerItemClickListener());

        setupSearchBar(rootView);
        return rootView;
    }

    private class DrawerItemClickListener implements ListView.OnItemClickListener {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            //Open Position
            InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(getActivity().INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(searchBar.getWindowToken(), 0);
            selectCallback.switchToExerciseHistory(position, currentlyUsedList[position].name);
        }
    }

    private void setupSearchBar(View rootView) {
        searchBar = (EditText) rootView.findViewById(R.id.searchExercisesInputBox);

        searchBar.setHint("Search for an Exercise");

        searchBar.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null || s.toString().equals("")) {
                    currentAdapter = new ArrayAdapter<Exercise>(getActivity(), R.layout.excercise_list_item, mNavigationItems);
                    mExerciseList.setAdapter(currentAdapter);
                    return;
                }
                String userInput = s.toString();
                String exercise;

                Pattern regexFromUserInput = Pattern.compile(".*" + userInput + ".*", Pattern.CASE_INSENSITIVE);
                Matcher patternMatch;
                ArrayList<Exercise> newNavigationItems = new ArrayList<Exercise>();
                for (int i = 0; i < mNavigationItems.length; i++) {
                    exercise = mNavigationItems[i].name;
                    patternMatch = regexFromUserInput.matcher(exercise);
                    if(patternMatch.matches()) {
                        newNavigationItems.add(mNavigationItems[i]);
                    }
                }
                currentlyUsedList = new Exercise[newNavigationItems.size()];
                newNavigationItems.toArray(currentlyUsedList);
                currentAdapter = new ArrayAdapter<Exercise>(getActivity(), R.layout.excercise_list_item, newNavigationItems);
                mExerciseList.setAdapter(currentAdapter);

            }
        });
    }
}
