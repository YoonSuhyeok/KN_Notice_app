package com.example.notice.dialog;

public class ShortUrlResult{

    String message;
    String code;
    ShortUrlData result;

    public ShortUrlData getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public static class ShortUrlData {
        String hash;
        String url;
        String orgUrl;

        public String getHash() {
            return hash;
        }

        public String getUrl() {
            return url;
        }

        public String getOrgUrl() {
            return orgUrl;
        }
    }

}