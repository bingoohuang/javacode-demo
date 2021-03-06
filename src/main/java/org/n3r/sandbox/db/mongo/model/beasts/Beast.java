package org.n3r.sandbox.db.mongo.model.beasts;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Copyright (c) 2013 Ivan Hristov
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = Beast.KIND)
@JsonSubTypes({
        @JsonSubTypes.Type(value = DireWolf.class, name = Kind.Constants.DIRE_WOLF)
})
public abstract class Beast {

    public static final String KIND = "kind";
    public static final String NAME = "name";

    private final Kind kind;
    private final String name;

    protected Beast(Kind kind, String name) {
        this.kind = kind;
        this.name = name;
    }

    @JsonProperty(KIND)
    public Kind getKind() {
        return kind;
    }

    @JsonProperty(NAME)
    public String getName() {
        return name;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Beast beast = (Beast) o;

        return new EqualsBuilder().append(this.kind, beast.kind).append(this.name, beast.name).isEquals();
    }

    @Override
    public int hashCode() {
        return new HashCodeBuilder(29, 67).append(kind).append(name).toHashCode();
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .appendSuper(super.toString())
                .append("kind", kind)
                .append("name", name)
                .toString();
    }
}
