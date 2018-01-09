package info.mikasez.carbon.stringcalculator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;

import java.util.Arrays;
import java.util.Collection;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(Parameterized.class)
public class StringCalculatorTest {

    private StringCalculator calculator;

    private String stringNumber;
    private int expected;

    public StringCalculatorTest(String stringNumber, int expected) {
        this.stringNumber = stringNumber;
        this.expected = expected;
    }


    @Parameterized.Parameters(name = "{index} : testAdd({0}) = {1}")
    public static Collection<Object[]> data(){
        return Arrays.asList(new Object[][]{
                {"", 0},
                {null, 0},
                {"10", 10},
                {"10, 11", 21},
                {"10, 11, 12", 33},
                {"10\n11, 12", 33},
                {"//;\n10;11,12", 33},
                {"10, 1000, 11, 1233, 12", 33},
                {"//foobar\n11,12foobar14", 37}
        });
    }

    @Test
    public void testItAll(){
        assertThat(calculator.add(stringNumber)).isEqualTo(expected);
    }

    @Before
    public void setUp(){
        calculator = new StringCalculator();
    }

    @Test
    public void should_return_zero_when_empty_value(){
        int result = calculator.add("");

        assertThat(result).isEqualTo(0);
    }

    @Test
    public void should_return_zero_when_null_value(){
        int result = calculator.add("");

        assertThat(result).isEqualTo(0);
    }

    @Test
    public void should_return_value_if_alone(){
        int result = calculator.add("10");

        assertThat(result).isEqualTo(10);

    }

    @Test
    public void should_add_two_values(){
        int result = calculator.add("10, 5");

        assertThat(result).isEqualTo(15);
    }
}