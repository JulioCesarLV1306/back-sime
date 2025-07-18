package com.sime.backwebsime.service;

import com.sime.backwebsime.DTO.DniResponseDTO;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Map;

@Service
public class DniService {
    
    private final WebClient webClient;
    
    @Value("${dni.api.url:https://apiperu.dev/api/dni}")
    private String dniApiUrl;
    
    @Value("${dni.api.token:}")
    private String dniApiToken;
    
    public DniService(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder
                .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(1024 * 1024))
                .build();
    }
    
    public DniResponseDTO consultarDni(String dni) {
        try {
            // Validar que el token esté configurado
            if (dniApiToken == null || dniApiToken.trim().isEmpty()) {
                DniResponseDTO errorResponse = new DniResponseDTO();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("Token de API no configurado");
                return errorResponse;
            }
            
            // Crear el cuerpo de la solicitud
            Map<String, String> requestBody = Map.of("dni", dni);
            
            // Realizar la llamada a la API
            DniResponseDTO response = webClient.post()
                    .uri(dniApiUrl)
                    .header("Authorization", "Bearer " + dniApiToken)
                    .header("Content-Type", "application/json")
                    .header("Accept", "application/json")
                    .bodyValue(requestBody)
                    .retrieve()
                    .bodyToMono(DniResponseDTO.class)
                    .timeout(Duration.ofSeconds(10))
                    .block();
            
            if (response == null) {
                DniResponseDTO errorResponse = new DniResponseDTO();
                errorResponse.setSuccess(false);
                errorResponse.setMessage("No se recibió respuesta de la API");
                return errorResponse;
            }
            
            return response;
            
        } catch (WebClientResponseException e) {
            DniResponseDTO errorResponse = new DniResponseDTO();
            errorResponse.setSuccess(false);
            
            switch (e.getStatusCode().value()) {
                case 400:
                    errorResponse.setMessage("DNI inválido o mal formateado");
                    break;
                case 401:
                    errorResponse.setMessage("Token de autorización inválido");
                    break;
                case 404:
                    errorResponse.setMessage("DNI no encontrado en RENIEC");
                    break;
                case 429:
                    errorResponse.setMessage("Límite de consultas excedido. Intente más tarde");
                    break;
                case 500:
                    errorResponse.setMessage("Error interno del servicio de consulta DNI");
                    break;
                default:
                    errorResponse.setMessage("Error en la consulta DNI: " + e.getMessage());
            }
            
            return errorResponse;
            
        } catch (Exception e) {
            DniResponseDTO errorResponse = new DniResponseDTO();
            errorResponse.setSuccess(false);
            errorResponse.setMessage("Error de conectividad: " + e.getMessage());
            return errorResponse;
        }
    }
}
