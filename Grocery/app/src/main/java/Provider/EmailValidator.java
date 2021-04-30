package Provider;

import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class EmailValidator{
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";// cấu trúc 1 email thông thường

    private Pattern pattern;

    public EmailValidator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    // Class kiểm định dạng email
    public boolean validate(final String hex) {

        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public static boolean validate2(final String hex){
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }
}
