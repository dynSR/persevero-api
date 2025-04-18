package com.dyns.persevero.services.impl;

import com.dyns.persevero.domain.model.impl.Exercise;
import com.dyns.persevero.domain.model.impl.MuscleGroup;
import com.dyns.persevero.enums.MuscleGroupName;
import com.dyns.persevero.exceptions.ResourceNotFoundException;
import com.dyns.persevero.repositories.MuscleGroupRepository;
import com.dyns.persevero.services.MessageService;
import com.dyns.persevero.services.MuscleGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MuscleGroupServiceImpl implements MuscleGroupService {

    private final MuscleGroupRepository muscleGroupRepository;
    private final MessageService messageService;

    @Override
    @Transactional
    public MuscleGroup save(MuscleGroup muscleGroup) {
        return muscleGroupRepository.save(muscleGroup);
    }

    @Override
    @Transactional(readOnly = true)
    public Iterable<MuscleGroup> findAll() {
        return muscleGroupRepository.findAll();
    }

    @Override
    @Transactional(readOnly = true)
    public MuscleGroup findOne(UUID uuid) {
        return muscleGroupRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "errors.entity.not_found",
                                MuscleGroup.class.getSimpleName(),
                                uuid
                        )
                ));
    }

    @Override
    public MuscleGroup fullUpdate(UUID uuid, MuscleGroup muscleGroup) {
        return muscleGroupRepository.findById(uuid)
                .map(foundMuscleGroup -> {
                    foundMuscleGroup.setName(muscleGroup.getName());
                    return muscleGroupRepository.save(foundMuscleGroup);
                })
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "error.entity.notFound",
                                MuscleGroup.class.getSimpleName(),
                                uuid
                        )
                ));
    }

    @Override
    public MuscleGroup partialUpdate(UUID uuid, MuscleGroup muscleGroup) {
        return muscleGroupRepository.findById(uuid)
                .map(
                        mg -> {
                            Optional.ofNullable(muscleGroup.getName()).ifPresent(mg::setName);
                            Optional.ofNullable(muscleGroup.getMuscles()).ifPresent(mg::setMuscles);
                            return muscleGroupRepository.save(mg);
                        }
                )
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "errors.entity.not_found",
                                MuscleGroup.class.getSimpleName(),
                                uuid
                        )
                ));
    }

    @Override
    @Transactional
    public void delete(UUID uuid) {
        muscleGroupRepository.findById(uuid).ifPresentOrElse(
                muscleGroupRepository::delete,
                () -> {
                    throw new ResourceNotFoundException(
                            messageService.getMessage(
                                    "errors.entity.not_found",
                                    MuscleGroup.class.getSimpleName(),
                                    uuid
                            )
                    );
                }
        );
    }

    @Override
    public boolean isPersisted(UUID uuid) {
        return muscleGroupRepository.existsById(uuid);
    }

    @Override
    @Transactional(readOnly = true)
    public MuscleGroup findByName(MuscleGroupName name) {
        return muscleGroupRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage(
                                "errors.entity.not_found_with_name",
                                MuscleGroup.class.getSimpleName(),
                                name
                        )
                ));
    }
}
