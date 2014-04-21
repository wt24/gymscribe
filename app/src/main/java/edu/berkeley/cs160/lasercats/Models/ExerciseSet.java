package edu.berkeley.cs160.lasercats.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Delete;
import com.activeandroid.query.Select;

import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by stpham on 4/16/14.
 */
@Table(name = "ExerciseSets")
public class ExerciseSet extends Model {

    @Column(name = "NumReps")
    public int numReps;
    @Column(name = "Weight")
    public float weight;
    @Column(name = "DateOfSet")
    public Date dateOfSet;
    @Column(name = "Exercise")
    public Exercise exercise;

    // constructors
    public ExerciseSet() { }

    public ExerciseSet(int numReps, float weight) {
        this.numReps = numReps;
        this.weight = weight;
    }

    public static List<ExerciseSet> getAllForExerciseSortedByDate(String e) {
        Exercise exercise = Exercise.getExerciseByName(e).get(0);
        return new Select()
                .from(ExerciseSet.class)
                .where("Exercise = ?", exercise.getId())
                .orderBy("DateOfSet DESC")
                .execute();
    }

    public static List<ExerciseSet> getAllForExercise(Exercise e) {
        return new Select()
                .from(ExerciseSet.class)
                .where("Exercise = ?", e.getId())
                .execute();
    }

    public String toString() {
        return exercise.name + " Weight " + weight + " Reps" + numReps;
    }

    public static List<ExerciseSet> getAll() {
        return new Select()
                .from(ExerciseSet.class)
                .orderBy("Id ASC")
                .execute();
    }

    /**
     * This will query for sets between 12:00am and 11:59:59pm of given date
     * @param e Associated Exercise
     * @param d Given day to query
     * @return
     */
    public static List<ExerciseSet> getAllByExerciseAndDate(Exercise e, Date d) {
        Date[] startEnd = startEndOfDate(d);
        return getAllByExerciseAndRangeOfDate(e, startEnd[0], startEnd[1]);
    }

    /**
     * Finds sets for given date range
     * @param e Associated Exercise
     * @param startDate Start of date range
     * @param endDate End of date range
     * @return
     */
    public static List<ExerciseSet> getAllByExerciseAndRangeOfDate(Exercise e, Date startDate, Date endDate) {
        return new Select()
                .from(ExerciseSet.class)
                .where("Exercise = ?", e.getId())
                .where("DateOfSet >= ? AND DateOfSet <= ?", startDate.getTime(), endDate.getTime())
                .orderBy("DateOfSet ASC")
                .execute();
    }

    /**
     * Finds the exercise object with the given name and calls the UniqueDates function whose param
     * is an exercise object
     * @param e String of exercise to get Unique dates for
     * @return
     */
    public static List<Date> getUniqueDates(String e) {
        Exercise exercise = Exercise.getExerciseByName(e).get(0);
        return getUniqueDates(exercise);
    }

    /**
     * Queries all sets associated with given Exercise and filters the list so that only unique days
     * are in the list
     * @param e Exercise object whose primary key plays an important role in the Set's exercise
     *          foreign key to query dates for
     * @return
     */
    public static List<Date> getUniqueDates(Exercise e) {
        List<ExerciseSet> allSetsByExercise = new Select()
                                                .from(ExerciseSet.class)
                                                .where("Exercise = ?", e.getId())
                                                .orderBy("DateOfSet ASC")
                                                .execute();
        List<Date> listOfUniqueDatesByDay = new ArrayList<Date>();
        for (int i = 0; i < allSetsByExercise.size(); i++) {
            ExerciseSet currentExerciseSet = allSetsByExercise.get(i);
            Date[] currentStartEnd = startEndOfDate(currentExerciseSet.dateOfSet);
            if (!listOfUniqueDatesByDay.contains(currentStartEnd[0])) {
                listOfUniqueDatesByDay.add(currentStartEnd[0]);
            }
        }
        return listOfUniqueDatesByDay;
    }

    /**
     * This functions returns a two element array whose first element zeroes the hours so that it'll
     * be 12:00am of that given day and second element is 11:59:59pm of the given day
     * @param d The day to find start and end of
     * @return
     */
    public static Date[] startEndOfDate(Date d) {
        Long dateInMilliseconds = d.getTime();
        Long secondsMinutesHoursIntoDate = dateInMilliseconds % 86400000;
        Date startDate = new Date(dateInMilliseconds - secondsMinutesHoursIntoDate);
        Date enDate = new Date(dateInMilliseconds - secondsMinutesHoursIntoDate + 86399000); // 23:59:59
        Date[] result = {startDate, enDate};
        return result;
    }

    public static void eraseTable() {
        new Delete().from(ExerciseSet.class).execute();
    }
}
