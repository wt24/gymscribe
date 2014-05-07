package edu.berkeley.cs160.lasercats;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.speech.tts.TextToSpeech;
import android.speech.tts.TextToSpeech.OnInitListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.activeandroid.query.Delete;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import edu.berkeley.cs160.lasercats.Models.Exercise;
import edu.berkeley.cs160.lasercats.Models.ExerciseSet;


public class RecordActivity extends BaseNavigationDrawerActivity implements ContinuousRecognizer.ContinuousRecognizerCallback, OnInitListener {

    private boolean isRecording = false;
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
    // TTS stuff
    private int MY_DATA_CHECK_CODE = 0;
    private TextToSpeech myTTS;
    boolean saidTwoNums = false;
    int repsResult = 0;
    int weightResult = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainLayoutId = R.layout.activity_record;
        super.onCreate(savedInstanceState);


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

        // So upon creating your activity, a good first step is to check for the presence of
        // the TTS resources with the corresponding intent:
        Intent checkTTSIntent = new Intent();
        checkTTSIntent.setAction(TextToSpeech.Engine.ACTION_CHECK_TTS_DATA);
        startActivityForResult(checkTTSIntent, MY_DATA_CHECK_CODE);

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

    /* Checks if the user needs to install package for TTS */
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == MY_DATA_CHECK_CODE) {
            if (resultCode == TextToSpeech.Engine.CHECK_VOICE_DATA_PASS) {
                myTTS = new TextToSpeech(this, this);
            }
            else {
                Intent installTTSIntent = new Intent();
                installTTSIntent.setAction(TextToSpeech.Engine.ACTION_INSTALL_TTS_DATA);
                startActivity(installTTSIntent);
            }
        }
    }

    public void speakWords(String speech) {
        //speak straight away
        myTTS.speak(speech, TextToSpeech.QUEUE_FLUSH, null);
    }

    //setup TTS
    public void onInit(int initStatus) {
        //check for successful instantiation
        if (initStatus == TextToSpeech.SUCCESS) {
            if(myTTS.isLanguageAvailable(Locale.US)==TextToSpeech.LANG_AVAILABLE)
                myTTS.setLanguage(Locale.US);
        }
        else if (initStatus == TextToSpeech.ERROR) {
            Toast.makeText(this, "Sorry! Text To Speech failed...", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onResult(String result) {
        Log.e("RESULT", result);
        String [] words = result.split(" ");
        boolean recordSet = false;
        boolean isReps = false;
        boolean isWeight = false;
        int reps = 0;
        float weight = 0;
        // storing numbers for next onResult

        if (words[0].equals("record") || words[0].equals("because") || words[0].equals("décor") || words[0].equals("workin") || words[0].equals("week")) {
            recordSet = true;
        }
        if (saidTwoNums) {
            Log.e("saidTwoNums", "IN HERE");
            if (words[0].equals("yes") || words[0].equals("yeah") || words[0].equals("yup") || words[0].equals("sure")) {
                saidTwoNums = false;
                Log.e("saidTwoNums", "IN YES");
                addSet(repsResult, weightResult);
                mContinuousRecognizer.stopListening();
                String confirmed = "The set is recorded";
                speakWords(confirmed);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mContinuousRecognizer.startListening();
                    }
                }, 2000);
            } else if (words[0].equals("No") || words[0].equals("nope") || words[0].equals("no")) {
                saidTwoNums = false;
                mContinuousRecognizer.stopListening();
                String tryAgain = "Okay please try again";
                speakWords(tryAgain);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mContinuousRecognizer.startListening();
                    }
                }, 2000);
            } else {
                Log.e("WRONG WORD for confirmation", "IGNORING because not yes or no");
            }
        } else if (recordSet) {
            Log.e("recordSet", "IN HERE");
            mContinuousRecognizer.stopListening();
            List<String> numbers = new ArrayList<String>();
            for (int i=0; i< words.length; i++) {
                if (words[i].matches("[0-9]+")) { // matches uses regex
                    Log.e("Match", words[i]);
                    numbers.add(words[i]);
                }
            }
            if (numbers.size() >= 2) {
                saidTwoNums = true;
                repsResult = Integer.parseInt(numbers.get(0));
                weightResult = Integer.parseInt(numbers.get(1));
                mContinuousRecognizer.stopListening();
                String correctText = "Did you say " + numbers.get(0) + " reps of " +  numbers.get(1) + "?";
                Log.e("saying", correctText);
                speakWords(correctText);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mContinuousRecognizer.startListening();
                    }
                }, 2500);
            } else {
                mContinuousRecognizer.stopListening();
                String wrongText = "Sorry we didn't quite get that. Please repeat";
                speakWords(wrongText);
                final Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mContinuousRecognizer.startListening();
                    }
                }, 4000);
            }
        } else {
            Log.e("WRONG WORD", "IGNORING");
        }
    }
}
