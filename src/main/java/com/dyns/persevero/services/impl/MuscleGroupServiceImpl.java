package com.dyns.persevero.services.impl;

import com.dyns.persevero.domain.model.impl.MuscleGroup;
import com.dyns.persevero.enums.MuscleGroupName;
import com.dyns.persevero.exceptions.ResourceNotFoundException;
import com.dyns.persevero.repositories.MuscleGroupRepository;
import com.dyns.persevero.services.MessageService;
import com.dyns.persevero.services.MuscleGroupService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MuscleGroupServiceImpl implements MuscleGroupService {

    private final MuscleGroupRepository muscleGroupRepository;

    @Autowired
    private MessageService messageService;

    @Override
    public MuscleGroup findByName(MuscleGroupName name) {
        return muscleGroupRepository.findByName(name)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage("errors.muscle_group.not_found_with_name", name)
                ));
    }

    @Override
    public MuscleGroup save(MuscleGroup entity) {
        if (!isPersisted(entity.getId())) {
            throw new ResourceNotFoundException(
                    messageService.getMessage("errors.muscle_group.not_found_with_id", entity.getId())
            );
        }

        return muscleGroupRepository.save(entity);
    }

    @Override
    public Iterable<MuscleGroup> findAll() {
        return muscleGroupRepository.findAll();
    }

    @Override
    public MuscleGroup findOne(UUID uuid) {
        return muscleGroupRepository.findById(uuid)
                .orElseThrow(() -> new ResourceNotFoundException(
                        messageService.getMessage("errors.muscle_group.not_found_with_id", uuid)
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
                        messageService.getMessage("errors.muscle_group.not_found_with_id", uuid)
                ));
    }

    @Override
    public void delete(UUID uuid) {
        muscleGroupRepository.findById(uuid)
                .ifPresentOrElse(
                        muscleGroupRepository::delete,
                        () -> {
                            throw new ResourceNotFoundException(
                                    messageService.getMessage("errors.muscle_group.not_found_with_id", uuid)
                            );
                        }
                );
    }

    @Override
    public boolean isPersisted(UUID uuid) {
        return muscleGroupRepository.existsById(uuid);
    }
}
