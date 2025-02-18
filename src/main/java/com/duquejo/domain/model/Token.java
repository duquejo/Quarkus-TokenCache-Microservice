package com.duquejo.domain.model;

public class Token {
    private String tokenType;
    private Long expiresIn;
    private Long extExpiresIn;
    private String accessToken;

    public Token() {
    }

    public Token(String tokenType, Long expiresIn, Long extExpiresIn, String accessToken) {
        this.tokenType = tokenType;
        this.expiresIn = expiresIn;
        this.extExpiresIn = extExpiresIn;
        this.accessToken = accessToken;
    }

    public String getTokenType() {
        return tokenType;
    }

    public void setTokenType(String tokenType) {
        this.tokenType = tokenType;
    }

    public Long getExpiresIn() {
        return expiresIn;
    }

    public void setExpiresIn(Long expiresIn) {
        this.expiresIn = expiresIn;
    }

    public Long getExtExpiresIn() {
        return extExpiresIn;
    }

    public void setExtExpiresIn(Long extExpiresIn) {
        this.extExpiresIn = extExpiresIn;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    @Override
    public String toString() {
        return "Token{" +
                "tokenType='" + tokenType + '\'' +
                ", expiresIn=" + expiresIn +
                ", extExpiresIn=" + extExpiresIn +
                ", accessToken='" + accessToken + '\'' +
                '}';
    }
}
