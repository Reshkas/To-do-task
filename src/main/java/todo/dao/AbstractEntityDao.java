package todo.dao;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todo.exception.DaoException;

import java.sql.*;
import java.util.List;
import java.util.Optional;

public abstract class AbstractEntityDao<T> implements EntityDao<T> {

    private static final Logger LOGGER = LoggerFactory.getLogger(AbstractEntityDao.class);

    private final DaoFactory factory;

    public AbstractEntityDao(DaoFactory factory) {
        this.factory = factory;
    }

    protected abstract String getSelectQuery();

    protected abstract String getInsertQuery();

    protected abstract String getUpdateQuery();

    protected abstract String getDeleteQuery();

    protected abstract List<T> parseResultSet(ResultSet resultSet) throws SQLException;

    protected abstract Optional<T> parseGeneratedEntityId(ResultSet resultSet, T entity) throws SQLException;

    protected abstract void prepareSelectByIdStatement(PreparedStatement statement, Integer id) throws SQLException;

    protected abstract void prepareInsertStatement(PreparedStatement statement, T entity) throws SQLException;

    protected abstract void prepareUpdateStatement(PreparedStatement statement, T entity) throws SQLException;

    protected abstract void prepareDeleteStatement(PreparedStatement statement, Integer id) throws SQLException;

    @Override
    public List<T> findAll() {
        String query = getSelectQuery();
        try (Connection connection = factory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            try (ResultSet resultSet = statement.executeQuery()) {
                return parseResultSet(resultSet);
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<T> findById(Integer id) {
        String query = getSelectQuery() + " WHERE id = ?";
        try (Connection connection = factory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            prepareSelectByIdStatement(statement, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                return parseResultSet(resultSet).stream()
                        .findFirst();
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public Optional<T> create(T entity) {
        String query = getInsertQuery();
        try (Connection connection = factory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {

            prepareInsertStatement(statement, entity);
            int insertCount = statement.executeUpdate();

            if (insertCount == 0) {
                return Optional.empty();
            }

            try (ResultSet resultSet = statement.getGeneratedKeys()) {
                return parseGeneratedEntityId(resultSet, entity);
            }

        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public int update(T entity) {
        String query = getUpdateQuery();
        try (Connection connection = factory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            prepareUpdateStatement(statement, entity);
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }

    @Override
    public int deleteById(Integer id) {
        String query = getDeleteQuery();
        try (Connection connection = factory.getConnection();
             PreparedStatement statement = connection.prepareStatement(query)) {

            prepareDeleteStatement(statement, id);
            return statement.executeUpdate();
        } catch (SQLException e) {
            LOGGER.error(e.getMessage(), e);
            throw new DaoException(e);
        }
    }
}
