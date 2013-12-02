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

import java.util.LinkedList;
import java.util.List;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;

import org.mapstruct.ap.MapperPrism;
import org.mapstruct.ap.model.DefaultMapperReference;
import org.mapstruct.ap.model.Mapper;
import org.mapstruct.ap.model.MapperReference;
import org.mapstruct.ap.model.MappingMethod;
import org.mapstruct.ap.util.TypeFactory;

/**
 * A {@link ModelElementProcessor} which creates a {@link Mapper} from the given list of {@link MappingMethod}s.
 *
 * @author Gunnar Morling
 */
public class MapperCreationProcessor implements ModelElementProcessor<List<MappingMethod>, Mapper> {

    private TypeFactory typeFactory;

    @Override
    public Mapper process(ProcessorContext context, TypeElement mapperTypeElement, List<MappingMethod> sourceModel) {
        this.typeFactory = context.getTypeFactory();

        List<MapperReference> mapperReferences = getReferencedMappers( mapperTypeElement );

        return new Mapper.Builder()
            .element( mapperTypeElement )
            .mappingMethods( sourceModel )
            .mapperReferences( mapperReferences )
            .options( context.getOptions() )
            .typeFactory( typeFactory )
            .elementUtils( context.getElementUtils() )
            .build();
    }

    @Override
    public int getPriority() {
        return 1000;
    }

    private List<MapperReference> getReferencedMappers(TypeElement element) {
        List<MapperReference> mapperReferences = new LinkedList<MapperReference>();
        MapperPrism mapperPrism = MapperPrism.getInstanceOn( element );

        for ( TypeMirror usedMapper : mapperPrism.uses() ) {
            mapperReferences.add( new DefaultMapperReference( typeFactory.getType( usedMapper ) ) );
        }

        return mapperReferences;
    }
}
