package vn.vnpay.demo1.config;

public class Message {

    private Message() {
        throw new IllegalStateException("Utility class");
    }
    public static final String CODE_SUCCESS = "00";
    public static final String SUCCESS = "SUCCESS";
    public static final String BANK_NOT_FOUND = "Bank not found";
    public static final String DATA_NOT_CORRECT ="Data not correct";
    public static final String CODE_IS_VALID ="01";
    public static final String CODE_BAD_REQUEST ="02";
    public static final String BANK_CODE ="bankCode";
    public static final String DATA ="Data";
    public static final String TOKEN_KEY = "TokenKey";

}
