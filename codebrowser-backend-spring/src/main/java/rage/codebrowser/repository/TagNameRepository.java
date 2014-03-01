package rage.codebrowser.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import rage.codebrowser.dto.TagName;

public interface TagNameRepository extends JpaRepository<TagName, Long> {

    TagName findByName(String name);

    @Query("SELECT DISTINCT t.tagName FROM Tag t WHERE t.snapshot = NULL ORDER BY t.tagName.name ASC")
    List<TagName> findExerciseAnswerTagNames();

    @Query("SELECT DISTINCT t.tagName FROM Tag t WHERE t.snapshot <> NULL ORDER BY t.tagName.name ASC")
    List<TagName> findSnapshotTagNames();
}
