package lk.ijse.backend.controller;

import lk.ijse.backend.dto.CountryDTO;
import lk.ijse.backend.service.CountryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/countries")
@CrossOrigin(origins = "*")
public class CountryController {

    @Autowired
    private CountryService countryService;

    @GetMapping
    public List<CountryDTO> getCountries(@RequestParam(required = false) String search) {
        if (search != null && !search.isEmpty()) {
            return countryService.searchCountries(search);
        }
        return countryService.getAllCountries();
    }
}
