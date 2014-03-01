package rage.codebrowser.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import rage.codebrowser.dto.Exercise;
import rage.codebrowser.dto.ExerciseAnswer;
import rage.codebrowser.dto.Student;

public interface ExerciseAnswerRepository extends JpaRepository<ExerciseAnswer, Long> {

    ExerciseAnswer findByStudentAndExercise(Student student, Exercise exercise);
    List<ExerciseAnswer> findByStudent(Student student);
}
