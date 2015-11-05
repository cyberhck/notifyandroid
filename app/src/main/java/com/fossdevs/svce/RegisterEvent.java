package com.fossdevs.svce;

public class RegisterEvent {
    private String data;

    public RegisterEvent(String event){
        data=event;
    }

    public String getData(){
        return data;
    }
}
