package edu.berkeley.cs160.lasercats.Models;

import com.activeandroid.Model;
import com.activeandroid.annotation.Column;
import com.activeandroid.annotation.Table;
import com.activeandroid.query.Select;

import java.io.Serializable;
import java.util.List;

/**
 * Created by stpham on 4/16/14.
 */
@Table(name = "Exercises")
public class Exercise extends Model implements Serializable{

    @Column(name = "Name")
    public String name;

    public List<ExerciseSet> sets() {
        return getMany(ExerciseSet.class, "ExerciseSet");
    }

    // constructors
    public Exercise() {
    }

    public Exercise(String name) {
        this.name = name;
    }

    public static List<Exercise> getAll() {
        return new Select()
                .from(Exercise.class)
                .orderBy("Name ASC")
                .execute();
    }

    public static List<Exercise> getExerciseById(int id) {
        return new Select()
                .from(Exercise.class)
                .where("Id = ?", id)
                .execute();
    }

    public static List<Exercise> getExerciseByName(String name) {
        return new Select()
                .from(Exercise.class)
                .where("Name = ?", name)
                .execute();
    }


    public String toString() {
        return name;
    }

    //TODO: getExercise for given day

}
