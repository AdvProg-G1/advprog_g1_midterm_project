package id.ac.ui.cs.advprog.perbaikiinaja.ServiceOrder.exception;

import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class RestExceptionHandlerTest {

    private final RestExceptionHandler handler = new RestExceptionHandler();

    @Test
    void testHandleNotFound_ReturnsNotFoundStatus() {
        EntityNotFoundException ex = new EntityNotFoundException("Item not found");
        ResponseEntity<String> response = handler.handleNotFound(ex);

        assertEquals(404, response.getStatusCodeValue());
        assertEquals("Item not found", response.getBody());
    }

    @Test
    void testHandleIllegalState_ReturnsBadRequestStatus() {
        IllegalStateException ex = new IllegalStateException("Illegal state occurred");
        ResponseEntity<String> response = handler.handleIllegalState(ex);

        assertEquals(400, response.getStatusCodeValue());
        assertEquals("Illegal state occurred", response.getBody());
    }

    @Test
    void testHandleValidation_ReturnsValidationErrors() {

        FieldError fieldError1 = new FieldError("object", "email", "must not be blank");
        FieldError fieldError2 = new FieldError("object", "password", "must be at least 8 characters");

        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(List.of(fieldError1, fieldError2));

        MethodArgumentNotValidException ex = mock(MethodArgumentNotValidException.class);
        when(ex.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<List<String>> response = handler.handleValidation(ex);

        assertEquals(400, response.getStatusCodeValue());
        List<String> errors = response.getBody();
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertTrue(errors.contains("email: must not be blank"));
        assertTrue(errors.contains("password: must be at least 8 characters"));
    }
}
