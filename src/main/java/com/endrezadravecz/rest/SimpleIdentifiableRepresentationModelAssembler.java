package com.endrezadravecz.rest;

import lombok.Getter;
import lombok.Setter;
import org.springframework.core.GenericTypeResolver;
import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.LinkBuilder;
import org.springframework.hateoas.server.LinkRelationProvider;
import org.springframework.hateoas.server.SimpleRepresentationModelAssembler;
import org.springframework.hateoas.server.core.EvoInflectorLinkRelationProvider;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;

public class SimpleIdentifiableRepresentationModelAssembler<T> implements SimpleRepresentationModelAssembler<T> {

    private final Class<?> controllerClass;

    @Getter
    private final LinkRelationProvider relProvider;

    @Getter
    private final Class<?> resourceType;

    @Getter
    @Setter
    private String basePath = "";

    public SimpleIdentifiableRepresentationModelAssembler(Class<?> controllerClass, LinkRelationProvider relProvider) {

        this.controllerClass = controllerClass;
        this.relProvider = relProvider;
        this.resourceType = GenericTypeResolver.resolveTypeArgument(this.getClass(), SimpleIdentifiableRepresentationModelAssembler.class);
    }

    public SimpleIdentifiableRepresentationModelAssembler(Class<?> controllerClass) {
        this(controllerClass, new EvoInflectorLinkRelationProvider());
    }

    public void addLinks(EntityModel<T> resource) {

        resource.add(getCollectionLinkBuilder().slash(getId(resource)).withSelfRel());
        resource.add(getCollectionLinkBuilder().withRel(this.relProvider.getCollectionResourceRelFor(this.resourceType)));
    }

    private Object getId(EntityModel<T> resource) {

        Field id = ReflectionUtils.findField(this.resourceType, "id");
        ReflectionUtils.makeAccessible(id);

        return ReflectionUtils.getField(id, resource.getContent());
    }

    public void addLinks(CollectionModel<EntityModel<T>> resources) {
        resources.add(getCollectionLinkBuilder().withSelfRel());
    }

    protected LinkBuilder getCollectionLinkBuilder() {

        WebMvcLinkBuilder linkBuilder = linkTo(this.controllerClass);

        for (String pathComponent : (getPrefix() + this.relProvider.getCollectionResourceRelFor(this.resourceType)).split("/")) {
            if (!pathComponent.isEmpty()) {
                linkBuilder = linkBuilder.slash(pathComponent);
            }
        }

        return linkBuilder;
    }

    private String getPrefix() {
        return getBasePath().isEmpty() ? "" : getBasePath() + "/";
    }
}
