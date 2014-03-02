package org.mapstruct.ap.model;

import java.util.Collections;
import java.util.Set;

import org.mapstruct.ap.model.common.ModelElement;
import org.mapstruct.ap.model.common.Type;

public class DecoratorConstructor extends ModelElement {

    private final String name;
    private final String delegateName;

    public DecoratorConstructor(String name, String delegateName) {
        this.name = name;
        this.delegateName = delegateName;
    }

    @Override
    public Set<Type> getImportTypes() {
        return Collections.emptySet();
    }

    public String getName() {
        return name;
    }

    public String getDelegateName() {
        return delegateName;
    }
}
