package ok.serge.taskmanagementsystem.mapper;

import ok.serge.taskmanagementsystem.dto.TaskDto;
import ok.serge.taskmanagementsystem.model.Task;

public class TaskMapper {

    public static TaskDto toDto(Task task) {
        TaskDto dto = new TaskDto();
        dto.setId(task.getId());
        dto.setTitle(task.getTitle());
        dto.setDescription(task.getDescription());
        dto.setDueDate(task.getDueDate());
        dto.setCompleted(task.getCompleted());

        return dto;
    }

    public static Task toEntity(TaskDto dto) {
        Task entity = new Task();
        entity.setTitle(dto.getTitle());
        entity.setDescription(dto.getDescription());
        entity.setDueDate(dto.getDueDate());
        entity.setCompleted(dto.getCompleted());

        return entity;
    }
}
