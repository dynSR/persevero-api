package com.dyns.persevero.services.impl;

import com.dyns.persevero.domain.model.impl.Exercise;
import com.dyns.persevero.domain.model.impl.Muscle;
import com.dyns.persevero.domain.model.impl.MuscleGroup;
import com.dyns.persevero.enums.MuscleName;
import com.dyns.persevero.exceptions.ResourceNotFoundException;
import com.dyns.persevero.repositories.ExerciseRepository;
import com.dyns.persevero.repositories.MuscleRepository;
import com.dyns.persevero.services.MessageService;
import com.dyns.persevero.services.MuscleService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MuscleServiceImpl implements MuscleService {

    private final MuscleRepository muscleRepository;
    private final ExerciseRepository exerciseRepository;
    private final MessageService messageService;

    @Override
    @Transactional
    public Muscle save(Muscle muscle) {
        return muscleRepository.save(muscle);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<Muscle> findAll() {
        return muscleRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public Muscle findOne(UUID uuid) {
        return muscleRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFound",
                                Muscle.class.getSimpleName(),
                                uuid
                        )
                ));
    }

    @Override
    @Transactional
    public Muscle fullUpdate(UUID uuid, Muscle muscle) {
        return muscleRepository.findById(uuid)
                .map(foundMuscle -> {
                   foundMuscle.setName(muscle.getName());
                   foundMuscle.setDescription(muscle.getDescription());
                    return muscleRepository.save(foundMuscle);
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFound",
                                Muscle.class.getSimpleName(),
                                uuid
                        )
                ));
    }

    @Override
    @Transactional
    public Muscle partialUpdate(UUID uuid, Muscle muscle) {
        return muscleRepository.findById(uuid)
                .map(foundMuscle -> {
                    Optional.ofNullable(muscle.getName()).ifPresent(foundMuscle::setName);
                    Optional.ofNullable(muscle.getDescription()).ifPresent(foundMuscle::setDescription);
                    Optional.ofNullable(muscle.getMuscleGroup()).ifPresent(foundMuscle::setMuscleGroup);
                    Optional.ofNullable(muscle.getExercises()).ifPresent(foundMuscle::setExercises);
                    return muscleRepository.save(foundMuscle);
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFound",
                                Muscle.class.getSimpleName(),
                                uuid
                        )
                ));
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        muscleRepository.findById(uuid).ifPresentOrElse(
                muscleRepository::delete,
                () -> {
                    throw new ResourceNotFoundException(
                            messageService.getMessage(
                                    "errors.entity.not_found",
                                    Muscle.class.getSimpleName(),
                                    uuid
                            )
                    );
                }
        );

    }

    @Override
    public boolean isPersisted(UUID uuid) {
        return muscleRepository.existsById(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public Muscle findByName(MuscleName name) {
        return muscleRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFoundWithName",
                                Muscle.class.getSimpleName(),
                                name
                        )
                ));
    }

    @Override
    @Transactional
    public Iterable<Muscle> findAllByExerciseId(UUID exerciseId) {
        if (!exerciseRepository.existsById(exerciseId)) throw new RuntimeException(
                messageService.getMessage(
                        "error.entity.notFound",
                        Exercise.class.getSimpleName(),
                        exerciseId
                )
        );

        return muscleRepository.findAllByExerciseId(exerciseId);
    }
}
