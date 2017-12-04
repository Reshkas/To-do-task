package todo.utils;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import todo.exception.ConfigurationLoadException;

import java.lang.reflect.Field;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

public class ConfigurationIT {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    public void sutUp() throws Exception {
        Field field = Configuration.class.getDeclaredField("INSTANCE");
        field.setAccessible(true);
        field.set(null, null);
    }

    @Test
    public void shouldReturnConfigurationInstance() {
        //given
        String fileName = "db-test.properties";

        //when
        Configuration configuration = Configuration.getInstance(fileName);

        //then
        assertNotNull(configuration);
    }

    @Test
    public void shouldThrowExceptionWhenCannotFindFileProperty() {
        //given
        thrown.expect(ConfigurationLoadException.class);
        thrown.expectMessage("Cannot find file: test.properties");

        String fileName = "test.properties";

        //when
        Configuration.getInstance(fileName);
    }

    @Test
    public void shouldReturnSingleInstanceOfDaoFactory() {
        //given
        String fileName = "db-test.properties";
        Configuration expectedInstance = Configuration.getInstance(fileName);

        //when
        Configuration actualInstance = Configuration.getInstance(null);

        //then
        assertThat(actualInstance, is(expectedInstance));
    }

    @Test
    public void shouldReturnDbProperties() {
        //given
        Configuration configuration = Configuration.getInstance("db-test.properties");

        //when
        String driverClassName = configuration.getDriver();
        String dbUrl = configuration.getUrl();
        String dbUser = configuration.getUser();
        String dbPassword = configuration.getPassword();

        //then
        assertThat("Cannot find key 'db.driver'", driverClassName, is("org.h2.Driver"));
        assertThat("Cannot find key 'db.url'", dbUrl, is("jdbc:h2:mem:tododb;DB_CLOSE_DELAY=-1"));
        assertThat("Cannot find key 'db.user'", dbUser, is("root"));
        assertThat("Cannot find key 'db.pass'", dbPassword, is("root"));
    }
}