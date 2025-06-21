package com.fullcycle.admin.catalog.infrastructure.configuration.usecases;

import com.fullcycle.admin.catalog.application.castmember.create.CreateCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.create.DefaultCreateCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.delete.DefaultDeleteCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.delete.DeleteCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.retrieve.get.DefaultGetCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.retrieve.get.GetCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.retrieve.list.DefaultListCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.retrieve.list.ListCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.update.DefaultUpdateCastMemberUseCase;
import com.fullcycle.admin.catalog.application.castmember.update.UpdateCastMemberUseCase;
import com.fullcycle.admin.catalog.domain.castmember.CastMemberGateway;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@RequiredArgsConstructor
@Configuration
public class CastMemberUseCaseConfig {

    private final CastMemberGateway castMemberGateway;

    @Bean
    public CreateCastMemberUseCase createCastMemberUseCase() {
        return new DefaultCreateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public UpdateCastMemberUseCase updateCastMemberUseCase() {
        return new DefaultUpdateCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public GetCastMemberUseCase getCastMemberByIdUseCase() {
        return new DefaultGetCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public ListCastMemberUseCase listCastMemberUseCase() {
        return new DefaultListCastMemberUseCase(castMemberGateway);
    }

    @Bean
    public DeleteCastMemberUseCase deleteCastMemberByIdUseCase() {
        return new DefaultDeleteCastMemberUseCase(castMemberGateway);
    }
}
