package org.example;

import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.math.BigDecimal;
import java.util.Scanner;

@SpringBootApplication
public class MazegameApplication {
    public static void main(String[] args) {

        SpringApplication app = new SpringApplication(MazegameApplication.class);
        app.setWebApplicationType(WebApplicationType.NONE);
        app.run(args);
    }

    @Bean
    public CommandLineRunner playGame() {
        return args -> {
            DefaultApi api = new DefaultApi();

            GameInputDto gameInput = new GameInputDto();
            gameInput.setGroupName("Steven Bethäuser und Alena Ergin");
            GameDto response = api.gamePost(gameInput);
            BigDecimal gameId = response.getGameId();

            System.out.println("Spiel wurde erfolgreich gestartet. GameID lautet: " + gameId);

            Scanner scanner = new Scanner(System.in);
            boolean gameOver = false;

            while (!gameOver) {
                GameDto game = api.gameGameIdGet(gameId);
                String status = game.getStatus().toString();
                if (status.equals("success")) {
                    System.out.println("Ziel erreicht! Super :");
                    break;
                } else if (status.equals("failed")) {
                    System.out.println("Ohje, leider verloren :( *mario sound*");
                    break;
                }

                PositionDto pos = game.getPosition();
                System.out.println("Aktuelle Position: (" + pos.getPositionX() + "," + pos.getPositionY() + ")");

                DirectionDto direction = null;
                while (direction == null) {
                    System.out.print("Bitte Richtung eingeben (o=oben, u=unten, l=links, r=rechts): ");
                    String input = scanner.nextLine().trim().toLowerCase();
                    switch (input) {
                        case "o": direction = DirectionDto.UP; break;
                        case "u": direction = DirectionDto.DOWN; break;
                        case "l": direction = DirectionDto.LEFT; break;
                        case "r": direction = DirectionDto.RIGHT; break;
                        default: System.out.println("Ungültige Eingabe. Bitte versuche es erneut mit einer der vorgegeben Eingaben."); break;
                    }
                }

                MoveInputDto move = new MoveInputDto();
                move.setDirection(direction);

                try {
                    MoveDto moveResult = api.gameGameIdMovePost(gameId, move);
                    System.out.println("Bewegung: " + direction + " -> " + moveResult.getMoveStatus());
                } catch (Exception e) {
                    System.out.println("Fehler bei der Bewegung: " + e.getMessage());
                    gameOver = true;
                }
            }

            System.out.println("Spiel beendet.");
            scanner.close();
        };
    }
}