package com.directa24.main.challenge.infrastructure.adapters.input.rest.controller;

import com.directa24.main.challenge.application.service.DirectorService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import java.util.List;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;

class GetDirectorsControllerTest {

    @Mock
    private DirectorService directorService;

    @InjectMocks
    private GetDirectorsController getDirectorsController;

    public GetDirectorsControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getDirectorsWithThresholdSuccess() {
        int threshold = 4;
        List<String> expectedDirectors = List.of("Martin Scorsese", "Woody Allen");
        when(directorService.getDirectorsWithThreshold(threshold)).thenReturn(expectedDirectors);

        ResponseEntity<List<String>> response = getDirectorsController.getDirectorsWithThreshold(threshold);

        assertThat(response.getStatusCodeValue()).isEqualTo(200);
        assertThat(response.getBody()).isNotNull().containsExactlyElementsOf(expectedDirectors);

        verify(directorService, times(1)).getDirectorsWithThreshold(threshold);
    }
}