import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ParseFirstMatchSingleRegex {
    public static void main(String[] args) {
        String text = "Some text Fix after stuff #PT123 and then Medium";

        // Combined pattern with capturing groups
        Pattern combinedPattern = Pattern.compile(
            "(#PT\\d+)"                    // Group 1: PT pattern
          + "|\\b(Low|Medium|High)\\b"     // Group 2: Severity
          + "|\\b(Fix|Accept)\\b",         // Group 3: Action
            Pattern.CASE_INSENSITIVE
        );

        Matcher matcher = combinedPattern.matcher(text);

        String firstType = null;
        String firstValue = null;
        String portionBefore = null;

        if (matcher.find()) {
            if (matcher.group(1) != null) {
                firstType = "PT";
                firstValue = matcher.group(1);
            } else if (matcher.group(2) != null) {
                firstType = "Severity";
                firstValue = matcher.group(2);
            } else if (matcher.group(3) != null) {
                firstType = "Action";
                firstValue = matcher.group(3);
            }
            portionBefore = text.substring(0, matcher.start()).trim();
        } else {
            portionBefore = text; // No match found
        }

        // Output
        System.out.println("First type found: " + firstType);
        System.out.println("First match value: " + firstValue);
        System.out.println("Portion before first: " + portionBefore);
    }
}
