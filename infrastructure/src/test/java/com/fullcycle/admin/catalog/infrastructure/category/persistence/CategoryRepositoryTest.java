package com.fullcycle.admin.catalog.infrastructure.category.persistence;

import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.MySQLGatewayTest;
import org.hibernate.PropertyValueException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CategoryRepositoryTest {

    @Autowired
    private CategoryRepository repository;

    @Test
    public void givenInvalidNullName_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "name";
        final var expectedMessage = "not-null property references a null or transient value: com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.name";
        final var category = Category.newCategory("any name", "any description", true);

        final var entity = CategoryJpaEntity.from(category);
        entity.setName(null);

        final var exception = assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));
        final var cause = assertInstanceOf(PropertyValueException.class, exception.getCause());
        assertEquals(expectedPropertyName, cause.getPropertyName());
        assertEquals(expectedMessage, cause.getMessage());
    }

    @Test
    public void givenInvalidNullCreatedAt_whenCallsSave_shouldReturnError() {
        final var expectedPropertyName = "createdAt";
        final var expectedMessage = "not-null property references a null or transient value: com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity.createdAt";
        final var category = Category.newCategory("any name", "any description", true);

        final var entity = CategoryJpaEntity.from(category);
        entity.setCreatedAt(null);

        final var exception = assertThrows(DataIntegrityViolationException.class, () -> repository.save(entity));
        final var cause = assertInstanceOf(PropertyValueException.class, exception.getCause());
        assertEquals(expectedPropertyName, cause.getPropertyName());
        assertEquals(expectedMessage, cause.getMessage());
    }

}
