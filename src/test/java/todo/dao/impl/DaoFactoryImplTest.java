package todo.dao.impl;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mockito;
import todo.dao.DaoFactory;
import todo.dao.EntityDao;
import todo.model.Task;
import todo.utils.Configuration;

import java.lang.reflect.Field;

import static org.junit.Assert.assertNotNull;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class DaoFactoryImplTest {

    private Configuration config = Mockito.mock(Configuration.class);

    private DaoFactory factory;

    @Before
    public void setUp() throws Exception {
        Field field = DaoFactoryImpl.class.getDeclaredField("INSTANCE");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    public void shouldReturnTaskDao() {
        //given
        when(config.getDriver()).thenReturn("org.h2.Driver");

        factory = DaoFactoryImpl.getInstance(config);

        //when
        EntityDao<Task> taskDao = factory.getTaskDao();

        //then
        assertNotNull(taskDao);

        verify(config).getDriver();
    }
}