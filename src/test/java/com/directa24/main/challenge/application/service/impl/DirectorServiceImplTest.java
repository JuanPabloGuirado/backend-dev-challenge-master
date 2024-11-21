package com.directa24.main.challenge.application.service.impl;

import com.directa24.main.challenge.application.dto.MovieDto;
import com.directa24.main.challenge.application.dto.MoviesResponseDto;
import com.directa24.main.challenge.domain.exception.MoviesApiException;
import com.directa24.main.challenge.infrastructure.adapters.output.client.MockClient;
import feign.FeignException;
import feign.Request;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class DirectorServiceImplTest {

    @Mock
    private MockClient mockClient;

    @InjectMocks
    private DirectorServiceImpl directorService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }
    @Test
    void testReturnDirectorListWhenMoviesArePresent() {
        int threshold = 1;
        int totalPages = 1;

        List<MovieDto> moviesPage1 = List.of(
                MovieDto.builder().director("Director A").build(),
                MovieDto.builder().director("Director A").build(),
                MovieDto.builder().director("Director B").build()
        );
        MoviesResponseDto responsePage1 = MoviesResponseDto.builder()
                .data(moviesPage1)
                .totalPages(totalPages)
                .build();

        when(mockClient.getMovies(1)).thenReturn(ResponseEntity.ok(responsePage1));

        List<String> result = directorService.getDirectorsWithThreshold(threshold);

        assertThat(result).containsExactly("Director A");
        verify(mockClient, times(1)).getMovies(1);
    }

    @Test
    void testReturnEmptyListWhenNoDirectorExceedsThreshold() {
        int threshold = 2;
        int totalPages = 1;

        List<MovieDto> moviesPage1 = List.of(
                MovieDto.builder().director("Director A").build(),
                MovieDto.builder().director("Director B").build()
        );
        MoviesResponseDto responsePage1 = MoviesResponseDto.builder()
                .data(moviesPage1)
                .totalPages(totalPages)
                .build();

        when(mockClient.getMovies(1)).thenReturn(ResponseEntity.ok(responsePage1));

        List<String> result = directorService.getDirectorsWithThreshold(threshold);

        assertThat(result).isEmpty();
        verify(mockClient, times(1)).getMovies(1);
    }

    @Test
    void testThrowMoviesApiExceptionWhenFeignClientFails() {
        int threshold = 1;

        when(mockClient.getMovies(1)).thenThrow(new MoviesApiException("Failed to fetch movies data from external API",
                "FEIGN CLIENT ERROR", HttpStatus.BAD_GATEWAY));

        assertThatThrownBy(() -> directorService.getDirectorsWithThreshold(threshold))
                .isInstanceOf(MoviesApiException.class)
                .hasMessageContaining("Failed to fetch movies data from external API");
        verify(mockClient, times(1)).getMovies(1);
    }

    @Test
    void testThrowMoviesApiExceptionWhenResponseBodyIsNull() {
        int threshold = 1;

        when(mockClient.getMovies(1)).thenReturn(null);

        assertThatThrownBy(() -> directorService.getDirectorsWithThreshold(threshold))
                .isInstanceOf(MoviesApiException.class)
                .hasMessageContaining("Couldn't retrieve response from api");
        verify(mockClient, times(1)).getMovies(1);
    }

    @Test
    void testThrowFeignExceptionWhenFeignClientFails() {
        int threshold = 1;
        when(mockClient.getMovies(1)).thenThrow(new FeignException
                .BadGateway("Feign client error", Request.create(Request.HttpMethod.GET,
                "url", Map.of(), null, null, null), null,
                null));

        assertThatThrownBy(() -> directorService.getDirectorsWithThreshold(threshold))
                .isInstanceOf(MoviesApiException.class)
                .hasMessageContaining("Failed to fetch movies data from external API")
                .hasFieldOrPropertyWithValue("status", HttpStatus.BAD_GATEWAY);
        verify(mockClient, times(1)).getMovies(1);
    }
}