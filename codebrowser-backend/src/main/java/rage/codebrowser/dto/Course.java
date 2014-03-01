package rage.codebrowser.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.ArrayList;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;


@Entity
public class Course extends AbstractNamedPersistable {

    @JsonIgnore
    @ManyToMany(mappedBy = "courses")
    private List<Student> students;
    @OneToMany(mappedBy = "course")
    private List<Exercise> exercises;
    private int amountOfStudents;
    
    public void addExercise(Exercise exercise) {
        if(getExercises().contains(exercise)) {
            return;
        }
        
        getExercises().add(exercise);
    }

    public List<Exercise> getExercises() {
        if(exercises == null) {
            exercises = new ArrayList<Exercise>();
        }
        
        return exercises;
    }

    public void setExercises(List<Exercise> exercises) {
        this.exercises = exercises;
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
        this.setAmountOfStudents(students.size());

    }

    /**
     * @return the amountOfStudents
     */
    public int getAmountOfStudents() {
        if (students != null && !students.isEmpty()){
            amountOfStudents = students.size();
        } else {
            amountOfStudents = 0;
        }
        return amountOfStudents;
    }

    /**
     * @param amountOfStudents the amountOfStudents to set
     */
    public void setAmountOfStudents(int amountOfStudents) {
        this.amountOfStudents = amountOfStudents;  
    }
}