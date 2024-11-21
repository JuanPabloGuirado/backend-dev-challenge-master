package com.directa24.main.challenge.infrastructure.adapters.input.rest.controller;

import com.directa24.main.challenge.application.service.DirectorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/directors")
public class GetDirectorsController {

    private final DirectorService directorService;

    @Operation(summary = "Get directors based on threshold param")
    @ApiResponse(
            responseCode = "200",
            content = {@Content(mediaType = "application/json")})
    @ApiResponse(responseCode = "400", description = "Bad request if a non numeric param is received")
    @GetMapping
    public ResponseEntity<List<String>> getDirectorsWithThreshold(@RequestParam Integer threshold) {
        return ResponseEntity.ok(directorService.getDirectorsWithThreshold(threshold));
    }
}
