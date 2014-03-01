package rage.codebrowser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rage.codebrowser.dto.SnapshotFile;

public interface SnapshotFileRepository extends JpaRepository<SnapshotFile, Long> {
}
