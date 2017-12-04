package todo.dao.impl;

import todo.dao.AbstractEntityDao;
import todo.dao.DaoFactory;
import todo.model.Status;
import todo.model.Task;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class TaskDao extends AbstractEntityDao<Task> {

    public TaskDao(DaoFactory factory) {
        super(factory);
    }

    @Override
    protected String getSelectQuery() {
        return "SELECT * FROM tasks";
    }

    @Override
    protected String getInsertQuery() {
        return "INSERT INTO tasks (title, description, deadline, status) VALUES(?, ?, ?, ?)";
    }

    @Override
    protected String getUpdateQuery() {
        return "UPDATE tasks SET title = ?, description = ?, deadline = ?, status = ? WHERE id = ?";
    }

    @Override
    protected String getDeleteQuery() {
        return "DELETE FROM tasks WHERE id = ?";
    }

    @Override
    protected List<Task> parseResultSet(ResultSet resultSet) throws SQLException {
        List<Task> tasks = new ArrayList<>();
        while (resultSet.next()) {
            Task task = createTask(resultSet);
            tasks.add(task);
        }
        return tasks;
    }

    @Override
    protected Optional<Task> parseGeneratedEntityId(ResultSet resultSet, Task task) throws SQLException {
        if (resultSet.next()) {
            return Optional.of(createTask(resultSet));
        }
        return Optional.empty();
    }

    @Override
    protected void prepareSelectByIdStatement(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    @Override
    protected void prepareInsertStatement(PreparedStatement statement, Task task) throws SQLException {
        parseTaskParams(statement, task);
    }

    @Override
    protected void prepareUpdateStatement(PreparedStatement statement, Task task) throws SQLException {
        parseTaskParams(statement, task);
        statement.setInt(5, task.getId());
    }

    @Override
    protected void prepareDeleteStatement(PreparedStatement statement, Integer id) throws SQLException {
        statement.setInt(1, id);
    }

    private Task createTask(ResultSet resultSet) throws SQLException {
        int id = resultSet.getInt(1);
        String title = resultSet.getString(2);
        String description = resultSet.getString(3);
        Timestamp timestamp = resultSet.getTimestamp(4);
        LocalDateTime deadline = timestamp != null ? timestamp.toLocalDateTime() : null;
        Status status = Status.of(resultSet.getString(5));
        return new Task(id, title, description, deadline, status);
    }

    private void parseTaskParams(PreparedStatement statement, Task task) throws SQLException {
        statement.setString(1, task.getTitle());
        statement.setString(2, task.getDescription());
        LocalDateTime deadline = task.getDeadline();
        Timestamp timestamp = deadline != null ? Timestamp.valueOf(deadline) : null;
        statement.setTimestamp(3, timestamp);
        Status status = task.getStatus();
        String statusName = status != null ? status.name() : null;
        statement.setString(4, statusName);
    }

}
