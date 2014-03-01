package rage.codebrowser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rage.codebrowser.dto.Snapshot;

public interface SnapshotRepository extends JpaRepository<Snapshot, Long> {
    
}
