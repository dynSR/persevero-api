package com.dyns.persevero.services;

/**
 * Generic service interface for managing entities.
 *
 * @param <T>  The entity type.
 * @param <ID> The type of the entity's identifier.
 */
public interface Service<T, ID> {

    /**
     * Saves a given entity.
     *
     * @param entity The entity to save.
     * @return The saved entity.
     */
    T save(T entity);

    /**
     * Retrieves all entities.
     *
     * @return An iterable of all entities or an empty array.
     */
    Iterable<T> findAll();

    /**
     * Retrieves an entity by its identifier.
     *
     * @param id The identifier of the entity.
     * @return The entity if found.
     */
    T findOne(ID id);

    /**
     * Partially updates an entity with the provided data.
     *
     * @param id     The identifier of the entity to update.
     * @param entity The entity containing updated fields.
     * @return The updated entity.
     */
    T partialUpdate(ID id, T entity);

    /**
     * Deletes an entity by its identifier.
     *
     * @param id The identifier of the entity to delete.
     */
    void delete(ID id);

    /**
     * Checks whether an entity with the given identifier exists.
     *
     * @param id The identifier of the entity.
     * @return {@code true} if the entity does not exist, {@code false} otherwise.
     */
    boolean doesNotExist(ID id);

}
