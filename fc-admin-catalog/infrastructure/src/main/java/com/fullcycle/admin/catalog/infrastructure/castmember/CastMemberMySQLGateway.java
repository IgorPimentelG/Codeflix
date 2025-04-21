package com.fullcycle.admin.catalog.infrastructure.castmember;

import com.fullcycle.admin.catalog.domain.castmember.CastMember;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberID;
import com.fullcycle.admin.catalog.domain.pagination.Pagination;
import com.fullcycle.admin.catalog.domain.pagination.SearchQuery;
import com.fullcycle.admin.catalog.infrastructure.castmember.persistence.CastMemberJpaEntity;
import com.fullcycle.admin.catalog.infrastructure.castmember.persistence.CastMemberRepository;
import com.fullcycle.admin.catalog.infrastructure.utils.SpecificationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class CastMemberMySQLGateway implements CastMemberGateway {

    private final CastMemberRepository castMemberRepository;

    @Override
    public CastMember create(CastMember castMember) {
        return save(castMember);
    }

    @Override
    public CastMember update(CastMember castMember) {
        return save(castMember);
    }

    @Override
    public void deleteById(CastMemberID id) {
        if (castMemberRepository.existsById(id.getValue())) {
            castMemberRepository.deleteById(id.getValue());
        }
    }

    @Override
    public Optional<CastMember> findById(CastMemberID id) {
        return castMemberRepository.findById(id.getValue()).map(CastMemberJpaEntity::toAggregate);
    }

    @Override
    public Pagination<CastMember> findAll(SearchQuery query) {
        final var page = PageRequest.of(
          query.page(),
          query.perPage(),
          Sort.by(Sort.Direction.fromString(query.direction()), query.sort())
        );

        final var where = Optional.ofNullable(query.terms())
          .filter(str -> !str.isBlank())
          .map(this::assembleSpecification)
          .orElse(null);

        final var result = castMemberRepository.findAll(where, page);
        return new Pagination<>(
          result.getNumber(),
          result.getSize(),
          result.getTotalElements(),
          result.map(CastMemberJpaEntity::toAggregate).toList()
        );
    }

    private CastMember save(CastMember castMember) {
        return castMemberRepository.save(CastMemberJpaEntity.from(castMember))
          .toAggregate();
    }

    private Specification<CastMemberJpaEntity> assembleSpecification(String terms) {
        return SpecificationUtils.like("name", terms);
    }
}
