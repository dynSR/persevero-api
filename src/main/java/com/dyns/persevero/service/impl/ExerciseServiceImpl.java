package com.dyns.persevero.service.impl;

import com.dyns.persevero.domain.model.Exercise;
import com.dyns.persevero.exception.ResourceNotFoundException;
import com.dyns.persevero.repository.ExerciseRepository;
import com.dyns.persevero.service.ExerciseService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;

    @Override
    public Exercise save(Exercise exercise) {
        // TODO: implements runtime exception
        if (!exerciseRepository.existsById(exercise.getId())) {
            throw new ResourceNotFoundException("");
        }

        return exerciseRepository.save(exercise);
    }

    @Override
    public Iterable<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    @Override
    public Exercise findOne(UUID uuid) {
        // TODO: implements runtime exception
        return exerciseRepository.findById(uuid)
                .orElseThrow(
                        () -> new ResourceNotFoundException("")
                );
    }

    @Override
    public Exercise partialUpdate(UUID uuid, Exercise exercise) {
        // TODO: implements runtime exception
        return exerciseRepository.findById(uuid)
                .map(e -> {
                    Optional.ofNullable(exercise.getName()).ifPresent(exercise::setName);
                    Optional.ofNullable(exercise.getDescription()).ifPresent(exercise::setDescription);
                    Optional.of(exercise.getSets()).ifPresent(exercise::setSets);
                    Optional.of(exercise.getReps()).ifPresent(exercise::setReps);
                    Optional.of(exercise.getWeight()).ifPresent(exercise::setWeight);

                    return exerciseRepository.save(e);
                })
                .orElseThrow(
                        () -> new ResourceNotFoundException("")
                );
    }

    @Override
    public void delete(UUID uuid) {
        // TODO: implements runtime exception
        if (!exerciseRepository.existsById(uuid)) throw new ResourceNotFoundException("");

        exerciseRepository.findById(uuid)
                .ifPresent(exerciseRepository::delete);
    }

    @Override
    public boolean exists(UUID uuid) {
        return exerciseRepository.existsById(uuid);
    }
}
