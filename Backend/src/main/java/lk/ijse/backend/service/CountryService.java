package lk.ijse.backend.service;

import jakarta.annotation.PostConstruct;
import lk.ijse.backend.dto.CountryDTO;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import tools.jackson.databind.JsonNode;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CountryService {
    private List<CountryDTO> cachedCountries = new ArrayList<>();
    private final RestTemplate restTemplate = new RestTemplate();

    @PostConstruct
    @Scheduled(fixedRate = 600000)
    public void fetchAndCacheCountries() {
        String url = "https://restcountries.com/v3.1/all?fields=name,capital,region,population,flags";

        try {
            JsonNode[] response = restTemplate.getForObject(url, JsonNode[].class);
            if (response != null) {
                List<CountryDTO> newCache = new ArrayList<>();
                for (JsonNode node : response) {
                    CountryDTO dto = new CountryDTO();
                    dto.setName(node.path("name").path("common").asText());

                    JsonNode capitalNode = node.path("capital");
                    dto.setCapital((capitalNode.isArray() && !capitalNode.isEmpty()) ? capitalNode.get(0).asText() : "N/A");

                    dto.setRegion(node.path("region").asText());
                    dto.setPopulation(node.path("population").asLong());
                    dto.setFlag(node.path("flags").path("svg").asText());

                    newCache.add(dto);
                }
                this.cachedCountries = newCache;
            }
        } catch (Exception e) {
            System.err.println("API Error: " + e.getMessage());
        }
    }

    public List<CountryDTO> getAllCountries() {
        return cachedCountries;
    }

    public List<CountryDTO> searchCountries(String keyword) {
        if (keyword == null || keyword.trim().isEmpty()) {
            return cachedCountries;
        }
        String lowerKeyword = keyword.toLowerCase();
        return cachedCountries.stream()
                .filter(c -> c.getName().toLowerCase().contains(lowerKeyword) ||
                        (c.getCapital() != null && c.getCapital().toLowerCase().contains(lowerKeyword)))
                .collect(Collectors.toList());
    }
}
