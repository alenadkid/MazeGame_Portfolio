package org.example;

import jakarta.annotation.PostConstruct;
import org.openapitools.client.api.DefaultApi;
import org.openapitools.client.model.GameDto;
import org.openapitools.client.model.GameInputDto;
import org.springframework.stereotype.Component;

@Component
public class StartupBean {
    @PostConstruct
    public void init() {
        DefaultApi defaultApi = new DefaultApi();
    
        GameInputDto gameInput = new GameInputDto();
        gameInput.setGroupName("Steven Bethäuser und Alena Ergin");
        System.out.println("Spiel von: " + gameInput.getGroupName());
        GameDto response = defaultApi.gamePost(gameInput);
        System.out.println("Spiel wurde erfolgreich gestartet. GameID lautet: " + response.getGameId());

        Session.setGameId(response.getGameId());

    }
}
