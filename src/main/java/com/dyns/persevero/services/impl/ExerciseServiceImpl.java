package com.dyns.persevero.services.impl;

import com.dyns.persevero.domain.model.impl.Exercise;
import com.dyns.persevero.exceptions.ResourceNotFoundException;
import com.dyns.persevero.repositories.ExerciseRepository;
import com.dyns.persevero.repositories.MuscleRepository;
import com.dyns.persevero.services.ExerciseService;
import com.dyns.persevero.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final MuscleRepository muscleRepository;

    @Autowired
    private MessageService messageService;

    @Override
    public Exercise save(Exercise exercise) {
        if (doesNotExist(exercise.getId())) {
            throw new ResourceNotFoundException(
                    messageService.getMessage("error.exercise.not_found_with_id", exercise.getId())
            );
        }

        return exerciseRepository.save(exercise);
    }

    @Override
    public Iterable<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    @Override
    public Exercise findOne(UUID uuid) {
        return exerciseRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage("error.exercise.not_found_with_id", uuid)
                ));
    }

    @Override
    public Exercise partialUpdate(UUID uuid, Exercise exercise) {
        return exerciseRepository.findById(uuid)
                .map(e -> {
                    Optional.ofNullable(exercise.getName()).ifPresent(e::setName);
                    Optional.ofNullable(exercise.getDescription()).ifPresent(e::setDescription);
                    Optional.of(exercise.getSets()).ifPresent(e::setSets);
                    Optional.of(exercise.getReps()).ifPresent(e::setReps);
                    Optional.of(exercise.getWeight()).ifPresent(e::setWeight);
                    Optional.ofNullable(exercise.getMuscles()).ifPresent(e::setMuscles);

                    return exerciseRepository.save(e);
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage("error.exercise.not_found_with_id", uuid)
                ));
    }

    @Override
    public void delete(UUID uuid) {
        exerciseRepository.findById(uuid)
                .ifPresentOrElse(
                        exerciseRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException(
                                    messageService.getMessage("errors.exercise.not_found_with_id", uuid)
                            );
                        }
                );
    }


    @Override
    public boolean doesNotExist(UUID uuid) {
        return !exerciseRepository.existsById(uuid);
    }

    @Override
    public Exercise findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    messageService.getMessage("error.argument.name_blank", name)
            );
        }

        return exerciseRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage("error.exercise.not_found", name)
                ));
    }

    @Override
    public Iterable<Exercise> findAllByMuscleId(UUID muscleId) {
        if (!muscleRepository.existsById(muscleId)) throw new RuntimeException(
                messageService.getMessage("error.muscle.not_found_with_id", muscleId)
        );

        return exerciseRepository.findAllByMuscleId(muscleId);
    }
}
