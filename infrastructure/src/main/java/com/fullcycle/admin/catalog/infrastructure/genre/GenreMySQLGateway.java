package com.fullcycle.admin.catalog.infrastructure.genre;

import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.genre.Genre;
import com.fullcycle.admin.catalog.domain.genre.GenreGateway;
import com.fullcycle.admin.catalog.domain.genre.GenreID;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import com.fullcycle.admin.catalog.infrastructure.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@Component
@RequiredArgsConstructor
public class GenreMySQLGateway implements GenreGateway {

    private final GenreRepository genreRepository;

    @Override
    public Genre create(final Genre genre) {
        return save(genre);
    }

    @Override
    public Genre update(final Genre genre) {
        return save(genre);
    }

    @Override
    public Optional<Genre> findById(final GenreID id) {
        return genreRepository.findById(id.getValue()).map(GenreJpaEntity::toAggregate);
    }

    @Override
    public Pagination<Genre> finalAll(final SearchQuery query) {
        final var page = PageRequest.of(
          query.page(),
          query.perPage(),
          Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var where = Optional.ofNullable(query.terms())
          .filter(str -> !str.isBlank())
          .map(this::assembleSpecification)
          .orElse(null);

        final var results = genreRepository.findAll(Specification.where(where), page);

        return new Pagination<>(
          results.getNumber(),
          results.getSize(),
          results.getTotalElements(),
          results.map(GenreJpaEntity::toAggregate).toList()
        );
    }

    @Override
    public List<GenreID> existsByIds(Iterable<GenreID> genreIds) {
        final var ids = StreamSupport.stream(genreIds.spliterator(), false)
          .map(GenreID::getValue)
          .toList();
        return genreRepository.existsByIds(ids).stream()
          .map(GenreID::from)
          .toList();
    }

    @Override
    public void deleteById(final GenreID id) {
        final var genreId = id.getValue();

        if (genreRepository.existsById(genreId)) {
            genreRepository.deleteById(id.getValue());
        }
    }

    private Genre save(final Genre genre) {
        return genreRepository.save(GenreJpaEntity.from(genre))
          .toAggregate();
    }

    private Specification<GenreJpaEntity> assembleSpecification(final String terms) {
        return SpecificationUtils.like("name", terms);
    }
}
