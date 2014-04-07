package edu.berkeley.cs160.lasercats;

import android.app.LauncherActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.CountDownTimer;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.app.AlertDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class RecordActivity extends BaseNavigationDrawerActivity {

    private boolean isRecording = false;
    private Button mEditButton;
    private ListView mListView;
    private ArrayAdapter<String> adapter;
    List<String> arr;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainLayoutId = R.layout.activity_record;
        super.onCreate(savedInstanceState);
        android.app.ActionBar ab = getActionBar();
        ab.setTitle((String) getIntent().getExtras().get("exercise"));

        String[] exercises = new String[]{
                "Set 1: 4.0 lbs x 8 reps",
                "Set 2: 5.0 lbs x 10 reps",
                "Set 3: 2.0 lbs x 11 reps",
                "Set 4: 4.0 lbs x 11 rep",
                "Set 5: 5.0 lbs x 12 reps",
                "Set 6: 6.0 lbs x 110 reps"
        };

        mListView = (ListView) findViewById(R.id.listView);
        arr = new ArrayList<String>(Arrays.asList(exercises));
        adapter = new ArrayAdapter<String>(this, R.layout.row, arr);

        mListView.setAdapter(adapter);

        mListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> adapterView, View view, int i, long l) {
                removeItemFromList(i);
                return true;
            }
        });
    }

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
                arr.remove(deletePosition);
                adapter.notifyDataSetChanged();
                adapter.notifyDataSetInvalidated();

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

    public void wizardAdd() {
        new CountDownTimer(5000,1000) {
            public void onTick(long millisUntilFinished) {
                //do nothing
            }
            public void onFinish() {
                RecordActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        arr.add("Set 7: 150.0 lbs x 8000 reps");
                        adapter.notifyDataSetChanged();
                        adapter.notifyDataSetInvalidated();
                    }
                });
            }
        }.start();


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
            wizardAdd();
        }
        else {
            b.setText("Record");
            b.setBackground(getResources().getDrawable(R.drawable.record_button_style));
        }
    }

}
