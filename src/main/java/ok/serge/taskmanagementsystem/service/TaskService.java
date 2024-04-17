package ok.serge.taskmanagementsystem.service;

import ok.serge.taskmanagementsystem.dto.TaskDto;
import ok.serge.taskmanagementsystem.exception.TaskNotFoundException;
import ok.serge.taskmanagementsystem.mapper.TaskMapper;
import ok.serge.taskmanagementsystem.model.Task;
import ok.serge.taskmanagementsystem.repository.TaskRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TaskService {

    private static final TaskNotFoundException TASK_NOT_FOUND = new TaskNotFoundException("Задание не было найдено");
    private static final TaskNotFoundException TASK_ALREADY_NOT_EXISTS = new TaskNotFoundException("Задание уже было удалено, либо не существует");

    private final TaskRepository taskRepository;

    public TaskService(TaskRepository taskRepository) {
        this.taskRepository = taskRepository;
    }

    @Transactional(readOnly = true)
    public List<TaskDto> findAll() {
        return taskRepository.findAll().stream()
                .map(TaskMapper::toDto)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TaskDto findById(Long id) throws TaskNotFoundException {
        Task task = taskRepository.findById(id)
                .orElseThrow(() -> TASK_NOT_FOUND);
        return TaskMapper.toDto(task);
    }

    public TaskDto create(TaskDto dto) {
        Task taskWithNoId = TaskMapper.toEntity(dto);
        Task taskWithId = taskRepository.save(taskWithNoId);
        return TaskMapper.toDto(taskWithId);
    }

    @Transactional
    public TaskDto update(Long id, TaskDto dto) throws TaskNotFoundException {
        Task target = taskRepository.findById(id)
                .orElseThrow(() -> TASK_NOT_FOUND);

        target.setVersion(target.getVersion() + 1);
        if (dto.getTitle() != null) {
            target.setTitle(dto.getTitle());
        }
        if (dto.getDescription() != null) {
            target.setDescription(dto.getDescription());
        }
        if (dto.getDueDate() != null) {
            target.setDueDate(dto.getDueDate());
        }
        if (dto.getCompleted() != null) {
            target.setCompleted(dto.getCompleted());
        }

        return TaskMapper.toDto(target);
    }

    @Transactional
    public void delete(Long id) throws TaskNotFoundException {
        Task target = taskRepository.findById(id)
                .orElseThrow(() -> TASK_ALREADY_NOT_EXISTS);
        taskRepository.delete(target);
    }
}
