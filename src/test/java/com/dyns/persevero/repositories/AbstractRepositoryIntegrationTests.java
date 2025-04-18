package com.dyns.persevero.repositories;

import com.dyns.persevero.domain.model.Model;
import com.dyns.persevero.fixtures.Fixture;
import com.dyns.persevero.utils.Validate;
import jakarta.transaction.Transactional;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.reflect.ParameterizedType;
import java.util.Objects;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

/**
 * An abstract class used to implement basic integration tests;
 * And all necessary components.
 *
 * @param <TRepo>    The type of repository that extends CrudRepository
 * @param <TEntity>  The type of entity managed by the repository that extends Model
 * @param <TFixture> The type of fixture used to mock data that extends Fixture
 * @param <TId>      The type of ID used by both the entity and the repository
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
        TRepo extends CrudRepository<TEntity, TId>,
        TEntity extends Model<TId>,
        TFixture extends Fixture<TEntity>,
        TId
        > {

    @Autowired
    protected TRepo underTest;

    protected TFixture fixture = null;

    protected abstract TFixture getFixture();

    protected abstract void setDependencies();

    protected TEntity getSavedEntity() {
        return underTest.save(
                getFixture().getOne()
        );
    }

    protected TEntity getSavedEntity(@NotNull TEntity entityToSave) {
        return underTest.save(
                Objects.requireNonNull(entityToSave, "Provided entity to save cannot be null.")
        );
    }

    protected void saveOne(@NotNull TEntity entityToSave) {
        underTest.save(
                Objects.requireNonNull(entityToSave, "Provided entity to save cannot be null.")
        );
    }

    protected void saveMany() {
        getFixture().getMany().forEach(underTest::save);
    }

    protected String getFailureMessage() {
        return getFailureMessage(getEntityTypeName());
    }

    protected String getFailureMessage(@NotBlank String entityName) {
        return String.format(
                "Expected %s to be found, but none was !",
                Validate.notBlank(entityName)
        );
    }

    @SuppressWarnings({"unchecked"})
    private String getEntityTypeName() {
        return ((Class<TEntity>) ((ParameterizedType) getClass().getGenericSuperclass()).getActualTypeArguments()[1])
                .getSimpleName()
                .toLowerCase();
    }

    @Test
    @DisplayName("Should save and retrieve entity")
    public void givenEntity_whenSaved_thenIsPersisted() {
        // GIVEN
        TEntity entity = getFixture().getOne();

        // WHEN
        TEntity savedEntity = getSavedEntity(entity);
        log.info("Saved entity : {}", savedEntity);

        // THEN
        assertThat(entity).isEqualTo(savedEntity);
        assertThat(underTest.findById(savedEntity.getId())).isPresent();
    }

    @Test
    @DisplayName("Should retrieve all entities")
    public void givenEntities_whenQueryingAll_thenReturnsIterableOfEntity() {
        // WHEN
        Iterable<TEntity> allEntities = underTest.findAll();
        log.info("All entities : {}", allEntities);

        // THEN
        assertThat(allEntities.spliterator().getExactSizeIfKnown())
                .isEqualTo(getFixture().getCreatedAmount());
    }

    @Test
    @DisplayName("Should retrieve one entity")
    public void givenEntity_whenQueryingOne_thenReturnsEntity() {
        // WHEN
        TEntity existingEntity = getSavedEntity();
        log.info("Entity : {}", existingEntity);

        // THEN
        assertThat(underTest.findById(existingEntity.getId())).isPresent();
    }

    @Test
    @DisplayName("Should delete entity")
    public void givenSavedEntity_whenDeleted_IsNotPersistedAnymore() {
        // GIVEN
        TEntity existingEntity = getSavedEntity();
        log.info("Deleted entity : {}", existingEntity);

        // WHEN
        underTest.delete(existingEntity);

        // THEN
        assertThat(underTest.findById(existingEntity.getId())).isEmpty();
    }

}
