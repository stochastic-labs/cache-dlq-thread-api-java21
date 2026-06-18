package com.stochasticlabs.cachedlqthreadapijava21.interfaces.http;

import com.stochasticlabs.cachedlqthreadapijava21.application.query.dto.InputResponseDTO;
import com.stochasticlabs.cachedlqthreadapijava21.application.query.usecase.SearchInputUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/inputs")
@Tag(name = "Input Queries", description = "Endpoints for fetching processed stochastic inputs via Cache-Aside pattern")
public class InputQueryController {

    private final SearchInputUseCase searchInputUseCase;

    public InputQueryController(SearchInputUseCase searchInputUseCase) {
        this.searchInputUseCase = searchInputUseCase;
    }

    @Operation(
            summary = "Search processed input by original integer value",
            description = "Looks into Redis cache first (Cache-Hit). If missing, queries MongoDB (Cache-Miss) and repopulates cache."
    )
    @ApiResponse(responseCode = "200", description = "Record found successfully")
    @ApiResponse(responseCode = "404", description = "Record not found in database")
    @GetMapping("/{originalValue}")
    public ResponseEntity<InputResponseDTO> search(@PathVariable int originalValue) {
        InputResponseDTO response = searchInputUseCase.execute(originalValue);
        return ResponseEntity.ok(response);
    }
}
