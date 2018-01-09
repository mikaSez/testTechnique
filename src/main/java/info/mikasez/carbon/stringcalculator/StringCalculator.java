package info.mikasez.carbon.stringcalculator;

import java.util.Arrays;
import java.util.List;
import java.util.function.IntPredicate;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.stream.Collectors.joining;
import static java.util.stream.Collectors.toList;

public class StringCalculator {

    private static final String CUSTOM_DELIMITER_DECLARATION = "//";
    //DOTALL ne peut pas être précisé au niveau du string
    private static final String CUSTOM_DELIMITER_PATTERN = "^((?!\\d).+?)+?\\n";
    private static final String CUSTOM_DELIMITER_EXTRACTOR_PATTERN = "//(.*)\\n.+";
    private static IntPredicate ALLOWED_VALUES = number -> number < 1000;
    //necessaire de ne pas reduire la chaine à [,|\n] pour l'ajout d'un delimiteur multicaractères
    private String DEFAULT_PATTERN = ",|\n";
    private Pattern extractorPattern = Pattern.compile(CUSTOM_DELIMITER_EXTRACTOR_PATTERN);


    /**
     * Methode faisant une addition des nombres si ces derniers sont inferieurs à 1000 et sont <strong>positifs</strong><br/>
     * Les nombres superieurs ou égaux à 1000 sont ignorés
     * <pre>
     *    "" = 0
     *    "1" = 1
     *    "1,2" = 3
     *    "1,1,1" = 3
     *    "1\n1,1" = 3
     *    "\\;\n1;1,1" = 3
     *    "1000,10,11" = 21
     *    "-1, 10, 3" = IllegalArgumentException
     * </pre>
     * @param addition : une chaine de caractères contenant les nombres
     *
     * @throws IllegalArgumentException : une valeur négative a été donnée à notre calculateur
     * L'ensemble des paramètres en faute est mis dans le message d'exception
     *
     * */
    public int add(String addition) {
        String pattern = DEFAULT_PATTERN;
        String value = addition;
        if (processEmptyValue(addition)) return 0;
        if(addition.startsWith(CUSTOM_DELIMITER_DECLARATION)){
            pattern = createNewDelimiterPattern(addition);
            value = addition.replaceFirst(CUSTOM_DELIMITER_PATTERN, "");
        }
        String []rawValues = value.split(pattern);
        List<Integer> values = getNumberValues(rawValues);
        throwExceptionIfNegativeValues(values);
        return getSum(values);
    }

    private String createNewDelimiterPattern(String addition) {
        Matcher m = extractorPattern.matcher(addition);
        StringBuilder sb = new StringBuilder(DEFAULT_PATTERN);
        if(m.matches()){
            sb.append('|');
            sb.append(m.group(1));
        } else {
            throw new RuntimeException("Something is wrong with delimiter: " + addition);
        }
        return sb.toString();
    }

    private int getSum(List<Integer> values) {

        return values
                .stream()
                .mapToInt(Integer::intValue)
                .filter(ALLOWED_VALUES)
                .sum();
    }

    private void throwExceptionIfNegativeValues(List<Integer> values) {
        String invalidValues = values.stream()
                                        .filter(val -> val < 0)
                                        .map(String::valueOf)
                                        .collect(joining(", "));

        if(!invalidValues.isEmpty()){
            throw new IllegalArgumentException("foobar " + invalidValues);
        }
    }

    private List<Integer> getNumberValues(String[] values) {
        return Arrays.stream(values)
                .map(String::trim)
                .mapToInt(Integer::parseInt)
                .boxed()
                .collect(toList());
    }

    private boolean processEmptyValue(String addition) {
        return addition == null || addition.isEmpty();
    }
}
