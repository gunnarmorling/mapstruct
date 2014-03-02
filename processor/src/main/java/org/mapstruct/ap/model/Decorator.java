package org.mapstruct.ap.model;

import java.util.Arrays;
import java.util.List;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;

import org.mapstruct.ap.model.common.Accessibility;
import org.mapstruct.ap.model.common.ModelElement;
import org.mapstruct.ap.model.common.Type;
import org.mapstruct.ap.model.common.TypeFactory;
import org.mapstruct.ap.prism.DecoratedWithPrism;

public class Decorator extends GeneratedType {

    private static final String IMPLEMENTATION_SUFFIX = "Impl";

    private Decorator(TypeFactory typeFactory, String packageName, String name, String superClassName,
                      String interfaceName, List<MappingMethod> methods, List<? extends ModelElement> fields,
                      boolean suppressGeneratorTimestamp, Accessibility accessibility) {
        super(
            typeFactory,
            packageName,
            name,
            superClassName,
            interfaceName,
            methods,
            fields,
            suppressGeneratorTimestamp,
            accessibility
        );
    }

    public static Decorator getInstance(Elements elementUtils, TypeFactory typeFactory, TypeElement mapperElement,
                                        DecoratedWithPrism decoratorPrism, List<MappingMethod> methods,
                                        boolean suppressGeneratorTimestamp) {
        Type decoratorType = typeFactory.getType( decoratorPrism.value() );

        return new Decorator(
            typeFactory,
            elementUtils.getPackageOf( mapperElement ).getQualifiedName().toString(),
            mapperElement.getSimpleName().toString() + IMPLEMENTATION_SUFFIX,
            decoratorType.getName(),
            mapperElement.getKind() == ElementKind.INTERFACE ? mapperElement.getSimpleName().toString() : null,
            methods,
            Arrays.asList(
                new MapperReference( typeFactory.getType( mapperElement ), "delegate" ),
                new DecoratorConstructor(
                    mapperElement.getSimpleName().toString() + IMPLEMENTATION_SUFFIX,
                    mapperElement.getSimpleName().toString() + "Impl_"
                )
            ),
            suppressGeneratorTimestamp,
            Accessibility.fromModifiers( mapperElement.getModifiers() )
        );
    }

    @Override
    protected String getTemplateName() {
        return GeneratedType.class.getName() + ".ftl";
    }
}
