package com.fullcycle.admin.catalog;

import com.fullcycle.admin.catalog.infrastructure.category.persistence.CategoryRepository;
import com.fullcycle.admin.catalog.infrastructure.genre.persistence.GenreRepository;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.Collection;
import java.util.List;

public class CleanUpExtension implements BeforeEachCallback {

    @Override
    public void beforeEach(ExtensionContext context) {
        final var ctx = SpringExtension.getApplicationContext(context);

        cleanUp(List.of(
          ctx.getBean(GenreRepository.class),
          ctx.getBean(CategoryRepository.class)
        ));
    }

    private void cleanUp(final Collection<CrudRepository> repositories) {
        repositories.forEach(CrudRepository::deleteAll);
    }
}