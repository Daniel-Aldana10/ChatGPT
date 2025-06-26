package com.aygo.aiintegration;

/**
 * Model class representing a chat request from the user.
 */
public class ChatRequest {
    private String input;
    /**
     * Gets the user input string.
     * @return The input string
     */
    public String getInput() {
        return input;
    }

    /**
     * Sets the user input string.
     * @param input The input string
     */
    public void setInput(String input) {
        this.input = input;
    }
}