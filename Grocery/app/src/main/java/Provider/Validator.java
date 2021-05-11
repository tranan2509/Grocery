package Provider;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Validator {
    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
            + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";
    private static final String PHONE = "^[+]*[(]{0,1}[0-9]{1,3}[)]{0,1}[-\\s\\./0-9]*$";

    private Pattern pattern;

    public Validator() {
        pattern = Pattern.compile(EMAIL_PATTERN);
    }

    // Class kiểm định dạng email
    public boolean validate(final String hex) {

        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public static boolean validateEmail(final String hex){
        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public static boolean validatePhone(final String hex){
        if (hex.length() < 10)
            return false;
        Pattern pattern = Pattern.compile(PHONE);
        Matcher matcher = pattern.matcher(hex);
        return matcher.matches();
    }

    public static boolean validatePassword(final String hex){
        if (hex.length() < 6)
            return false;
        return true;
    }

    public static boolean validateDob(final String date){
        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
        boolean valid = false;
        try {
            // why 2008-02-2x, 20-11-02, 12012-04-05 are valid date?
            sdf.parse(date);
            // strict mode - check 30 or 31 days, leap year
            sdf.setLenient(false);
            valid = true;

        } catch (ParseException e) {
            e.printStackTrace();
            valid = false;
        }
        return valid;
    }
}
