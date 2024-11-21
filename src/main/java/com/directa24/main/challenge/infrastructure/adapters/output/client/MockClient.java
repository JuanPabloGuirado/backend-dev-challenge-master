package com.directa24.main.challenge.infrastructure.adapters.output.client;

import com.directa24.main.challenge.config.FeignClientConfig;
import com.directa24.main.challenge.application.dto.MoviesResponseDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "mock-api-client", url = "${mock-api-client.host}", configuration = FeignClientConfig.class)
public interface MockClient {

    @GetMapping
    ResponseEntity<MoviesResponseDto> getMovies(@RequestParam("page") int page);
}
