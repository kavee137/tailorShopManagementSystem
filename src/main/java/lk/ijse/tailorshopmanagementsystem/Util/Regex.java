package lk.ijse.tailorshopmanagementsystem.Util;//package lk.ijse.tailorshopmanagementsystem.Util;
//
//import java.util.regex.Matcher;
//import java.util.regex.Pattern;
//
//public class Regex {
//
//    public static boolean isTextFieldValid(TextField textField, String text){
//        String field = "";
//
//        switch (textField) {
//
//            case NAME:
//                field = "^[a-zA-Z ]+$";
//                break;
//            case NIC:
//                field = "[0-9V]+";
//                break;
//            case EMAIL:
//                field = "^([A-z])([A-z0-9.]){1,}[@]([A-z0-9]){1,10}[.]([A-z]){2,5}$";
//                break;
//
//        }
//
//
//        Pattern pattern = Pattern.compile(field);
//
//        if (text != null){
//            if (text.trim().isEmpty()){
//                return false;
//            }
//        }else {
//            return false;
//        }
//
//        Matcher matcher = pattern.matcher(text);
//
//        if (matcher.matches()){
//            return true;
//        }
//        return false;
//    }
//
//
//    public static boolean setTextColor(TextField location, javafx.scene.control.TextField textField) {
//        if (Regex.isTextFieldValid(location, textField.getText())) {
//            textField.setStyle("-fx-focus-color: green; -fx-unfocus-color: green;");
//            // textField.setStyle("-fx-control-inner-background: green; -fx-focus-color: green; -fx-unfocus-color: green;");
//
//            return true;
//        } else {
//            textField.setStyle("-fx-focus-color: red; -fx-unfocus-color: red;");
//            //textField.setStyle("-fx-control-inner-background: red; -fx-focus-color: red; -fx-unfocus-color: red;");
//            return false;
//        }
//    }
//
//
//}


import java.util.regex.Matcher;
import java.util.regex.Pattern;

//public class Regex {
//
//    public static boolean isTextFieldValid(TextField textField, String text) {
//        String field = "";
//
//        switch (textField) {
//            case NAME:
//                field = "^[a-zA-Z ]+$";
//                break;
//            case NIC:
//                field = "[0-9V]+";
//                break;
//            case ADDRESS:
//                field = "^[a-zA-Z ]+$";
//                break;
//
//            case EMAIL:
//                field = "^([A-z])([A-z0-9.]){1,}[@]([A-z0-9]){1,10}[.]([A-z]){2,5}$";
//                break;
//        }
//
//        Pattern pattern = Pattern.compile(field);
//
//        if (text != null && !text.trim().isEmpty()) {
//            Matcher matcher = pattern.matcher(text);
//            return matcher.matches();
//        }
//
//        return false;
//    }
//
//    public static boolean setTextColor(TextField textField, javafx.scene.control.TextField fxTextField) {
//        if (isTextFieldValid(textField, fxTextField.getText())) {
//            fxTextField.setStyle("-fx-border-color: green; -fx-unfocus-color: green;");
//        } else {
//            fxTextField.setStyle("-fx-border-color: red; -fx-unfocus-color: red;");
//        }
//        return false;
//    }
//}




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
            case QTY:
                field = "^[0-9]+$";
                break;
            case CUSID:
                field = "^C\\d+$";
                break;
            case RESID:
                field = "^[0-9]+$";
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