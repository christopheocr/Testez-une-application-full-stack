package com.openclassrooms.starterjwt.mapper;

import lombok.Getter;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;


public class EntityMapperTest {

    @Getter
    static class DummyEntity {
        Long id;
        DummyEntity(Long id) { this.id = id; }
    }

    @Getter
    static class DummyDto {
        Long id;
        DummyDto(Long id) { this.id = id; }
    }

    static class DummyMapper implements EntityMapper<DummyDto, DummyEntity> {
        public DummyEntity toEntity(DummyDto dto) {
            return new DummyEntity(dto.getId());
        }

        public DummyDto toDto(DummyEntity entity) {
            return new DummyDto(entity.getId());
        }

        public List<DummyEntity> toEntity(List<DummyDto> dtoList) {
            return dtoList.stream().map(this::toEntity).collect(Collectors.toList());
        }

        public List<DummyDto> toDto(List<DummyEntity> entityList) {
            return entityList.stream().map(this::toDto).collect(Collectors.toList());
        }
    }

    @Test
    void testMapperMethods() {
        DummyMapper mapper = new DummyMapper();

        DummyDto dto = new DummyDto(1L);
        DummyEntity entity = mapper.toEntity(dto);
        assertEquals(1L, entity.getId());

        DummyDto dtoBack = mapper.toDto(entity);
        assertEquals(1L, dtoBack.getId());

        List<DummyEntity> entities = mapper.toEntity(List.of(dto));
        assertEquals(1, entities.size());
        assertEquals(1L, entities.get(0).getId());

        List<DummyDto> dtos = mapper.toDto(entities);
        assertEquals(1, dtos.size());
        assertEquals(1L, dtos.get(0).getId());
    }
}
