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

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * An abstract class used to implement basic integration tests;
 * And all necessary components.
 *
 * @param <REPO>    The type of repository that extends CrudRepository
 * @param <ENTITY>  The type of entity managed by the repository that extends Model
 * @param <FIXTURE> The type of fixture used to mock data that extends Fixture
 * @param <ID>      The type of ID used by both the entity and the repository
 * @param <NAME>    The type of name used by the entity to differentiate between String and enum
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
        REPO extends CrudRepository<ENTITY, ID>,
        ENTITY extends Model<ID, NAME>,
        FIXTURE extends Fixture<ENTITY>,
        ID,
        NAME
        > {

    /**
     * The repository that is currently under test within this class.
     */
    @Autowired
    protected REPO underTest;

    /**
     * The fixture instance used by this class throughout all tests.
     */
    protected FIXTURE fixture = null;

    protected final String failureMessageTemplate = "Expected %s to be found";

    /**
     * Assign the fixture within the repository integration tests class.
     *
     * @return The fixture or a new instance if none is found.
     */
    protected abstract FIXTURE getFixture();

    /**
     * Init the test class before each test.
     *
     * @implNote Must be annotated with {@code @BeforeEach} to be used before each test.
     */
    public abstract void init();

    /**
     * Basic save test :
     * <p> - Given an entity through the fixture
     * <p> - When saving the entity
     * <p> - Then asserts that the given entity and the persisted one are the same
     * <p> - Then asserts that the saved entity can be retrieved via the repository
     */
    @Test
    @DisplayName("Basic test - should save and retrieve entity")
    public void shouldSaveAndRetrievePersistedEntity() {
        ENTITY entity = getFixture().getOne();
        ENTITY savedEntity = underTest.save(entity);

        log.info("Saved entity : {}", savedEntity);

        assertThat(entity).isEqualTo(savedEntity);
        assertThat(underTest.findById(savedEntity.getId())).isPresent();
    }

    /**
     * Basic duplicate test on property {name} :
     * <p> - Given two entities with identical names
     * <p> - When saving both entities
     * <p> - Then asserts that both entities have the same name
     * <p> - Then asserts that query throws a data integrity violation exception
     */
    @Test
    @DisplayName("Basic test - should throw exception when providing duplicate entity")
    public void shouldThrowExceptionWhenDuplicateEntity() {
        ENTITY entity = getFixture().getOne();
        NAME name = entity.getName();

        ENTITY duplicateEntity = getFixture().getOne();
        duplicateEntity.setName(name);

        log.info(
                "Duplication with : {} | {}",
                entity.getName(),
                duplicateEntity.getName()
        );
        assertThat(entity.getName()).isEqualTo(duplicateEntity.getName());

        underTest.save(entity);
        try {
            underTest.save(duplicateEntity);
        } catch (DataIntegrityViolationException exception) {
            assertThat(exception).isInstanceOf(DataIntegrityViolationException.class);
        }
    }

    /**
     * Basic find all test :
     * <p> - Given a list of entities
     * <p> - When saved
     * <p> - Then asserts that all entities are retrieved and well persisted
     */
    @Test
    @DisplayName("Basic test - should retrieve all entities")
    public void shouldRetrieveAllEntities() {
        List<ENTITY> entities = getFixture().getMany();
        entities.forEach(underTest::save);

        List<ENTITY> allEntities = StreamSupport
                .stream(underTest.findAll().spliterator(), false)
                .collect(Collectors.toList());
        log.info("All entities : {}", allEntities);

        assertThat(allEntities).isNotNull();
        assertThat(allEntities.size()).isNotZero();
    }


    /**
     * Basic update test on property {name} :
     * <p> - Given an entity and a new name
     * <p> - When persisting updated property
     * <p> - Then asserts that the provided entity and the persisted one are equal
     * <p> - Then asserts that the updated entity has the new name value
     */
    @Test
    @DisplayName("Basic test - should update entity")
    @SuppressWarnings({"unchecked"})
    public void shouldUpdateEntity() {
        ENTITY entity = getFixture().getOne();
        log.info("Updated entity before : {}", entity);

        Object newName;
        if (entity.getNameClass().isEnum()) {
            Class<Enum> enumClass = (Class<Enum>) entity.getNameClass();
            newName = Enum.valueOf(enumClass, "NONE");
        } else {
            newName = "Updated value";
        }
        entity.setName((NAME) newName);

        ENTITY updatedEntity = underTest.save(entity);
        log.info("Updated entity after : {}", updatedEntity);

        assertThat(updatedEntity).isEqualTo(entity);
        assertThat(updatedEntity.getName()).isEqualTo(newName);
    }

    /**
     * Basic delete test :
     * <p> - Given one persisted entity
     * <p> - When deleting
     * <p> - Then asserts that this entity cannot be retrieved anymore
     */
    @Test
    @DisplayName("Basic test - should delete entity")
    public void shouldDeleteEntity() {
        ENTITY entity = getFixture().getOne();
        ENTITY savedEntity = underTest.save(entity);

        log.info("Deleted entity : {}", savedEntity);

        underTest.delete(savedEntity);
        assertThat(underTest.findById(savedEntity.getId())).isEmpty();
    }
}
