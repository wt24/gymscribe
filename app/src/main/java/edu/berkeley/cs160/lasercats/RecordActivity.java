package edu.berkeley.cs160.lasercats;

import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;


public class RecordActivity extends BaseNavigationDrawerActivity {

    private boolean isRecording = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        mainLayoutId = R.layout.activity_record;
        super.onCreate(savedInstanceState);

    }


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
        }
        else {
            b.setText("Record");
            b.setBackground(getResources().getDrawable(R.drawable.record_button_style));
        }
    }

}
