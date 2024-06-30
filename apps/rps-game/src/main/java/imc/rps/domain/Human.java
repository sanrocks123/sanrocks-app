package imc.rps.domain;

import static imc.rps.utils.GameUtils.getOption2SymbolMap;

import java.util.List;
import java.util.Scanner;

public class Human extends Player {

    private final Scanner scanner = new Scanner(System.in);

    @Override
    public String getPlayerNameInput() {
        System.out.print("\n\nplease enter your name : ");

        boolean isValidNameEntered = false;
        String name = null;

        while (!isValidNameEntered) {
            name = scanner.nextLine();
            if (!name.isBlank()) {
                isValidNameEntered = true;
            }
        }
        return name;
    }

    @Override
    public Symbol getSelectedSymbolInput(final List<Symbol> symbols) {

        printSymbolOptions();

        boolean isValidOptionSelected = false;
        String optionSelected = "00";
        int optionNo = 1000;

        while (!isValidOptionSelected) {

            System.out.print("\n\nplease select your symbol option : ");
            optionSelected = scanner.nextLine();

            try {
                optionNo = Integer.parseInt(optionSelected);
            } catch (NumberFormatException numberFormatException) {
                System.out.printf(
                        "sorry, option [%s] is invalid. please enter number only", optionSelected);
                continue;
            }

            if (getOption2SymbolMap().containsKey(optionNo)) {
                isValidOptionSelected = true;
                break;
            }

            System.out.printf("sorry, option [%d] is out of range. please retry", optionNo);
        }
        return symbols.get(optionNo - 1);
    }

    private void printSymbolOptions() {
        System.out.println("\n\nSymbol Options:");
        getOption2SymbolMap()
                .forEach(
                        (k, v) -> {
                            System.out.printf("\n%d -> %s", k, v.name());
                        });
    }
}
