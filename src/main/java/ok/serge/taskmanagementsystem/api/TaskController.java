package ok.serge.taskmanagementsystem.api;

import ok.serge.taskmanagementsystem.annotation.OptimisticLock;
import ok.serge.taskmanagementsystem.dto.TaskDto;
import ok.serge.taskmanagementsystem.exception.TaskNotFoundException;
import ok.serge.taskmanagementsystem.service.TaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(value = "tasks", produces = "application/json")
@OptimisticLock
public class TaskController {

    private final TaskService taskService;

    @Autowired
    public TaskController(TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping
    public List<TaskDto> getAll() {
        return taskService.findAll();
    }

    @GetMapping("/{id}")
    public TaskDto getById(@PathVariable Long id) throws TaskNotFoundException {
        return taskService.findById(id);
    }

    @PostMapping
    public TaskDto create(@Validated(TaskDto.Create.class) @RequestBody TaskDto dto) {
        return taskService.create(dto);
    }

    @PutMapping("/{id}")
    public TaskDto update(@Validated(TaskDto.Update.class) @RequestBody TaskDto dto, @PathVariable Long id) throws TaskNotFoundException {
        return taskService.update(id, dto);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) throws TaskNotFoundException {
        taskService.delete(id);
    }
}
