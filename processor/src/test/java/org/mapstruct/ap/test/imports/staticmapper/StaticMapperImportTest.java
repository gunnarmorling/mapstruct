/**
 *  Copyright 2012-2016 Gunnar Morling (http://www.gunnarmorling.de/)
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
package org.mapstruct.ap.test.imports.staticmapper;

import static org.fest.assertions.Assertions.assertThat;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.ap.test.imports.staticmapper.other.PlushGiraffe;
import org.mapstruct.ap.test.imports.staticmapper.other.PlushGiraffeDto;
import org.mapstruct.ap.test.imports.staticmapper.other.PlushGiraffeMapper;
import org.mapstruct.ap.testutil.IssueKey;
import org.mapstruct.ap.testutil.WithClasses;
import org.mapstruct.ap.testutil.runner.AnnotationProcessorTestRunner;
import org.mapstruct.ap.testutil.runner.GeneratedSource;

/**
 * Test for generating a mapper which references another mapper located in another package, declaring a static method.
 *
 * @author Gunnar Morling
 */
@WithClasses({
    ZooMapper.class, Zoo.class, ZooDto.class, PlushGiraffe.class, PlushGiraffeDto.class, PlushGiraffeMapper.class
})
@RunWith(AnnotationProcessorTestRunner.class)
public class StaticMapperImportTest {

    private final GeneratedSource generatedSource = new GeneratedSource();

    @Rule
    public GeneratedSource getGeneratedSource() {
        return generatedSource;
    }

    @Test
    @IssueKey( "730" )
    public void mapperRequiresInnerClassImports() {
        PlushGiraffe giraffe = new PlushGiraffe();
        giraffe.setNeckLength( 430 );

        Zoo zoo = new Zoo();
        zoo.setHeadOfZoo( giraffe );

        ZooDto target = ZooMapper.INSTANCE.zooToDto( zoo );

        assertThat( target ).isNotNull();
        assertThat( target.getHeadOfZoo().getNeckLength() ).isEqualTo( 430 );
        generatedSource.forMapper( ZooMapper.class ).containsImportFor( PlushGiraffeMapper.class );
    }
}
