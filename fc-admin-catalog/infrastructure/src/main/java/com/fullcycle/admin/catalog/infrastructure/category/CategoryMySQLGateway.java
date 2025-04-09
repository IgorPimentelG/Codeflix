package com.fullcycle.admin.catalog.infrastructure.category;

import com.fullcycle.admin.catalog.domain.category.Category;
import com.fullcycle.admin.catalog.domain.category.CategoryGateway;
import com.fullcycle.admin.catalog.domain.category.CategoryID;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalog.infrastructure.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static com.fullcycle.admin.catalog.infrastructure.utils.SpecificationUtils.like;

@RequiredArgsConstructor
@Service
public class CategoryMySQLGateway implements CategoryGateway {

    private final CategoryRepository repository;

    @Override
    public Category create(final Category category) {
        return repository.save(CategoryJpaEntity.from(category)).toAggregate();
    }

    @Override
    public Category update(final Category category) {
        return repository.save(CategoryJpaEntity.from(category)).toAggregate();
    }

    @Override
    public Optional<Category> findById(final CategoryID id) {
        return repository.findById(id.getValue()).map(CategoryJpaEntity::toAggregate);
    }

    @Override
    public List<CategoryID> existsByIds(final Iterable<CategoryID> id) {
        return Collections.emptyList();
    }

    @Override
    public Pagination<Category> findAll(final SearchQuery query) {
        final var page = PageRequest.of(
          query.page(),
          query.perPage(),
          Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

       final var specification = Optional.ofNullable(query.terms())
          .filter(term -> !term.isBlank())
          .map(term -> SpecificationUtils.<CategoryJpaEntity>like("name", term)
                .or(like("description", term))
          )
          .orElse(null);

       final var pageResult = repository.findAll(Specification.where(specification), page);

        return new Pagination<>(
          pageResult.getNumber(),
          pageResult.getSize(),
          pageResult.getTotalElements(),
          pageResult.map(CategoryJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public void delete(final CategoryID id) {
        repository.deleteById(id.getValue());
    }
}
