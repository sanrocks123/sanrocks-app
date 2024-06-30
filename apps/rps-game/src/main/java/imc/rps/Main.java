package imc.rps;

import imc.rps.service.GameRuleSchemaServiceImpl;
import imc.rps.utils.GameRunner;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) throws IOException {
        showGameOverview();
        new GameRunner(new GameRuleSchemaServiceImpl()).run();
    }

    private static void showGameOverview() {
        try (InputStream in = Main.class.getResourceAsStream("/game-overview.txt"); ) {

            String gameOverviewText = new String(in.readAllBytes(), StandardCharsets.UTF_8);
            System.out.print(gameOverviewText);

            System.out.print("\nPress 'Y' to continue OR press any key to exit : ");
            Scanner scanner = new Scanner(System.in);
            String option = scanner.nextLine();
            if (option.equalsIgnoreCase("Y") || option.equalsIgnoreCase("Yes")) {
                return;
            }
            System.exit(0);

        } catch (Exception ex) {
            System.out.printf("Oops ! something went wrong here - %s", ex);
        }
    }
}
