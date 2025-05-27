package org.example;

import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.*;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Scanner;

@Component
public class PlayerClient {

    public static void main(String[] args) {
        DefaultApi api = new DefaultApi();
        Scanner scanner = new Scanner(System.in);
        GameDto gameDto = new GameDto();

        BigDecimal gameId = gameDto.getGameId();

        if (gameId == null){
            System.out.println("Fehler. Es wurde kein Spiel gestartet. Prüfe die StartupBean");
        }

        System.out.println("Spiel wurde erfolgreich gestartet. GameID lautet: " + gameDto.getGameId());

        boolean gameOver = false;

        while (!gameOver) {
            GameDto game = api.gameGameIdGet(gameId);
            String status = game.getStatus().toString();
            if (status.equals("success")) {
                System.out.println("Ziel erreicht! Super :)");
                break;
            } else if (status.equals("failed")) {
                System.out.println("Ohje, leider verloren :( *mario sound*");
                break;
            }

            PositionDto pos = game.getPosition();
            System.out.println("Aktuelle Position ist: (" + pos.getPositionX() + "," + pos.getPositionY() + ")");

            DirectionDto direction = null;
            while (direction == null) {
                System.out.print("Richtung eingeben (o=oben, u=unten, l=links, r=rechts): ");
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

        System.out.println("🏁 Das Spiel ist beendet.");
        scanner.close();
    }
}
