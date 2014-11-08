package org.mapstruct.ap.test.array;

import java.util.List;

import org.mapstruct.Mapper;
import org.mapstruct.ap.test.array.source.Scientist;
import org.mapstruct.ap.test.array.target.ScientistDto;
import org.mapstruct.factory.Mappers;

@Mapper
public interface ScienceMapper {

    ScienceMapper INSTANCE = Mappers.getMapper( ScienceMapper.class );

    ScientistDto[] scientistsToDtos(Scientist[] scientists);

    ScientistDto[] scientistsToDtos(List<Scientist> scientists);

    List<ScientistDto> scientistsToDtosAsList(Scientist[] scientists);

    ScientistDto scientistToDto(Scientist scientist);
}
