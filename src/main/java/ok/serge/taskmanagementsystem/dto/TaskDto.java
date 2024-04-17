package ok.serge.taskmanagementsystem.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Null;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ok.serge.taskmanagementsystem.utils.Status;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TaskDto {

    public interface Create {

    }

    public interface Update {

    }

    @Null(groups = {Create.class, Update.class}, message = "Невалидный формат запроса")
    private Long id;

    @Size(
            groups = {Create.class, Update.class},
            max = 64,
            message = "Превышена максимальная длина заголовка"
    )
    private String title;

    @Size(
            groups = {Create.class, Update.class},
            max = 256,
            message = "Превышена максимальная длина описания"
    )
    private String description;

    @FutureOrPresent(
            groups = {Create.class, Update.class},
            message = "Дата исполнения не может быть раньше текущей"
    )
    private LocalDateTime dueDate;

    @NotNull(
            groups = {Create.class, Update.class},
            message = "Статус не задан"
    )
    private Status completed;
}
