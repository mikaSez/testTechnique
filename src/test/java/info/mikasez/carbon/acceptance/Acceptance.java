package info.mikasez.carbon.acceptance;

import info.mikasez.carbon.stringcalculator.StringCalculator;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import static org.assertj.core.api.Assertions.assertThat;

public class Acceptance {

    @Rule
    public ExpectedException expectedException = ExpectedException.none();

    StringCalculator calculator;

    @Before
    public void setUp(){
        calculator = new StringCalculator();
    }

    @Test
    public void test_chaine_vide(){
        assertThat(calculator.add("0")).isEqualTo(0);
        assertThat(calculator.add(null)).isEqualTo(0);
    }

    @Test
    public void test_un_seul_nombre(){
        assertThat(calculator.add("1")).isEqualTo(1);
    }

    @Test
    public void test_deux_nombres(){
        assertThat(calculator.add("1,2")).isEqualTo(3);
    }

    @Test
    public void test_saut_de_ligne(){
        assertThat(calculator.add("1\n2,3")).isEqualTo(6);
    }

    @Test
    public void test_delimiteur_personalisé(){
        assertThat(calculator.add("//;\n1;2,3")).isEqualTo(6);
    }

    @Test
    public void test_ignore_numbers_plus_grand_que_1000(){
        assertThat(calculator.add("1,2,1000")).isEqualTo(3);
    }

    @Test
    //nombres negatives leur liste + foobar
    public void une_exception_est_levée_quand_les_nombres_sont_negatifs(){
        expectedException.expect(IllegalArgumentException.class);
        expectedException.expectMessage("foobar -1, -2");
        calculator.add("1,2,-1, -2");
    }
}
