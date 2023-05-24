import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.jexl3.*;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Filterer {
    private static final String ARGUMENT_TEMPLATE = "column\\[(\\d+)]([<>=]+)(?!\\()(\\S+)(?<!\\))";
    private final String filter;

    public Filterer(String filter) {
        this.filter = filter;
    }

    public boolean matchesFilter(String text) {
        String[] values = text.split(",");
        String boolFilter = getBoolFilter(values);
        JexlEngine jexlEngine = new JexlBuilder().create();
        JexlExpression jexlExpression = jexlEngine.createExpression(boolFilter);
        JexlContext context = new MapContext();
        return (long) jexlExpression.evaluate(context) == 1;
    }

    private String getBoolFilter(String[] values) {
        Pattern pattern = Pattern.compile(ARGUMENT_TEMPLATE);
        Matcher matcher = pattern.matcher(filter);
        String boolFilter = filter;
        while (matcher.find()) {
            Argument argument = new Argument(
                    Integer.parseInt(matcher.group(1)),
                    Sign.stringToSign(matcher.group(2)),
                    matcher.group(3)
            );
            boolFilter = boolFilter.replaceFirst(ARGUMENT_TEMPLATE, String.valueOf(checkArgument(argument, values)));
        }
        return boolFilter;
    }

    public boolean checkArgument(Argument argument, String[] values) {
        switch (argument.sign) {
            case EQUAL:
                return values[argument.columnNum - 1].equals(argument.value);
            case LESS:
                return values[argument.columnNum - 1].compareTo(argument.value) < 0;
            case GREATER:
                return values[argument.columnNum - 1].compareTo(argument.value) > 0;
            case NOT_EQUAL:
                return !values[argument.columnNum - 1].equals(argument.value);
            default:
                throw new RuntimeException();
        }
    }

    private enum Sign {
        EQUAL("="), LESS("<"), GREATER(">"), NOT_EQUAL("<>");

        private final String s;

        Sign(String s) {
            this.s = s;
        }

        public static Sign stringToSign(String s) {
            return Arrays.stream(Sign.values())
                    .filter(sign -> sign.s.equals(s))
                    .findFirst()
                    .orElseThrow(IllegalArgumentException::new);
        }
    }

    @Data
    @AllArgsConstructor
    private static final class Argument {
        private int columnNum;
        private Sign sign;
        private String value;
    }
}
