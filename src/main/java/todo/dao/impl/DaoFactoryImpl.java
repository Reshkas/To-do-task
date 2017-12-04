package todo.dao.impl;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todo.dao.DaoFactory;
import todo.dao.EntityDao;
import todo.exception.DaoException;
import todo.model.Task;
import todo.model.TaskMetadata;
import todo.model.User;
import todo.utils.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Objects;

public class DaoFactoryImpl implements DaoFactory {

    private static final Logger LOGGER = LoggerFactory.getLogger(DaoFactoryImpl.class);
    private static DaoFactory INSTANCE = null;

    private final Configuration config;

    private EntityDao<Task> taskDao;

    private DaoFactoryImpl(Configuration config) {
        this.config = Objects.requireNonNull(config, "Configuration cannot be NULL");
        try {
            Class.forName(config.getDriver());
        } catch (ClassNotFoundException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    public static DaoFactory getInstance(Configuration config) {
        if (INSTANCE == null) {
            INSTANCE = new DaoFactoryImpl(config);
        }
        return INSTANCE;
    }

    @Override
    public Connection getConnection() {
        String url = config.getUrl();
        String user = config.getUser();
        String password = config.getPassword();
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public EntityDao<Task> getTaskDao() {
        if (taskDao == null) {
            taskDao = new TaskDao(this);
        }
        return taskDao;
    }

    @Override
    public EntityDao<User> getUserDao() {
        return null;
    }

    @Override
    public EntityDao<TaskMetadata> getTaskMetadataDao() {
        return null;
    }
}
