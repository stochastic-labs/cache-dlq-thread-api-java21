package com.stochasticlabs.cachedlqthreadapijava21.interfaces.http;

import com.stochasticlabs.cachedlqthreadapijava21.application.query.dto.InputResponseDTO;
import com.stochasticlabs.cachedlqthreadapijava21.application.query.usecase.SearchInputUseCase;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inputs")
public class InputQueryController {

    private final SearchInputUseCase searchInputUseCase;

    public InputQueryController(SearchInputUseCase searchInputUseCase) {
        this.searchInputUseCase = searchInputUseCase;
    }

    @GetMapping("/{originalValue}")
    public ResponseEntity<InputResponseDTO> search(@PathVariable int originalValue) {
        InputResponseDTO response = searchInputUseCase.execute(originalValue);
        return ResponseEntity.ok(response);
    }
}
