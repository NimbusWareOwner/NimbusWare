package com.example.cheatclient.mock;

public class MockText {
    private String text;
    
    public MockText(String text) {
        this.text = text;
    }
    
    public String getString() {
        return text;
    }
    
    public static MockText literal(String text) {
        return new MockText(text);
    }
}