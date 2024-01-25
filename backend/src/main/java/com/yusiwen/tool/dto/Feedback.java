package com.yusiwen.tool.dto;


import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * @author yusiwen
 * @date 2020/12/17 9:02
 */
public class Feedback implements Serializable {

    private int rate;

    private String email;

    private String suggest;

    private String ip;

    private LocalDateTime dateTime = LocalDateTime.now();

    public int getRate() {
        return rate;
    }

    public void setRate(int rate) {
        this.rate = rate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSuggest() {
        return suggest;
    }

    public void setSuggest(String suggest) {
        this.suggest = suggest;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public LocalDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(LocalDateTime dateTime) {
        this.dateTime = dateTime;
    }
}
