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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.berkeley.cs160.lasercats.R;

public class SelectExerciseFragment extends Fragment {
    private DrawerLayout mDrawerLayout;
    private ListView mExerciseList;
    private String[] mNavigationItems;
    private ExerciseCallbacks selectCallback;
    private ArrayAdapter<String> currentAdapter;
    private EditText searchBar;

    public SelectExerciseFragment(ExerciseCallbacks callBackClass) {
        selectCallback = callBackClass;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_select_exercise_inner, container, false);


        mNavigationItems = getResources().getStringArray(R.array.exercise_items_array);
        mExerciseList = (ListView) rootView.findViewById(R.id.listOfExercises);
        currentAdapter = new ArrayAdapter<String>(getActivity(), R.layout.excercise_list_item, mNavigationItems);
        currentAdapter.sort(new Comparator<String>() {
            @Override
            public int compare(String lhs, String rhs) {
                return lhs.compareTo(rhs);   //or whatever your sorting algorithm
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
            selectCallback.switchToExerciseHistory(position, mNavigationItems[position]);
        }
    }

    private void setupSearchBar(View rootView) {
        searchBar = (EditText) rootView.findViewById(R.id.searchExercisesInputBox);

        searchBar.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) { }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s == null || s.toString().equals("")) {
                    currentAdapter = new ArrayAdapter<String>(getActivity(), R.layout.excercise_list_item, mNavigationItems);
                    mExerciseList.setAdapter(currentAdapter);
                    return;
                }
                String userInput = s.toString();
                String exercise;

                Pattern regexFromUserInput = Pattern.compile(".*" + userInput + ".*", Pattern.CASE_INSENSITIVE);
                Matcher patternMatch;
                ArrayList<String> newNavigationItems = new ArrayList<String>();
                for (int i = 0; i < mNavigationItems.length; i++) {
                    exercise = mNavigationItems[i];
                    patternMatch = regexFromUserInput.matcher(exercise);
                    if(patternMatch.matches()) {
                        newNavigationItems.add(exercise);
                    }
                }
                currentAdapter = new ArrayAdapter<String>(getActivity(), R.layout.excercise_list_item, newNavigationItems);
                mExerciseList.setAdapter(currentAdapter);

            }
        });
    }
}
