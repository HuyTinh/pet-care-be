package com.petcare.service;

import java.util.List;
import java.util.Optional;

public interface EntityService<Entity, Key> {

    abstract public List<Entity> getAllEntity();

    abstract public Optional<Entity> getEntityById(Key id);

    abstract public Entity createEntity(Entity entity);

    abstract public Entity updateEntity(Entity entity);

    abstract public void deleteEntity(Key id);

}
