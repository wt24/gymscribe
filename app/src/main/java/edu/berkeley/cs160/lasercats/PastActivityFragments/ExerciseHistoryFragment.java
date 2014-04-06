package edu.berkeley.cs160.lasercats.PastActivityFragments;

import android.app.Fragment;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import edu.berkeley.cs160.lasercats.R;

public class ExerciseHistoryFragment extends Fragment {
    private GraphicalView mChart;

    private XYMultipleSeriesDataset mDataset = new XYMultipleSeriesDataset();

    private XYMultipleSeriesRenderer mRenderer = new XYMultipleSeriesRenderer();

    private XYSeries mCurrentSeries;

    private XYSeriesRenderer mCurrentRenderer;

    ExerciseCallbacks masterActivityCallback;
    String exerciseName;

    public ExerciseHistoryFragment(String givenExerciseName, ExerciseCallbacks callBackFuncParent) {
        exerciseName = givenExerciseName;
        masterActivityCallback = callBackFuncParent;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_exercise_history, container, false);
        Button backButton = (Button) rootView.findViewById(R.id.backBtn);
        Button calendarButton = (Button) rootView.findViewById(R.id.calBtn);

        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                masterActivityCallback.switchToSelectExercise();
            }
        });

        calendarButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        TextView titleView = (TextView) rootView.findViewById(R.id.textView);
        titleView.setText(exerciseName);
        return rootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        LinearLayout layout = (LinearLayout) getView().findViewById(R.id.chartView);
        if (mChart == null) {
            initChart();
            addSampleData();
            mChart = ChartFactory.getCubeLineChartView(getActivity(), mDataset, mRenderer, 0.3f);
            layout.addView(mChart);
        } else {
            mChart.repaint();
        }
    }

    private void initChart() {
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.TRANSPARENT);
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));

        mCurrentSeries = new XYSeries("Average Weight");
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();
        mRenderer.addSeriesRenderer(mCurrentRenderer);
    }

    private void addSampleData() {
        mCurrentSeries.add(1, 2);
        mCurrentSeries.add(2, 3);
        mCurrentSeries.add(3, 2);
        mCurrentSeries.add(4, 5);
        mCurrentSeries.add(5, 4);
        mCurrentSeries.add(6, 4);
        mCurrentSeries.add(7, 4);
        mCurrentSeries.add(8, 4);
    }
}
