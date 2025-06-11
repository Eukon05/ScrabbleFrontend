package org.example.model;

import java.io.Serializable;

public record BestScore(int gameID, String nick, String date, int score) implements Serializable {}
