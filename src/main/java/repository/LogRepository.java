package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import repository.entity.LogEntity;

public interface LogRepository extends JpaRepository<LogEntity, Integer> {
}
