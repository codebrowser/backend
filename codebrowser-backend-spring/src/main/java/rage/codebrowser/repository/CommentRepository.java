package rage.codebrowser.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rage.codebrowser.dto.Comment;
import rage.codebrowser.dto.Course;
import rage.codebrowser.dto.Exercise;
import rage.codebrowser.dto.Snapshot;
import rage.codebrowser.dto.Student;

public interface CommentRepository extends JpaRepository<Comment, Long> {

    @Query("SELECT c FROM Comment c WHERE (c.student = ?1 and c.course = ?2 and c.exercise = ?3) and (c.snapshot = ?4 or c.snapshot = NULL)")
    Page<Comment> findSolutionComments(Student student, Course course, Exercise exercise, Snapshot snapshot, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE (c.student = ?1 and c.course = ?2 and c.exercise = ?3) and (c.snapshot = ?4 or c.snapshot = NULL) and (LOWER(c.comment) LIKE %?5% OR LOWER(c.exercise.name) LIKE %?5% OR LOWER(c.course.name) LIKE %?5% OR LOWER(c.student.name) LIKE %?5% OR LOWER(c.username) LIKE %?5%)")
    Page<Comment> findSolutionCommentsContaining(Student student, Course course, Exercise exercise, Snapshot snapshot, String searchString, Pageable pageable);

    @Query("SELECT c FROM Comment c WHERE LOWER(c.comment) LIKE %?1% OR LOWER(c.exercise.name) LIKE %?1% OR LOWER(c.course.name) LIKE %?1% OR LOWER(c.student.name) LIKE %?1% OR LOWER(c.username) LIKE %?1%")
    Page<Comment> findCommentsContaining(String searchString, Pageable pageable);
}