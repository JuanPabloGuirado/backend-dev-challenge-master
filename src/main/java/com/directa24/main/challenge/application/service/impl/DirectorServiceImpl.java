package com.directa24.main.challenge.application.service.impl;

import com.directa24.main.challenge.application.service.DirectorService;
import com.directa24.main.challenge.application.dto.MovieDto;
import com.directa24.main.challenge.domain.exception.MoviesApiException;
import com.directa24.main.challenge.infrastructure.adapters.output.client.MockClient;
import feign.FeignException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class DirectorServiceImpl implements DirectorService {

    private final MockClient client;

    @Override
    public List<String> getDirectorsWithThreshold(Integer threshold) {
        int currentPage = 1;
        Map<String, Integer> directorMovieCount = new HashMap<>();
        AtomicInteger totalPages = new AtomicInteger(1);

        while (currentPage <= totalPages.get()) {
            try {
                Optional.ofNullable(client.getMovies(currentPage))
                        .map(ResponseEntity::getBody)
                        .ifPresentOrElse(body -> {
                            totalPages.set(body.getTotalPages());
                            List<MovieDto> movies = body.getData();

                            if (CollectionUtils.isNotEmpty(movies)) {
                                movies.forEach(movie -> directorMovieCount.merge(movie.getDirector(),
                                        1, Integer::sum));
                            }
                        }, () -> {
                            log.error("Couldn't retrieve response from api");
                            throw new MoviesApiException("Couldn't retrieve response from api",
                                    "UNEXPECTED ERROR", HttpStatus.INTERNAL_SERVER_ERROR);
                        });
            } catch (FeignException exception) {
                log.error("Error with feign client");
                throw new MoviesApiException("Failed to fetch movies data from external API",
                        "FEIGN CLIENT ERROR", HttpStatus.BAD_GATEWAY);
            }

            currentPage++;
        }

        return directorMovieCount.entrySet().stream()
                .filter(entry -> entry.getValue() > threshold)
                .map(Map.Entry::getKey)
                .sorted()
                .collect(Collectors.toList());
    }
}
