package imc.rps.utils;

public enum GameResultEnum {
    CLASH(0),
    PLAYER1(1),
    PLAYER2(-1),
    EVALUATION_ERROR(-100);

    private final int value;

    GameResultEnum(final int value) {
        this.value = value;
    }

    public int getValue() {
        return this.value;
    }
}
