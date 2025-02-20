package com.fullcycle.admin.catalog.domain.category;

import com.fullcycle.admin.catalog.domain.errors.DomainException;
import com.fullcycle.admin.catalog.domain.validation.handler.ThrowsValidationHandler;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class CategoryTest {

    @Test
    public void givenValidParams_whenCallNewCategory_thenInstantiateCategory() {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertNotNull(category);
        assertNotNull(category.getId());
        assertEquals(expectedName, category.getName());
        assertEquals(expectedDescription, category.getDescription());
        assertEquals(expectedIsActive, category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNull(category.getUpdatedAt());
        assertNull(category.getDeletedAt());
    }

    @Test
    public void givenInvalidNullName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be null";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;

        final var category = Category.newCategory(null, expectedDescription, expectedIsActive);

        final var expectException = assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, expectException.getErrors().size());
        assertEquals(expectedErrorMessage, expectException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidEmptyName_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = " ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name cannot be empty";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectException = assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, expectException.getErrors().size());
        assertEquals(expectedErrorMessage, expectException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameLengthLessThan3_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = "Fi ";
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name must be between 3 and 255 characters";
        final var expectedDescription = "Categorias relacionadas a filmes.";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectException = assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, expectException.getErrors().size());
        assertEquals(expectedErrorMessage, expectException.getErrors().get(0).message());
    }

    @Test
    public void givenInvalidNameLengthMoreThan255_whenCallNewCategoryAndValidate_thenShouldReceiveError() {
        final var expectedName = """
            É claro que a consolidação das estruturas obstaculiza a apreciação da importância das diretrizes de 
            desenvolvimento para o futuro. O que temos que ter sempre em mente é que a mobilidade dos capitais 
            internacionais oferece uma interessante oportunidade para verificação das formas de ação. A prática 
            cotidiana prova que o início da atividade geral de formação de atitudes estimula a padronização do
            remanejamento dos quadros funcionais. 
         """;
        final var expectedErrorCount = 1;
        final var expectedErrorMessage = "Name must be between 3 and 255 characters";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        final var expectException = assertThrows(DomainException.class, () -> category.validate(new ThrowsValidationHandler()));

        assertEquals(expectedErrorCount, expectException.getErrors().size());
        assertEquals(expectedErrorMessage, expectException.getErrors().get(0).message());
    }

    @Test
    public void givenValidEmptyDescription_whenCallNewCategoryAndValidate_thenShouldReceiveOk() {
        final var expectedName = "Any name";
        final var expectedDescription = "   ";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        assertNotNull(category);
        assertNotNull(category.getId());
        assertEquals(expectedName, category.getName());
        assertEquals(expectedDescription, category.getDescription());
        assertEquals(expectedIsActive, category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNull(category.getUpdatedAt());
        assertNull(category.getDeletedAt());
    }

    @Test
    public void givenValidFalseIsActive_whenCallNewCategoryAndValidate_thenShouldReceiveOk() {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = false;

        final var category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        assertNotNull(category);
        assertNotNull(category.getId());
        assertEquals(expectedName, category.getName());
        assertEquals(expectedDescription, category.getDescription());
        assertEquals(expectedIsActive, category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNull(category.getUpdatedAt());
        assertNotNull(category.getDeletedAt());
    }

    @Test
    public void givenValidActiveCategory_whenCallDeactivate_thenReturnCategoryInactive() {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = false;

        final var category = Category.newCategory(expectedName, expectedDescription, true);

        assertTrue(category.isActive());
        assertNull(category.getDeletedAt());

        final var categoryDeactivated = category.deactivate();

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        assertEquals(category.getId(), categoryDeactivated.getId());
        assertEquals(expectedName, category.getName());
        assertEquals(expectedDescription, category.getDescription());
        assertEquals(expectedIsActive, category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNotNull(categoryDeactivated.getUpdatedAt());
        assertNotNull(categoryDeactivated.getDeletedAt());
    }

    @Test
    public void givenValidInactiveCategory_whenCallActivate_thenReturnCategoryActivate() {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;

        final var category = Category.newCategory(expectedName, expectedDescription, false);

        assertFalse(category.isActive());
        assertNotNull(category.getDeletedAt());

        final var categoryDeactivated = category.activate();

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));
        assertEquals(category.getId(), categoryDeactivated.getId());
        assertEquals(expectedName, category.getName());
        assertEquals(expectedDescription, category.getDescription());
        assertEquals(expectedIsActive, category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNotNull(categoryDeactivated.getUpdatedAt());
        assertNull(categoryDeactivated.getDeletedAt());
    }

    @Test
    public void givenValidCategory_whenCallUpdate_thenReturnCategoryUpdated() {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;

        final var category = Category.newCategory("Any name", "Any category", false);
        final var updatedCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        assertEquals(category.getId(), updatedCategory.getId());
        assertEquals(expectedName, category.getName());
        assertEquals(expectedDescription, category.getDescription());
        assertEquals(expectedIsActive, category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNotNull(updatedCategory.getUpdatedAt());
        assertNull(updatedCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategory_whenCallUpdateToInactive_thenReturnCategoryUpdated() {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = false;

        final var category = Category.newCategory("Any name", "Any category", true);

        assertTrue(category.isActive());

        final var updatedCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        assertDoesNotThrow(() -> category.validate(new ThrowsValidationHandler()));

        assertEquals(category.getId(), updatedCategory.getId());
        assertEquals(expectedName, category.getName());
        assertEquals(expectedDescription, category.getDescription());
        assertEquals(expectedIsActive, category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNotNull(updatedCategory.getUpdatedAt());
        assertNotNull(updatedCategory.getDeletedAt());
    }

    @Test
    public void givenValidCategory_whenCallUpdateWithInvalidParam_thenReturnCategoryUpdated() {
        final String expectedName = null;
        final var expectedDescription = "Any description";
        final var expectedIsActive = false;

        final var category = Category.newCategory("Any name", "Any category", true);

        assertTrue(category.isActive());

        final var updatedCategory = category.update(expectedName, expectedDescription, expectedIsActive);

        assertEquals(category.getId(), updatedCategory.getId());
        assertNull(updatedCategory.getName());
        assertEquals(expectedDescription, category.getDescription());
        assertEquals(expectedIsActive, category.isActive());
        assertNotNull(category.getCreatedAt());
        assertNotNull(updatedCategory.getUpdatedAt());
        assertNotNull(updatedCategory.getDeletedAt());
    }
}
