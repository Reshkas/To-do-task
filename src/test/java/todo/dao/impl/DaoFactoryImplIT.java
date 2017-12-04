package todo.dao.impl;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import todo.dao.DaoFactory;
import todo.utils.Configuration;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class DaoFactoryImplIT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    private Configuration configuration = Configuration.getInstance("db-test.properties");

    @Before
    public void sutUp() throws Exception {
        Field field = DaoFactoryImpl.class.getDeclaredField("INSTANCE");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    public void shouldCreateDaoFactoryInstance() {
        //when
        DaoFactory daoFactory = DaoFactoryImpl.getInstance(configuration);

        //then
        assertNotNull(daoFactory);
    }

    @Test
    public void shouldReturnSingleInstanceOfDaoFactory() {
        //given
        DaoFactory expectedInstance = DaoFactoryImpl.getInstance(configuration);

        //when
        DaoFactory actualInstance = DaoFactoryImpl.getInstance(null);

        //then
        assertThat(actualInstance, is(expectedInstance));
    }

    @Test
    public void shouldThrowExceptionWhenConfigurationIsNull() {
        //given
        thrown.expect(NullPointerException.class);
        thrown.expectMessage("Configuration cannot be NULL");

        //when
        DaoFactoryImpl.getInstance(null);
    }

    @Test
    public void shouldReturnConnection() throws SQLException {
        //given
        DaoFactory factory = DaoFactoryImpl.getInstance(configuration);

        //when
        try (Connection connection = factory.getConnection()) {
            //then
            assertNotNull("Connection is NULL", connection);
            assertFalse("Connection is CLOSE", connection.isClosed());
        }
    }
}