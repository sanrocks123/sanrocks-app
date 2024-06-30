package imc.rps.domain;

import java.util.List;
import java.util.Random;

public class Computer extends Player {

    private final Random random = new Random();

    @Override
    public String getPlayerNameInput() {
        return "Computer";
    }

    @Override
    public Symbol getSelectedSymbolInput(final List<Symbol> symbols) {
        return symbols.get(random.nextInt(0, symbols.size()));
    }
}
