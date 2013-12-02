/**
 *  Copyright 2012-2013 Gunnar Morling (http://www.gunnarmorling.de/)
 *  and/or other contributors as indicated by the @authors tag. See the
 *  copyright.txt file in the distribution for a full listing of all
 *  contributors.
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.mapstruct.ap.processor;

import java.util.List;
import javax.annotation.processing.Messager;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.util.ElementFilter;
import javax.lang.model.util.Types;
import javax.tools.Diagnostic.Kind;

import org.mapstruct.ap.model.BeanMappingMethod;
import org.mapstruct.ap.model.Mapper;
import org.mapstruct.ap.model.MappingMethod;
import org.mapstruct.ap.model.PropertyMapping;
import org.mapstruct.ap.model.Type;

/**
 * A {@link ModelElementProcessor} which performs several validations on the {@link MappingMethod}s hosted on the given
 * {@link Mapper}.
 *
 * @author Gunnar Morling
 */
public class MethodValidationProcessor implements ModelElementProcessor<Mapper, Mapper> {

    private Types typeUtils;
    private Messager messager;

    @Override
    public Mapper process(ProcessorContext context, TypeElement mapperTypeElement, Mapper sourceModel) {
        this.typeUtils = context.getTypeUtils();
        this.messager = context.getMessager();

        for ( MappingMethod mappingMethod : sourceModel.getMappingMethods() ) {
            if ( mappingMethod instanceof BeanMappingMethod ) {
                for ( PropertyMapping propertyMapping : ( (BeanMappingMethod) mappingMethod ).getPropertyMappings() ) {
                    reportErrorIfPropertyCanNotBeMapped( mappingMethod.getExecutable(), propertyMapping );
                }
            }

            //TODO #77 check for conversion / element mapping method for iterable / map mapping methods

            reportErrorIfNoImplementationTypeIsRegisteredForInterfaceReturnType( mappingMethod );
        }

        return sourceModel;
    }

    @Override
    public int getPriority() {
        return 1100;
    }

    /**
     * Reports an error if source the property can't be mapped from source to target. A mapping if possible if one of
     * the following conditions is true:
     * <ul>
     * <li>the source type is assignable to the target type</li>
     * <li>a mapping method exists</li>
     * <li>a built-in conversion exists</li>
     * <li>the property is of a collection or map type and the constructor of the target type (either itself or its
     * implementation type) accepts the source type.</li>
     * </ul>
     *
     * @param method The mapping method owning the property mapping.
     * @param property The property mapping to check.
     */
    private void reportErrorIfPropertyCanNotBeMapped(ExecutableElement executable, PropertyMapping property) {
        boolean collectionOrMapTargetTypeHasCompatibleConstructor = false;

        if ( property.getTargetType().isCollectionType() || property.getTargetType().isMapType() ) {
            if ( property.getTargetType().getImplementationType() != null ) {
                collectionOrMapTargetTypeHasCompatibleConstructor = hasCompatibleConstructor(
                    property.getSourceType(),
                    property.getTargetType().getImplementationType()
                );
            }
            else {
                collectionOrMapTargetTypeHasCompatibleConstructor = hasCompatibleConstructor(
                    property.getSourceType(),
                    property.getTargetType()
                );
            }
        }

        if ( property.getSourceType().isAssignableTo( property.getTargetType() ) ||
            property.getMappingMethod() != null ||
            property.getConversion() != null ||
            ( ( property.getTargetType().isCollectionType() || property.getTargetType().isMapType() ) &&
                collectionOrMapTargetTypeHasCompatibleConstructor ) ) {
            return;
        }

        messager.printMessage(
            Kind.ERROR,
            String.format(
                "Can't map property \"%s %s\" to \"%s %s\".",
                property.getSourceType(),
                property.getSourceName(),
                property.getTargetType(),
                property.getTargetName()
            ),
            executable
        );
    }

    /**
     * Whether the given target type has a single-argument constructor which accepts the given source type.
     *
     * @param sourceType the source type
     * @param targetType the target type
     *
     * @return {@code true} if the target type has a constructor accepting the given source type, {@code false}
     *         otherwise.
     */
    private boolean hasCompatibleConstructor(Type sourceType, Type targetType) {
        List<ExecutableElement> targetTypeConstructors = ElementFilter.constructorsIn(
            targetType.getTypeElement()
                .getEnclosedElements()
        );

        for ( ExecutableElement constructor : targetTypeConstructors ) {
            if ( constructor.getParameters().size() != 1 ) {
                continue;
            }

            //get the constructor resolved against the type arguments of specific target type
            ExecutableType typedConstructor = (ExecutableType) typeUtils.asMemberOf(
                (DeclaredType) targetType.getTypeMirror(), constructor
            );

            if ( typeUtils.isAssignable(
                sourceType.getTypeMirror(),
                typedConstructor.getParameterTypes().iterator().next()
            ) ) {
                return true;
            }
        }

        return false;
    }

    private void reportErrorIfNoImplementationTypeIsRegisteredForInterfaceReturnType(MappingMethod method) {
        if ( method.getReturnType().getTypeMirror().getKind() != TypeKind.VOID &&
            method.getReturnType().isInterface() &&
            method.getReturnType().getImplementationType() == null ) {
            messager.printMessage(
                Kind.ERROR,
                String.format(
                    "No implementation type is registered for return type %s.",
                    method.getReturnType()
                ),
                method.getExecutable()
            );
        }
    }
}
