package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import repository.entity.KeywordEntity;

public interface KeywordRepository extends JpaRepository<KeywordEntity, Integer> {
}
