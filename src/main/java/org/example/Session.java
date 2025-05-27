package org.example;

import java.math.BigDecimal;

public class Session {
    private static BigDecimal gameId;

    public static void setGameId(BigDecimal id) {
        gameId = id;
    }

    public static BigDecimal getGameId() {
        return gameId;
    }
}
