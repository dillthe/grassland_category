package web.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import java.security.Timestamp;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class KeywordDTO {
    private Long categoryId;
    private String keyword;
    private Timestamp createdAt;
}