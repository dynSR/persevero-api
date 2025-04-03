package com.dyns.persevero.services.impl;

import com.dyns.persevero.domain.model.impl.Muscle;
import com.dyns.persevero.enums.MuscleName;
import com.dyns.persevero.exceptions.ResourceNotFoundException;
import com.dyns.persevero.repositories.ExerciseRepository;
import com.dyns.persevero.repositories.MuscleRepository;
import com.dyns.persevero.services.MessageService;
import com.dyns.persevero.services.MuscleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MuscleServiceImpl implements MuscleService {

    private final MuscleRepository muscleRepository;
    private final ExerciseRepository exerciseRepository;

    @Autowired
    private MessageService messageService;

    @Override
    public Muscle save(Muscle muscle) {
        if (doesNotExist(muscle.getId())) {
            throw new ResourceNotFoundException(
                    messageService.getMessage("error.muscle.not_found_with_id", muscle.getId())
            );
        }

        return muscleRepository.save(muscle);
    }

    @Override
    public Iterable<Muscle> findAll() {
        return muscleRepository.findAll();
    }

    @Override
    public Muscle findOne(UUID uuid) {
        return muscleRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage("error.muscle.not_found_with_id", uuid)
                ));
    }

    @Override
    public Muscle partialUpdate(UUID uuid, Muscle muscle) {
        return muscleRepository.findById(uuid)
                .map(m -> {
                    Optional.ofNullable(muscle.getName()).ifPresent(m::setName);
                    Optional.ofNullable(muscle.getDescription()).ifPresent(m::setDescription);
                    Optional.ofNullable(muscle.getMuscleGroup()).ifPresent(m::setMuscleGroup);
                    Optional.ofNullable(muscle.getExercises()).ifPresent(m::setExercises);

                    return muscleRepository.save(m);
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage("error.muscle.not_found_with_id", uuid)
                ));
    }

    @Override
    public void delete(UUID uuid) {
        muscleRepository.findById(uuid)
                .ifPresentOrElse(
                        muscleRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException(
                                    messageService.getMessage("errors.muscle.not_found_with_id", uuid)
                            );
                        }
                );

    }

    @Override
    public boolean doesNotExist(UUID uuid) {
        return !muscleRepository.existsById(uuid);
    }

    @Override
    public Muscle findByName(MuscleName name) {
        return muscleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage("error.exercise.not_found", name)
                ));
    }

    @Override
    public Iterable<Muscle> findAllByExerciseId(UUID exerciseId) {
        if (!exerciseRepository.existsById(exerciseId)) throw new RuntimeException(
                messageService.getMessage("error.exercise.not_found_with_id", exerciseId)
        );

        return muscleRepository.findAllByExerciseId(exerciseId);
    }
}
