package org.upc.deliveryservice.shared.infrastructure.documentation.configuration.openapi;
import io.swagger.v3.oas.models.ExternalDocumentation;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfiguration {

    @Bean
    public OpenAPI deliveryOpenApi() {
        return new OpenAPI()
                .info(apiInfo())
                .externalDocs(externalDocs());
    }

    private Info apiInfo() {
        return new Info()
                .title("DELIVERY SERVICE API")
                .description("REST API documentation for DELIVERY SERVICE")
                .version("v1.0.0")
                .license(new License()
                        .name("Apache 2.0")
                        .url("https://www.apache.org/licenses/LICENSE-2.0"))
                .contact(new Contact()
                        .name("Cobox Team")
                        .url("https://github.com/G1-FundamentosArqui-6336")
                        .email("cobox@upc.edu.pe"));
    }

    private ExternalDocumentation externalDocs() {
        return new ExternalDocumentation()
                .description("Cobox External Documentation")
                .url("https://github.com/G1-FundamentosArqui-6336");
    }
}