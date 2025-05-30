package com.fullcycle.admin.catalog.application.video.delete;

import com.fullcycle.admin.catalog.application.UseCaseTest;
import com.fullcycle.admin.catalog.domain.video.MediaResourceGateway;
import com.fullcycle.admin.catalog.domain.video.VideoGateway;
import com.fullcycle.admin.catalog.domain.video.VideoID;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;

public class DeleteVideoUseCaseTest extends UseCaseTest {

    @InjectMocks
    private DefaultDeleteVideoUseCase useCase;

    @Mock
    private VideoGateway videoGateway;

    @Mock
    private MediaResourceGateway mediaResourceGateway;

    @Override
    protected List<Object> getMocks() {
        return List.of(videoGateway);
    }

    @Test
    public void givenValidId_whenCallsDeleteVideo_shouldDeleteIt() {
        final var id = VideoID.unique().getValue();

        doNothing().when(videoGateway).deleteById(any());
        assertDoesNotThrow(() -> useCase.execute(id));
        verify(videoGateway).deleteById(eq(VideoID.from(id)));
        verify(mediaResourceGateway).clearResources(eq(VideoID.from(id)));
    }

    @Test
    public void givenInvalidId_whenCallsDeleteVideo_shouldBeOk() {
        final var id = VideoID.unique().getValue();

        doNothing().when(videoGateway).deleteById(any());
        assertDoesNotThrow(() -> useCase.execute(id));
        verify(videoGateway).deleteById(eq(VideoID.from(id)));
        verify(mediaResourceGateway).clearResources(eq(VideoID.from(id)));
    }
}
