package com.fullcycle.admin.catalog.infrastructure.category;

import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalog.MySQLGatewayTest;
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@MySQLGatewayTest
public class CategoryMySQLGatewayTest {

    @Autowired
    private CategoryMySQLGateway categoryGateway;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void givenValidCategory_whenCallsCreate_shouldReturnNewCategory() {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;

        Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, categoryRepository.count());

        final var output = categoryGateway.create(category);

        assertEquals(1, categoryRepository.count());
        assertNotNull(output.getId());
        assertEquals(expectedName, output.getName());
        assertEquals(expectedDescription, output.getDescription());
        assertEquals(expectedIsActive, output.isActive());
        assertNotNull(output.getCreatedAt());
        assertNull(output.getUpdatedAt());
        assertNull(output.getDeletedAt());

        final var entity = categoryRepository.findById(category.getId().getValue()).get();

        assertNotNull(entity.getId());
        assertEquals(expectedName, entity.getName());
        assertEquals(expectedDescription, entity.getDescription());
        assertEquals(expectedIsActive, entity.isActive());
        assertNotNull(entity.getCreatedAt());
        assertNull(entity.getUpdatedAt());
        assertNull(entity.getDeletedAt());
    }

    @Test
    public void givenValidCategory_whenCallsUpdate_shouldReturnCategoryUpdated() {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;

        Category category = Category.newCategory("name with error", null, expectedIsActive);

        assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        assertEquals(1, categoryRepository.count());

        final var categoryUpdated = category.clone().update(expectedName, expectedDescription, expectedIsActive);
        final var output = categoryGateway.update(categoryUpdated);

        assertEquals(1, categoryRepository.count());
        assertNotNull(output.getId());
        assertEquals(expectedName, output.getName());
        assertEquals(expectedDescription, output.getDescription());
        assertEquals(expectedIsActive, output.isActive());
        assertNotNull(output.getCreatedAt());
        assertNotNull(output.getUpdatedAt());
        assertNull(output.getDeletedAt());

        final var entity = categoryRepository.findById(category.getId().getValue()).get();

        assertNotNull(entity.getId());
        assertEquals(expectedName, entity.getName());
        assertEquals(expectedDescription, entity.getDescription());
        assertEquals(expectedIsActive, entity.isActive());
        assertNotNull(entity.getCreatedAt());
        assertNotNull(entity.getUpdatedAt());
        assertNull(entity.getDeletedAt());
    }

    @Test
    public void givenPrePersistedCategoryAndValidCategoryId_whenTryToDeletedIt_shouldDeletedCategory() {
        Category category = Category.newCategory("any name", "any description", true);
        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        assertEquals(1, categoryRepository.count());

        categoryGateway.delete(category.getId());
        assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenInvalidCategoryId_whenTryToDeletedIt_shouldDeletedCategory() {
        assertEquals(0, categoryRepository.count());

        categoryGateway.delete(CategoryID.from(UUID.randomUUID()));
        assertEquals(0, categoryRepository.count());
    }

    @Test
    public void givenPrePersistCategoryAndValidCategoryId_whenCallsFindById_shouldReturnCategory() {
        final var expectedName = "Any name";
        final var expectedDescription = "Any description";
        final var expectedIsActive = true;

        Category category = Category.newCategory(expectedName, expectedDescription, expectedIsActive);

        assertEquals(0, categoryRepository.count());
        categoryRepository.saveAndFlush(CategoryJpaEntity.from(category));
        assertEquals(1, categoryRepository.count());

        final var output = categoryGateway.findById(category.getId()).get();

        assertEquals(1, categoryRepository.count());
        assertNotNull(output.getId());
        assertEquals(expectedName, output.getName());
        assertEquals(expectedDescription, output.getDescription());
        assertEquals(expectedIsActive, output.isActive());
        assertNotNull(output.getCreatedAt());
        assertNull(output.getUpdatedAt());
        assertNull(output.getDeletedAt());
    }

    @Test
    public void givenValidCategoryIdNotStored_whenCallsFindById_shouldReturnEmpty() {
        assertEquals(0, categoryRepository.count());
        final var output = categoryGateway.findById(CategoryID.from(UUID.randomUUID()));
        assertTrue(output.isEmpty());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var category1 = Category.newCategory("any-name", null, true);
        final var category2 = Category.newCategory("any-name", null, true);
        final var category3 = Category.newCategory("any-name", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
          CategoryJpaEntity.from(category1),
          CategoryJpaEntity.from(category2),
          CategoryJpaEntity.from(category3)
        ));

        assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "", "name", "asc");
        final var output = categoryGateway.findAll(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.items().size());
        assertEquals(category1.getId(), output.items().get(0).getId());
    }

    @Test
    public void givenEmptyCategoriesTable_whenCallsFindAll_shouldReturnPage() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 0;

        assertEquals(0, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "", "name", "asc");
        final var output = categoryGateway.findAll(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.items().size());
    }

    @Test
    public void givenFollowPagination_whenCallsFindAllWithPage1_shouldReturnPaginated() {
        var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 3;

        final var category1 = Category.newCategory("any-name", null, true);
        final var category2 = Category.newCategory("any-name", null, true);
        final var category3 = Category.newCategory("any-name", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
          CategoryJpaEntity.from(category1),
          CategoryJpaEntity.from(category2),
          CategoryJpaEntity.from(category3)
        ));

        assertEquals(3, categoryRepository.count());

        var query = new SearchQuery(0, 1, "", "name", "asc");
        var output = categoryGateway.findAll(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        assertEquals(expectedPerPage, output.items().size());

        expectedPage = 1;

        query = new SearchQuery(1, 1, "", "name", "asc");
        output = categoryGateway.findAll(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        assertEquals(expectedPerPage, output.items().size());

        expectedPage = 2;

        query = new SearchQuery(2, 1, "", "name", "asc");
        output = categoryGateway.findAll(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.total());
        assertEquals(expectedPerPage, output.items().size());
    }

    @Test
    public void givenPrePersistedCategoriesAndDocAsTerms_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var category1 = Category.newCategory("any-name", null, true);
        final var category2 = Category.newCategory("doc", null, true);
        final var category3 = Category.newCategory("any-name", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
          CategoryJpaEntity.from(category1),
          CategoryJpaEntity.from(category2),
          CategoryJpaEntity.from(category3)
        ));

        assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "doc", "name", "asc");
        final var output = categoryGateway.findAll(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.items().size());
        assertEquals(category2.getId(), output.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategoriesAndDescriptionAsTerms_whenCallsFindAll_shouldReturnPaginated() {
        final var expectedPage = 0;
        final var expectedPerPage = 1;
        final var expectedTotal = 1;

        final var category1 = Category.newCategory("any-name", "any", true);
        final var category2 = Category.newCategory("any-name", "description", true);
        final var category3 = Category.newCategory("any-name", null, true);

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
          CategoryJpaEntity.from(category1),
          CategoryJpaEntity.from(category2),
          CategoryJpaEntity.from(category3)
        ));

        assertEquals(3, categoryRepository.count());

        final var query = new SearchQuery(0, 1, "description", "name", "asc");
        final var output = categoryGateway.findAll(query);

        assertEquals(expectedPage, output.currentPage());
        assertEquals(expectedPerPage, output.perPage());
        assertEquals(expectedTotal, output.items().size());
        assertEquals(category2.getId(), output.items().get(0).getId());
    }

    @Test
    public void givenPrePersistedCategories_whenCallsExistsByIds_shouldReturnIds() {
        final var category1 = Category.newCategory("any-name", "any", true);
        final var category2 = Category.newCategory("any-name", "description", true);
        final var category3 = Category.newCategory("any-name", null, true);

        final var expectedIds = List.of(category1.getId(), category2.getId());

        assertEquals(0, categoryRepository.count());

        categoryRepository.saveAll(List.of(
          CategoryJpaEntity.from(category1),
          CategoryJpaEntity.from(category2),
          CategoryJpaEntity.from(category3)
        ));

        assertEquals(3, categoryRepository.count());

        final var ids = List.of(category1.getId(), category2.getId(), CategoryID.from(UUID.randomUUID()));
        final var output = categoryGateway.existsByIds(ids);

       assertEquals(expectedIds, output);
    }
}
