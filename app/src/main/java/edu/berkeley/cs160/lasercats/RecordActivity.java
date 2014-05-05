package edu.berkeley.cs160.lasercats;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.CountDownTimer;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.app.AlertDialog;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.util.Log;
import android.speech.tts.TextToSpeech;
import com.activeandroid.query.Delete;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.berkeley.cs160.lasercats.Models.Exercise;
import edu.berkeley.cs160.lasercats.Models.ExerciseSet;


public class RecordActivity extends BaseNavigationDrawerActivity implements ContinuousRecognizer.ContinuousRecognizerCallback, TextToSpeech.OnInitListener {

    private boolean isRecording = false;
    private TextToSpeech tts;
    private Button mEditButton;
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    private ArrayList<ExerciseSet> sets;
    private ArrayList<String> setStrings;
    private int exerciseID;
    private Exercise exercise;
    private Button mAddToLogBtn;
    private EditText mRepsInput;
    private EditText mWeightInput;
    private Button mInputToggleBtn;
    private Button mHistoryBtn;
    private RelativeLayout mInputSetForm;
    private ContinuousRecognizer mContinuousRecognizer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainLayoutId = R.layout.activity_record;
        super.onCreate(savedInstanceState);

        tts = new TextToSpeech(this, this);

        exerciseID = Integer.valueOf(getIntent().getExtras().get("exercise").toString());
        exercise = Exercise.getExerciseById(exerciseID).get(0);

        // setting actionbar to Name
        android.app.ActionBar ab = getActionBar();
        ab.setTitle(exercise.name);

        mContinuousRecognizer = new ContinuousRecognizer(getApplicationContext());
        mContinuousRecognizer.setContinuousRecognizerCallback(this);

        mInputSetForm = (RelativeLayout) findViewById(R.id.manualInputSetForm);
        mInputSetForm.setVisibility(View.GONE);

        mInputToggleBtn = (Button) findViewById(R.id.manualInputToggle);
        mInputToggleBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               toggleManualInputForm();
            }
        });

        mAddToLogBtn = (Button) findViewById(R.id.addToLog);
        mHistoryBtn = (Button) findViewById(R.id.historyButton);
        mRepsInput = (EditText) findViewById(R.id.repsInput);
        mWeightInput = (EditText) findViewById(R.id.weightInput);

        mAddToLogBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String repsInput = mRepsInput.getText().toString().trim();
                String weightInput = mWeightInput.getText().toString().trim();
                if (repsInput.equals("") || weightInput.equals("")) {
                    AlertDialog.Builder invalid = new AlertDialog.Builder(RecordActivity.this)
                            .setTitle("Invalid submission")
                            .setMessage("Please enter a number into Reps and Weights")
                            .setPositiveButton("OK", null);
                    invalid.show();
                } else {
                    addSet(Integer.parseInt(repsInput), Float.parseFloat(weightInput));
                }
            }
        });

        mHistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToHistory();
            }
        });

        loadSets();
    }

    private void switchToHistory() {
        selectItem(1);
    }

    /**
     * Function: Toggles visibility of manual input form
     */
    protected void toggleManualInputForm() {
        if (mInputSetForm.getVisibility() == View.VISIBLE) {
            mInputSetForm.setVisibility(View.GONE);
        } else {
            mInputSetForm.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Function: Load sets from database for current exercise
     */
    protected void loadSets() {
        // getting all exercises
        sets = new ArrayList<ExerciseSet>(ExerciseSet.getAllForExercise(exercise));
        System.out.println(">>>>>>>>>>>>>>> SETS: " + sets);

        // just converting it to Strings
        setStrings = new ArrayList<String>();
        for (ExerciseSet set : sets) {
            setStrings.add(set.toString());
        }

        mListView = (ListView) findViewById(R.id.listView);
        adapter = new ArrayAdapter<String>(this, R.layout.excercise_list_item, setStrings);
        mListView.setAdapter(adapter);
        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                removeItemFromList(i);
                return true;
            }
        });
    }

    /**
     * Function: add new set to database
     * @param reps
     * @param weight
     */
    protected void addSet(int reps, float weight) {
        ExerciseSet set = new ExerciseSet(reps, weight);
        set.exercise = exercise;
        set.dateOfSet = new java.sql.Date(new Date().getTime());
        set.save();
        loadSets();
        mRepsInput.setText("");
        mWeightInput.setText("");
    }

    /**
     * Function: delete set from database
     * @param setID
     */
    protected void deleteSet(int setID) {
        new Delete()
                .from(ExerciseSet.class)
                .where("Id = ?", setID)
                .execute();
    }

    /**
     * Function: delete selected log from list & database
     * @param i
     */
    protected void removeItemFromList(int i) {
        final int deletePosition = i;
        AlertDialog.Builder alert = new AlertDialog.Builder(
                RecordActivity.this);
        alert.setTitle("Delete");
        alert.setMessage("Do you want delete this item?");

        alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub

                // main code on after clicking yes
                ExerciseSet e = sets.get(deletePosition);
                deleteSet(Integer.valueOf(e.getId().toString()));
                loadSets();
            }
        });
        alert.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // TODO Auto-generated method stub
                dialog.dismiss();
            }
        });

        alert.show();

    }

    /**
     * Function: add log that was configured in the text fields
     * @param view
     */
    public void addLogButtonPressed(View view) {
        EditText repsInput = (EditText) findViewById(R.id.repsInput);
        EditText weightInput = (EditText) findViewById(R.id.weightInput);


        System.out.println(">>>>>>>>>>>>>>> REPS : " + repsInput.getText() + " , WEIGHT : " + weightInput.getText());
    }

        //Start Editing Button
    /*
        mEditButton = (Button) findViewById(R.id.editButton);
        mEditButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //do shit
            }
        });
    */


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.record, menu);
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

    public void recordButtonPressed(View view) {
        Button b = (Button) findViewById(R.id.recordButton);
        isRecording = !isRecording;

        if (isRecording) {
            b.setText("Stop");
            b.setBackground(getResources().getDrawable(R.drawable.cancel_button_style));
            //DO STUFF HERE
            //addSet(1000,4000);
            loadSets();
            mContinuousRecognizer.startListening();
        }
        else {
            b.setText("Record");
            b.setBackground(getResources().getDrawable(R.drawable.record_button_style));
            mContinuousRecognizer.stopListening();
        }
    }

    @Override
    public void onResult(String result) {
        String [] words = result.split(" ");
        boolean recordSet = false;
        boolean isReps = false;
        boolean isWeight = false;
        int reps = 0;
        float weight = 0;

        for(int i = 0; i < words.length; i++) {
            if(words[i].equals("record")) {
                recordSet = true;
            }
            if(recordSet) {
                if(words[i].equals("reps") && i > 0) {
                    try {
                        reps = Integer.parseInt(words[i-1]);
                        isReps = true;
                    } catch(NumberFormatException e) {
                        // handle invalid input
                        isReps = false;
                    }
                }
                else if(words[i].equals("pounds") && i > 0) {
                    try {
                        weight = Float.parseFloat(words[i-1]);
                        isReps = true;
                    } catch(NumberFormatException e) {
                        // handle invalid input
                        isReps = false;
                    }
                }
            }
        }
        if(recordSet && isReps && isWeight) {
            addSet(reps, weight);
            speak("recorded " + reps + " reps of " + weight + " pounds");
        } else {
            speak("please try again");
        }
    }

    @Override
    public void onDestroy() {
        if(tts != null) {
            tts.stop();
            tts.shutdown();
        }
        super.onDestroy();
    }

    @Override
    public void onInit(int status) {
        if (status == TextToSpeech.SUCCESS) {
            int result = tts.setLanguage(Locale.US);
            if (result == TextToSpeech.LANG_MISSING_DATA || result == TextToSpeech.LANG_NOT_SUPPORTED) {
                Log.e("TTS", "This Language is not supported");
            } else {
                Log.e("TTS", "Initialization successful");
            }
        } else {
            Log.e("TTS", "Initilization Failed!");
        }
    }

    private void speak(String text) {
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null);
    }
}
