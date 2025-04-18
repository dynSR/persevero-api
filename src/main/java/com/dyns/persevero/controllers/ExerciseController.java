package com.dyns.persevero.controllers;

import com.dyns.persevero.domain.dto.ExerciseDto;
import com.dyns.persevero.domain.model.impl.Exercise;
import com.dyns.persevero.mappers.ExerciseMapper;
import com.dyns.persevero.services.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/v1/exercises")
@RequiredArgsConstructor
public class ExerciseController {

    private final ExerciseService exerciseService;
    private final ExerciseMapper mapper;

    @GetMapping
    public ResponseEntity<List<ExerciseDto>> index() {
        List<ExerciseDto> exercises = StreamSupport.stream(
                        exerciseService.findAll().spliterator(), false
                ).map(mapper::toDto)
                .toList();
        return ResponseEntity.ok(exercises);
    }

    @GetMapping(path = "/{uuid}")
    public ResponseEntity<ExerciseDto> read(@PathVariable("uuid") UUID uuid) {
        return ResponseEntity.ok(mapper.toDto(exerciseService.findOne(uuid)));
    }

    @PostMapping
    public ResponseEntity<ExerciseDto> create(@RequestBody @Validated ExerciseDto exerciseDto) {
        Exercise exerciseEntity = mapper.toEntity(exerciseDto);
        Exercise savedEntity = exerciseService.save(exerciseEntity);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(mapper.toDto(savedEntity));
    }

    @PutMapping(path = "/{uuid}")
    public ResponseEntity<ExerciseDto> edit(
            @PathVariable("uuid") UUID uuid,
            @RequestBody ExerciseDto exerciseDto
    ) {
        Exercise exerciseToUpdate = mapper.toEntity(exerciseDto);
        Exercise updatedExercise = exerciseService.fullUpdate(uuid, exerciseToUpdate);
        return ResponseEntity.ok(mapper.toDto(updatedExercise));
    }

    @PatchMapping(path = "/{uuid}")
    public ResponseEntity<ExerciseDto> patch(
            @PathVariable("uuid") UUID uuid,
            @RequestBody @Validated ExerciseDto exerciseDto
    ) {
        Exercise exerciseEntity = mapper.toEntity(exerciseDto);
        Exercise updatedExercise = exerciseService.partialUpdate(uuid, exerciseEntity);
        return ResponseEntity.ok(mapper.toDto(updatedExercise));
    }

    @DeleteMapping(path = "/{uuid}")
    public ResponseEntity<Void> remove(@PathVariable("uuid") UUID uuid) {
        exerciseService.delete(uuid);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{exerciseId}/muscles/{muscleId}")
    public ResponseEntity<Void> addMuscle(
            @PathVariable("exerciseId") UUID exerciseId,
            @PathVariable("muscleId") UUID muscleId
    ) {
        exerciseService.addMuscle(exerciseId, muscleId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{exerciseId}/muscles/{muscleId}")
    public ResponseEntity<Void> removeMuscle(
            @PathVariable("exerciseId") UUID exerciseId,
            @PathVariable("muscleId") UUID muscleId
    ) {
        exerciseService.removeMuscle(exerciseId, muscleId);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{uuid}/muscles")
    public ResponseEntity<Void> addMuscles(
            @PathVariable("uuid") UUID uuid,
            @RequestBody Set<UUID> muscleIds
    ) {
        exerciseService.addMuscles(uuid, muscleIds);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{uuid}/muscles")
    public ResponseEntity<Void> removeMuscles(
            @PathVariable("uuid") UUID uuid,
            @RequestBody Set<UUID> muscleIds
    ) {
        exerciseService.removeMuscles(uuid, muscleIds);
        return ResponseEntity.noContent().build();
    }
}
