package repository;

import org.springframework.data.jpa.repository.JpaRepository;
import repository.entity.QuestionEntity;

public interface QuestionRepository extends JpaRepository<QuestionEntity, Integer> {
}
