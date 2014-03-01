
package rage.codebrowser.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import rage.codebrowser.dto.Testresult;

public interface TestRepository extends JpaRepository<Testresult, Long>{
    
}
