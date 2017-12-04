package todo.dao;

import todo.model.Task;
import todo.model.TaskMetadata;
import todo.model.User;

import java.sql.Connection;

public interface DaoFactory {

    Connection getConnection();

    EntityDao<Task> getTaskDao();

    EntityDao<User> getUserDao();

    EntityDao<TaskMetadata> getTaskMetadataDao();

}
