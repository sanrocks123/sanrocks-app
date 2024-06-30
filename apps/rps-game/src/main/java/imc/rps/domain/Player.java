package imc.rps.domain;

import java.util.List;

public abstract class Player {

    private final long id = Thread.currentThread().getId();
    protected String name = "";
    private Symbol selectedSymbol = null;

    private int numberOfWins = 0;

    public abstract String getPlayerNameInput();

    public abstract Symbol getSelectedSymbolInput(final List<Symbol> symbols);

    public int getNumberOfWins() {
        return this.numberOfWins;
    }

    public int updateWinCounter() {
        return numberOfWins++;
    }

    public void setName(final String name) {
        this.name = name;
    }

    public String getName() {
        return this.name.toUpperCase();
    }

    public void setSelectedSymbol(final Symbol selectedSymbol) {
        this.selectedSymbol = selectedSymbol;
    }

    public Symbol getSelectedSymbol() {
        return this.selectedSymbol;
    }

    public long getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return String.format(
                "id: [%s], name: [%s], selectedSymbol: [%s], wins:[%s]",
                id, name, selectedSymbol, numberOfWins);
    }
}
