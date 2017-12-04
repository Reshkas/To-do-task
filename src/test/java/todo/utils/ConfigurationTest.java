package todo.utils;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class ConfigurationTest {

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Test
    public void shouldThrowExceptionWhenFileNameIsNull() {
        //given
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Properties file name cannot be NULL or EMPTY");

        //when
        Configuration.getInstance(null);
    }

    @Test
    public void shouldThrowExceptionWhenFileNameIsEmptyString() {
        //given
        thrown.expect(IllegalArgumentException.class);
        thrown.expectMessage("Properties file name cannot be NULL or EMPTY");

        //when
        Configuration.getInstance("");
    }

}