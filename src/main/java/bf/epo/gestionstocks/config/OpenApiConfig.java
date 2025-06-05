package bf.epo.gestionstocks.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("API Gestion Stocks & Ventes")
                        .version("1.0.0")
                        .description("Documentation des endpoints REST pour gérer les produits, commandes et rapports"));
    }
}
