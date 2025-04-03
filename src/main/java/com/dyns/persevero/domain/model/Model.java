package com.dyns.persevero.domain.model;

import java.io.Serializable;

/**
 * Basic form of a model class.
 *
 * @param <ID> The type of id used by the model.
 *             {@code String, Long, UUID, Integer}
 */
public interface Model<ID, NAME> extends Serializable {
    ID getId();

    void setId(ID id);

    NAME getName();

    void setName(NAME name);

    /**
     * Function used to get the class type of the name property of this model.
     * <p>
     * This type is used for abstraction purpose(s).
     *
     * @return The name class type
     * @implNote The best is to simply return {@code name.getClass()}
     */
    Class<?> getNameClass();
}
