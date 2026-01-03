package br.com.api.ecommerce_email.models;

import br.com.api.ecommerce_email.models.enums.StatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "tb_logs")
@NoArgsConstructor
@AllArgsConstructor
@Data
@EqualsAndHashCode(of = "id")
public class EmailLog {
    @Id
    private String id;
    private String emailFrom;
    private String emailTo;
    private String eventType;
    private LocalDateTime sentAt;
    private StatusEnum status;
}