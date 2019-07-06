/*
 * Copyright (c) 2013-2019 GraphAware
 *
 * This file is part of the GraphAware Framework.
 *
 * GraphAware Framework is free software: you can redistribute it and/or modify it under
 * the terms of the GNU General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 * See the GNU General Public License for more details. You should have received a copy of
 * the GNU General Public License along with this program.  If not, see
 * <http://www.gnu.org/licenses/>.
 */

package com.graphaware.common.policy.inclusion.fluent;

import com.graphaware.common.description.predicate.Predicate;
import com.graphaware.common.description.property.DetachedPropertiesDescription;
import com.graphaware.common.description.property.LiteralPropertiesDescription;
import com.graphaware.common.policy.inclusion.BaseEntityInclusionPolicy;
import com.graphaware.common.policy.inclusion.EntityInclusionPolicy;
import org.neo4j.graphdb.Entity;

/**
 * An abstract base-class for {@link EntityInclusionPolicy} implementations that are based on property
 * value {@link Predicate}s. In other words, the implementations can be used to specify, which {@link Entity}s
 * to include based on the presence and/or value of their properties.
 */
public abstract class IncludeEntities<C extends IncludeEntities<?, T>, T extends Entity> extends BaseEntityInclusionPolicy<T> implements EntityInclusionPolicy<T> {

    private final DetachedPropertiesDescription propertiesDescription;

    /**
     * Construct a new policy.
     *
     * @param propertiesDescription description of properties an included {@link Entity} must fulfill.
     *                              More precisely, a {@link LiteralPropertiesDescription} of the {@link Entity}
     *                              must be more specific than the given properties description.
     */
    protected IncludeEntities(DetachedPropertiesDescription propertiesDescription) {
        this.propertiesDescription = propertiesDescription;
    }

    /**
     * Construct a new description from this description by adding/replacing a predicate with a new one.
     *
     * @param propertyKey key of the property the predicate is for.
     * @param predicate   the predicate.
     * @return a new instance of properties description.
     */
    public C with(String propertyKey, Predicate predicate) {
        return newInstance(propertiesDescription.with(propertyKey, predicate));
    }

    /**
     * Create a new instance of this policy reconfigured with a different properties description.
     *
     * @param propertiesDescription of the new instance.
     * @return new instance.
     */
    protected abstract C newInstance(DetachedPropertiesDescription propertiesDescription);

    /**
     * Get the properties description with which this policy has been configured.
     *
     * @return properties description.
     */
    public DetachedPropertiesDescription getPropertiesDescription() {
        return propertiesDescription;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean include(T entity) {
        return new LiteralPropertiesDescription(entity).isMoreSpecificThan(propertiesDescription);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        IncludeEntities that = (IncludeEntities) o;

        if (!propertiesDescription.equals(that.propertiesDescription)) return false;

        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public int hashCode() {
        return propertiesDescription.hashCode();
    }
}
