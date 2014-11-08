package org.mapstruct.ap.test.array;

import static org.fest.assertions.Assertions.assertThat;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mapstruct.ap.test.array.source.Scientist;
import org.mapstruct.ap.test.array.target.ScientistDto;
import org.mapstruct.ap.testutil.IssueKey;
import org.mapstruct.ap.testutil.WithClasses;
import org.mapstruct.ap.testutil.runner.AnnotationProcessorTestRunner;

@WithClasses( { Scientist.class, ScientistDto.class, ScienceMapper.class } )
@RunWith(AnnotationProcessorTestRunner.class)
@IssueKey("108")
public class ArrayMappingTest {

    @Test
    public void shouldMapArrayToArray() {
        ScientistDto[] dtos = ScienceMapper.INSTANCE
            .scientistsToDtos( new Scientist[]{ new Scientist( "Bob" ), new Scientist( "Larry" ) } );

        assertThat( dtos ).isNotNull();
        assertThat( dtos ).onProperty( "name" ).containsOnly( "Bob", "Larry" );
    }

    @Test
    public void shouldMapListToArray() {
        ScientistDto[] dtos = ScienceMapper.INSTANCE
            .scientistsToDtos( Arrays.asList( new Scientist( "Bob" ), new Scientist( "Larry" ) ) );

        assertThat( dtos ).isNotNull();
        assertThat( dtos ).onProperty( "name" ).containsOnly( "Bob", "Larry" );
    }

    @Test
    public void shouldMapArrayToList() {
        List<ScientistDto> dtos = ScienceMapper.INSTANCE
            .scientistsToDtosAsList( new Scientist[]{ new Scientist( "Bob" ), new Scientist( "Larry" ) } );

        assertThat( dtos ).isNotNull();
        assertThat( dtos ).onProperty( "name" ).containsOnly( "Bob", "Larry" );
    }
}
