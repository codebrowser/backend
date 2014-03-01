package rage.codebrowser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rage.codebrowser.dto.Course;
import rage.codebrowser.dto.Exercise;

public interface ExerciseRepository extends JpaRepository<Exercise, Long> {
    Exercise findByCourseAndName(Course course, String name);
}
