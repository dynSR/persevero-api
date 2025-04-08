package com.dyns.persevero.repositories.impl;

import com.dyns.persevero.domain.model.Model;
import com.dyns.persevero.fixtures.Fixture;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.ParameterizedType;
import java.util.Collection;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * An abstract class used to implement basic integration tests;
 * And all necessary components.
 *
 * @param <REPOSITORY> The type of repository that extends CrudRepository
 * @param <ENTITY>     The type of entity managed by the repository that extends Model
 * @param <FIXTURE>    The type of fixture used to mock data that extends Fixture
 * @param <ID>         The type of ID used by both the entity and the repository
 * @param <NAME>       The type of name used by the entity to differentiate between String and enum
 * @see CrudRepository
 * @see Model
 * @see Fixture
 */
@Slf4j
@SpringBootTest
@ExtendWith(SpringExtension.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public abstract class AbstractRepositoryIntegrationTests<
        REPOSITORY extends CrudRepository<ENTITY, ID>,
        ENTITY extends Model<ID, NAME>,
        FIXTURE extends Fixture<ENTITY>,
        ID,
        NAME
        > {

    @Autowired
    protected REPOSITORY underTest;

    protected FIXTURE fixture = null;

    protected abstract FIXTURE getFixture();

    public abstract void setDependencies();

    protected String getFailureMessage() {
        return String.format("Expected %s to be found, but none was !", getEntityTypeName());
    }

    protected String getFailureMessage(String entityName) {
        return String.format("Expected %s to be found, but none was !", entityName);
    }

    @SuppressWarnings({"unchecked"})
    private String getEntityTypeName() {
        return ((Class<ENTITY>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1])
                .getSimpleName()
                .toLowerCase();
    }

    @Test
    @DisplayName("Should save and retrieve entity")
    public void givenValidEntity_whenSaved_thenIsPersisted() {
        // GIVEN
        ENTITY entity = getFixture().getOne();

        // WHEN
        ENTITY savedEntity = underTest.save(entity);
        log.info("Saved entity : {}", savedEntity);

        // THEN
        assertThat(entity).isEqualTo(savedEntity);
        assertThat(underTest.findById(savedEntity.getId())).isPresent();
    }

    // TODO - Change this test to be able to test against any unique entity property
    @Test
    @DisplayName("Should throw exception when providing duplicate entities")
    public void givenEntitiesWithSameName_whenSaved_ThenThrowDataIntegrityException() {
        // GIVEN
        ENTITY entity = getFixture().getOne();
        ENTITY duplicateEntity = getFixture().getOne();
        duplicateEntity.setName(entity.getName());

        log.info(
                "Duplication with : {} | {}",
                entity.getName(),
                duplicateEntity.getName()
        );
        assertThat(entity.getName()).isEqualTo(duplicateEntity.getName());

        // WHEN
        underTest.save(entity);
        try {
            underTest.save(duplicateEntity);
        } catch (DataIntegrityViolationException exception) {
            // THEN
            assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
        }
    }

    @Test
    @DisplayName("Should retrieve a list of entities")
    public void givenEntities_whenQueryingAll_thenReturnsListOfEntities() {
        // GIVEN
        List<ENTITY> allEntities = List.copyOf(
                (Collection<? extends ENTITY>) underTest.findAll()
        );
        log.info("All entities : {}", allEntities);

        // THEN
        assertThat(allEntities).isNotNull();
        assertThat(allEntities.size()).isNotZero();
    }

    @Test
    @DisplayName("Should delete entity")
    public void givenSavedEntity_whenDeleted_IsDeleted() {
        // GIVEN
        ENTITY savedEntity = underTest.save(getFixture().getOne());
        log.info("Deleted entity : {}", savedEntity);

        // WHEN
        underTest.delete(savedEntity);

        // THEN
        assertThat(underTest.findById(savedEntity.getId())).isEmpty();
    }
}
