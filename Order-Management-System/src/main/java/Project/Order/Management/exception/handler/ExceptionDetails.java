package Project.Order.Management.exception.handler;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExceptionDetails {
    private LocalDateTime dateTime;
    private Integer status;
    private String error;
    private String message;
    private String path;
}
