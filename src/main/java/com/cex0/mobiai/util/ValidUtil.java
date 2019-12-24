package com.cex0.mobiai.util;

import java.util.regex.Pattern;

public final class ValidUtil {
    public static final String NULL_STR = "null";

    private ValidUtil() {

    }

    public static boolean isNotEmpty(Object obj) {
        if (obj instanceof String) {
            if("".equals(obj.toString().trim())) {
                return false;
            }
        }
        else if (obj == null) {
            return false;
        }
        return true;
    }

    public static boolean isEmpty(Object obj) {
        if (obj instanceof String) {
            if ("".equals(obj.toString().trim())) {
                return true;
            }
        } else if (obj == null) {
            return true;
        }

        return false;
    }

    public static boolean isNotEmptyAndNull(Object obj) {
        if (obj instanceof String) {
            if ("".equals(obj.toString().trim()) || NULL_STR.equals(obj.toString())) {
                return false;
            }
        } else if (obj == null) {
            return false;
        }

        return true;
    }

    public static boolean isEmptyAndNull(Object obj) {
        if (obj instanceof String) {
            if ("".equals(obj.toString().trim()) || NULL_STR.equals(obj.toString())) {
                return true;
            }
        } else if (obj == null) {
            return true;
        }

        return false;
    }

    public static boolean isEmptyOrNull(Object o) {
        if (o == null) {
            return true;
        } else {
            if (o instanceof String) {
                String s = (String)o;
                if (s.trim().isEmpty() || NULL_STR.equalsIgnoreCase(s)) {
                    return true;
                }
            } else if (o instanceof byte[]) {
                byte[] s = (byte[])((byte[])o);
                if (s.length == 0) {
                    return true;
                }
            }

            return false;
        }
    }

    public static boolean checkEmail(String email) {
        String regex = "^\\w+((-\\w+)|(\\.\\w+))*\\@[A-Za-z0-9]+((\\.|-)[A-Za-z0-9]+)*\\.[A-Za-z]+$";
        return Pattern.matches(regex, email);
    }

    public static boolean checkIdCard(String idCard) {
        String regex = "[1-9]\\d{13,16}[a-zA-Z0-9]{1}";
        return Pattern.matches(regex, idCard);
    }

    public static boolean checkMobile(String mobile) {
        String regex = "^(\\+\\d+)?1[34578]\\d{9}$";
        return Pattern.matches(regex, mobile);
    }

    public static boolean checkPhone(String phone) {
        String regex = "^(0[0-9]{2,3}\\-?)?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$";
        return Pattern.matches(regex, phone);
    }

    public static boolean checkDigit(String digit) {
        String regex = "\\-?[1-9](\\d+)?|0";
        return Pattern.matches(regex, digit);
    }

    public static boolean checkDecimals(String decimals) {
        String regex = "\\-?[1-9](\\d+)?(\\.\\d+)?|0|\\-0\\.\\d+";
        return Pattern.matches(regex, decimals);
    }

    public static boolean checkChinese(String chinese) {
        String regex = "^[一-龥]+$";
        return Pattern.matches(regex, chinese);
    }

    public static boolean checkBirthday(String birthday) {
        String regex = "[1-9]{4}([-./])\\d{1,2}\\1\\d{1,2}";
        return Pattern.matches(regex, birthday);
    }
}
