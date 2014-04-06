package edu.berkeley.cs160.lasercats.SettingsActivityFragments;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceFragment;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import edu.berkeley.cs160.lasercats.R;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ListOfSettingsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ListOfSettingsFragment#newInstance} factory method to
 * create an instance of this fragment.
 *
 */
public class ListOfSettingsFragment extends PreferenceFragment {

    public ListOfSettingsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // here we add the preferences xml
        addPreferencesFromResource(R.xml.preferences);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return super.onCreateView(inflater, container, savedInstanceState);
    }

}
