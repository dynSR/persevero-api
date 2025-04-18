package com.dyns.persevero.domain.model;

import java.io.Serializable;

/**
 * Basic form of a model class.
 *
 * @param <ID> The type of id used by the model.
 *             {@code String, Long, UUID, Integer}
 */
public interface Model<ID> extends Serializable {

    ID getId();

}
