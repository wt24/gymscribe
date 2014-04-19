package edu.berkeley.cs160.lasercats.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.sql.Date;
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
    public ExerciseSet() {
    }

    public ExerciseSet(int numReps, float weight) {
        this.numReps = numReps;
        this.weight = weight;
    }

    public static List<ExerciseSet> getAllForExercise(Exercise e) {
        return new Select()
                .from(ExerciseSet.class)
                .where("Exercise = ?", e.getId())
                .execute();
    }

    public String toString() {
        String s = "";
        s += "Set " + this.getId() + ": " + weight + " lbs x " + numReps + " reps";
        return s;
    }
}
