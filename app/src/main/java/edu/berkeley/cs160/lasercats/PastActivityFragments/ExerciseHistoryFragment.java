package edu.berkeley.cs160.lasercats.PastActivityFragments;

import android.app.Fragment;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import edu.berkeley.cs160.lasercats.Models.Exercise;
import edu.berkeley.cs160.lasercats.Models.ExerciseSet;
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
                masterActivityCallback.switchToCalendarFor(exerciseName);
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
            populateSeriesData();
            mChart = ChartFactory.getTimeChartView(getActivity(), mDataset, mRenderer, "MM DD YY");
            layout.addView(mChart);
        } else {
            mChart.repaint();
        }
    }

    private void initChart() {
        mRenderer.setApplyBackgroundColor(true);
        mRenderer.setBackgroundColor(Color.TRANSPARENT);
        mRenderer.setMarginsColor(Color.argb(0x00, 0xff, 0x00, 0x00));

        mRenderer.setAxesColor(Color.BLACK);

        mRenderer.setAxisTitleTextSize(16);
        mRenderer.setXTitle("Date");
        mRenderer.setXLabelsAngle(-45f);
        mRenderer.setYTitle("Max Weight");
        mRenderer.setXLabelsColor(Color.BLACK);
        mRenderer.setYLabelsColor(0, Color.BLACK);
        mRenderer.setZoomInLimitX(24l * 3600000l);
        mRenderer.setShowGridX(true);
        mRenderer.setShowGridY(true);
        mRenderer.setLabelsTextSize(16);
        mRenderer.setPointSize(5);
        mRenderer.setYLabelsAlign(Paint.Align.RIGHT);
        mRenderer.setYLabelsPadding(5.0f);
        mRenderer.setXLabelsAlign(Paint.Align.RIGHT);
        mCurrentSeries = new XYSeries("Max Weight");
        mDataset.addSeries(mCurrentSeries);
        mCurrentRenderer = new XYSeriesRenderer();

        mCurrentRenderer.setFillPoints(true);
        mCurrentRenderer.setPointStyle(PointStyle.CIRCLE);

        mRenderer.addSeriesRenderer(mCurrentRenderer);
    }

    private int getDiffInDays(Date d1, Date d2) {
        Long diffInMilliSeconds = Math.abs(d2.getTime() - d1.getTime());
        Long diffInDays = diffInMilliSeconds / (24l * 3600000l);
        return Integer.parseInt("" + diffInDays, 10);
    }

    private void populateSeriesData() {
        //Java Util's Date Instantiates to the time it was created
        java.util.Date dateNow = new java.util.Date();
        Date[] sqlStartEndOfToday = ExerciseSet.startEndOfDate(new Date(dateNow.getTime()));
        Date sqlSixtyDaysAgo = new Date(sqlStartEndOfToday[1].getTime() - (60l * 24l * 3600000l));
        Date[] sqlStartEndOfSixtyDaysAgo = ExerciseSet.startEndOfDate(new Date(sqlSixtyDaysAgo.getTime()));
        java.util.Date dateNowMinusSixtyDays = new java.util.Date(sqlStartEndOfSixtyDaysAgo[0].getTime());

        Exercise exerciseObj = Exercise.getExerciseByName(exerciseName).get(0);
        //Get Last 60 Days Only
        List<ExerciseSet> exerciseSets = ExerciseSet.getAllByExerciseAndRangeOfDate(exerciseObj,
                sqlStartEndOfSixtyDaysAgo[0],
                sqlStartEndOfToday[1]);

        HashMap<Date, ArrayList<ExerciseSet>> bucketOfSetsByDate = new HashMap<Date, ArrayList<ExerciseSet>>();
        for (Iterator<ExerciseSet> iter = exerciseSets.iterator(); iter.hasNext(); ) {
            ExerciseSet currentSet = iter.next();
            Date[] sqlStartEndOfCurrentSet = ExerciseSet.startEndOfDate(currentSet.dateOfSet);
            if(!bucketOfSetsByDate.containsKey(sqlStartEndOfCurrentSet[0])) {
                ArrayList<ExerciseSet> tempArrayList = new ArrayList<ExerciseSet>();
                bucketOfSetsByDate.put(sqlStartEndOfCurrentSet[0], tempArrayList);
            }
            bucketOfSetsByDate.get(sqlStartEndOfCurrentSet[0]).add(currentSet);
        }

        float globalMin = 100000000f;
        for(int i = 0; i < 60; i++) {
            Date currentDate = new Date(sqlStartEndOfSixtyDaysAgo[0].getTime() + (i * 24l * 3600000l));
            if(bucketOfSetsByDate.containsKey(currentDate)) {
                float maxWeightForDate = 0;
                ArrayList<ExerciseSet> listForDate = bucketOfSetsByDate.get(currentDate);
                for (Iterator<ExerciseSet> iter = listForDate.iterator(); iter.hasNext(); ) {
                    ExerciseSet currentSet = iter.next();
                    maxWeightForDate = Math.max(currentSet.weight, maxWeightForDate);
                }
                mCurrentSeries.add(currentDate.getTime(), maxWeightForDate);
                // Add Custom Label
                SimpleDateFormat dateFormat = new SimpleDateFormat("MMM d, yyyy");
                mRenderer.addXTextLabel(currentDate.getTime(), dateFormat.format(currentDate));
                globalMin = Math.min(globalMin, maxWeightForDate);
            }
        }
        mRenderer.setYAxisMin(globalMin - 10);
        mRenderer.setXAxisMin(sqlStartEndOfSixtyDaysAgo[0].getTime());
    }
}
