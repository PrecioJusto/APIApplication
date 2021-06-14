package app.preciojusto.application.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class TwitterRequestDTO {
    private String oauth_token;
    private String oauth_verifier;
}
