package com.dyns.persevero.services.impl;

import com.dyns.persevero.domain.model.impl.Exercise;
import com.dyns.persevero.domain.model.impl.Muscle;
import com.dyns.persevero.exceptions.ResourceNotFoundException;
import com.dyns.persevero.repositories.ExerciseRepository;
import com.dyns.persevero.repositories.MuscleRepository;
import com.dyns.persevero.services.ExerciseService;
import com.dyns.persevero.services.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
@RequiredArgsConstructor
public class ExerciseServiceImpl implements ExerciseService {

    private final ExerciseRepository exerciseRepository;
    private final MuscleRepository muscleRepository;
    private final MessageService messageService;

    @Override
    @Transactional
    public Exercise save(Exercise exercise) {
        return exerciseRepository.save(exercise);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Exercise> findAll() {
        return exerciseRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Exercise findOne(UUID uuid) {
        return exerciseRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFound",
                                Exercise.class.getSimpleName(),
                                uuid
                        )
                ));
    }

    @Override
    @Transactional
    public Exercise fullUpdate(UUID uuid, Exercise exercise) {
        return exerciseRepository.findById(uuid)
                .map(foundExercise -> {
                    foundExercise.setName(exercise.getName());
                    foundExercise.setDescription(exercise.getDescription());
                    foundExercise.setSets(exercise.getSets());
                    foundExercise.setReps(exercise.getReps());
                    foundExercise.setWeight(exercise.getWeight());
                    return exerciseRepository.save(foundExercise);
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFound",
                                Exercise.class.getSimpleName(),
                                uuid
                        )
                ));
    }

    @Override
    @Transactional
    public Exercise partialUpdate(UUID uuid, Exercise exercise) {
        return exerciseRepository.findById(uuid)
                .map(foundExercise -> {
                    Optional.ofNullable(exercise.getName()).ifPresent(foundExercise::setName);
                    Optional.ofNullable(exercise.getDescription()).ifPresent(foundExercise::setDescription);
                    Optional.ofNullable(exercise.getSets()).ifPresent(foundExercise::setSets);
                    Optional.ofNullable(exercise.getReps()).ifPresent(foundExercise::setReps);
                    Optional.ofNullable(exercise.getWeight()).ifPresent(foundExercise::setWeight);
                    Optional.ofNullable(exercise.getMuscles()).ifPresent(foundExercise::setMuscles);
                    return exerciseRepository.save(foundExercise);
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFound",
                                Exercise.class.getSimpleName(),
                                uuid
                        )
                ));
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        exerciseRepository.findById(uuid).ifPresentOrElse(
                exerciseRepository::delete,
                () -> {
                    throw new ResourceNotFoundException(
                            messageService.getMessage(
                                    "errors.entity.not_found",
                                    Exercise.class.getSimpleName(),
                                    uuid
                            )
                    );
                }
        );
    }

    @Override
    public boolean isPersisted(UUID uuid) {
        return exerciseRepository.existsById(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Exercise findByName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException(
                    messageService.getMessage("error.exercise.name.required", name)
            );
        }

        return exerciseRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFoundWithName",
                                Exercise.class.getSimpleName(),
                                name
                        )
                ));
    }

    @Override
    public void addMuscle(UUID exerciseId, UUID muscleId) {
        Exercise existingExercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFoundWithId",
                                Exercise.class.getSimpleName(),
                                exerciseId
                        )
                ));
        Muscle existingMuscle = muscleRepository.findById(muscleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFoundWithId",
                                Muscle.class.getSimpleName(),
                                muscleId
                        )
                ));

        existingExercise.addMuscle(existingMuscle);
        exerciseRepository.save(existingExercise);
    }

    @Override
    public void removeMuscle(UUID exerciseId, UUID muscleId) {
        Exercise existingExercise = exerciseRepository.findById(exerciseId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFoundWithId",
                                Exercise.class.getSimpleName(),
                                exerciseId
                        )
                ));
        Muscle existingMuscle = muscleRepository.findById(muscleId)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFoundWithId",
                                Muscle.class.getSimpleName(),
                                muscleId
                        )
                ));

        existingExercise.removeMuscle(existingMuscle);
        exerciseRepository.save(existingExercise);
    }

    @Override
    @Transactional
    public void addMuscles(UUID uuid, Iterable<UUID> muscleIds) {
        Exercise existingExercise = exerciseRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFoundWithId",
                                Exercise.class.getSimpleName(),
                                uuid
                        )
                ));

        Set<Muscle> existingMuscles = StreamSupport.stream(
                        muscleRepository.findAllById(muscleIds).spliterator(),
                        false
                )
                .collect(Collectors.toSet());

        existingMuscles.forEach(existingExercise::addMuscle);
        Exercise savedExercise = exerciseRepository.save(existingExercise);
    }

    @Override
    @Transactional
    public void removeMuscles(UUID uuid, Iterable<UUID> muscleIds) {
        Exercise existingExercise = exerciseRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFoundWithId",
                                Exercise.class.getSimpleName(),
                                uuid
                        )
                ));

        Set<Muscle> existingMuscles = StreamSupport.stream(
                        muscleRepository.findAllById(muscleIds).spliterator(),
                        false
                )
                .collect(Collectors.toSet());

        existingMuscles.forEach(existingExercise::removeMuscle);
        exerciseRepository.save(existingExercise);
    }
}
