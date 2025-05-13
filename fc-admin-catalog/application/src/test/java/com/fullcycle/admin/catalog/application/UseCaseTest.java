package com.fullcycle.admin.catalog.application;

import com.fullcycle.admin.catalog.domain.Identifier;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@ExtendWith(MockitoExtension.class)
public abstract class UseCaseTest implements BeforeEachCallback {

    protected abstract List<Object> getMocks();

    @Override
    public void beforeEach(ExtensionContext context) throws Exception {
        Mockito.reset(getMocks().toArray());
    }

    protected List<String> asStrings(final List<? extends Identifier> objects) {
        return objects.stream()
            .map(item -> item.getValue().toString())
            .toList();
    }

    protected Set<String> asStrings(final Set<? extends Identifier> objects) {
        return objects.stream()
          .map(item -> item.getValue().toString())
          .collect(Collectors.toSet());
    }
}
