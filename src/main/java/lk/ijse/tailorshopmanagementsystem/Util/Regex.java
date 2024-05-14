package lk.ijse.tailorshopmanagementsystem.Util;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
public class Regex {

    public static boolean isTextFieldValid(TextField textField, String text) {
        String field = "";

        switch (textField) {
            case NAME:
            case ADDRESS:
                field = "^[a-zA-Z ]+$";
                break;
            case NIC:
                field = "[0-9V]+";
                break;
            case EMPID:
                field = "^E\\d+$";
                break;
            case QTY:
                field = "^[0-9]+$";
                break;
            case FABSIZE:
                field = "^[0-9]+$";
                break;
            case CUSID:
                field = "^C\\d+$";
                break;
            case SUPID:
                field = "^S\\d+$";
                break;
            case UID:
                field = "^U\\d+$";
                break;
            case EID:
                field = "^E\\d+$";
                break;
            case RESID:
                field = "^[0-9]+$";
                break;
            case FABID:
                field = "^F\\d+$";
                break;
            case PRICE:
                field = "^[0-9]+$";
                break;
            case ANY:
                field = ".*";
                break;
            case PRICEDOT:
                field = "^\\d[\\.\\d]*$";
                break;
            case DESCRIPTION:
                field = "^[a-zA-Z]+$";
                break;
            case LBLNAME:
                field = ".+";
                break;
            case EMAIL:
                field = "^([A-z])([A-z0-9.]){1,}[@]([A-z0-9]){1,10}[.]([A-z]){2,5}$";
                break;
            case TEL:
                field = "^\\d{10}$";
                break;
            case STATUS:
                field = "^(Active|Inactive)$";
                break;
        }

        Pattern pattern = Pattern.compile(field);

        if (text != null && !text.trim().isEmpty()) {
            Matcher matcher = pattern.matcher(text);
            return matcher.matches();
        }
        return false;
    }

    public static boolean setTextColor(TextField textField, javafx.scene.control.TextField fxTextField) {
        boolean isValid = isTextFieldValid(textField, fxTextField.getText());
        if (isValid) {
            fxTextField.setStyle("-fx-border-color: green; -fx-unfocus-color: green;");
        } else {
            fxTextField.setStyle("-fx-border-color: red; -fx-unfocus-color: red;");
        }
        return isValid;
    }
}