import lombok.AllArgsConstructor;
import lombok.Data;
import org.apache.commons.jexl3.*;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class Filterer {
    private static final String ARGUMENT_TEMPLATE = "column\\[(\\d+)]\\s*([<>=]+)\\s*(\"[^\"]+\"|\\S+)";
    private final String filter;

    public Filterer(String filter) {
        this.filter = filter;
    }

    public boolean matchesFilter(String text) {
        if (filter.isEmpty()) return true;
        String[] values = text.split(",");
        String boolFilter = getBoolFilter(values);
        return getResult(boolFilter);
    }

    private static boolean getResult(String boolFilter) {
        JexlEngine jexlEngine = new JexlBuilder().create();
        JexlExpression jexlExpression = jexlEngine.createExpression(boolFilter);
        JexlContext context = new MapContext();
        boolean result;
        var res = jexlExpression.evaluate(context);
        if (res instanceof Long) {
            result = (long) res == 1;
        } else if (res instanceof Boolean) {
            result = (boolean) res;
        } else {
            result = (int) res == 1;
        }
        return result;
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
        Comparable arg = values[argument.columnNum - 1], value = argument.value;
        try {
            arg = Integer.parseInt((String) arg);
            value = Integer.parseInt((String) value);
        } catch (NumberFormatException ignored) {
        }
        switch (argument.sign) {
            case EQUAL:
                return arg.equals(value);
            case LESS:
                return arg.compareTo(value) < 0;
            case GREATER:
                return arg.compareTo(value) > 0;
            case NOT_EQUAL:
                return !arg.equals(value);
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
