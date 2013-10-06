package com.github.bednar.aap;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.github.bednar.aap.model.ApiModel;
import com.github.bednar.aap.model.EntityModel;
import com.github.bednar.aap.model.OperationModel;
import com.github.bednar.aap.model.ParameterModel;
import com.github.bednar.aap.model.PropertyModel;
import com.google.common.base.Function;
import com.google.common.base.Predicate;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.FluentIterable;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * @author Jakub Bednář (06/10/2013 10:42 AM)
 */
public final class ModelBuilder
{
    private final Map<Class, ApiModel> apiCache = Maps.newConcurrentMap();
    private final Map<Class, EntityModel> entityCache = Maps.newConcurrentMap();

    private ModelBuilder()
    {
    }

    private final static ModelBuilder intance = new ModelBuilder();

    @Nonnull
    public static ModelBuilder getInstance()
    {
        return intance;
    }

    @Nonnull
    public ApiModel getApiModel(final @Nonnull Class klass)
    {
        ApiModel model = apiCache.get(klass);
        if (model == null)
        {
            return buildApiModel(klass);
        }

        return model;
    }

    @Nonnull
    public EntityModel getEntityModel(final @Nonnull Class klass)
    {
        EntityModel model = entityCache.get(klass);
        if (model == null)
        {
            return buildEntityModel(klass);
        }

        return model;
    }

    @Nonnull
    private synchronized ApiModel buildApiModel(final @Nonnull Class<?> klass)
    {
        ApiModel model = apiCache.get(klass);
        if (model == null)
        {
            model = new ApiModelTransform().apply(klass);

            apiCache.put(klass, model);
        }

        return model;
    }

    //TODO jb buildery do samostatnych trid
    @Nonnull
    private synchronized EntityModel buildEntityModel(final @Nonnull Class klass)
    {
        EntityModel model = entityCache.get(klass);
        if (model == null)
        {
            model = new EntityModelTransformer().apply(klass);

            entityCache.put(klass, model);
        }

        return model;
    }

    private class ApiModelTransform implements Function<Class, ApiModel>
    {
        @Nonnull
        @Override
        public ApiModel apply(final @Nonnull @SuppressWarnings("NullableProblems") Class klass)
        {
            ApiModel model = new ApiModel();

            model.path = processPath(klass);

            model.consumes = processConsumes(klass);
            model.produces = processProduces(klass);

            model.shortDescription = processShortDescription(klass);

            model.operations = processOperations(klass, model);

            return model;
        }
    }

    private class OperationModelTransform implements Function<Method, OperationModel>
    {
        private final ApiModel model;

        private OperationModelTransform(final ApiModel model)
        {
            this.model = model;
        }

        @Nullable
        @Override
        public OperationModel apply(final @Nonnull @SuppressWarnings("NullableProblems") Method method)
        {
            OperationModel model = new OperationModel();

            model.position = processPosition(method);

            model.path = processPath(this.model.path, method);
            model.httpMethod = processHttpMethod(method);

            model.shortDescription = processShortDescription(method);
            model.authorizations = processAuthorizations(method);

            model.responseEntity = processResponseEntity(method);
            model.responseWrapper = processResponseWrapper(method);
            model.responses = processResponses(method);

            model.parameters = processParameters(method);

            return model;
        }
    }

    private class ApiParamTransform implements Function<MethodParameter, ParameterModel>
    {
        @Nullable
        @Override
        public ParameterModel apply(final @Nonnull @SuppressWarnings("NullableProblems") MethodParameter methodParameter)
        {
            ParameterModel model = new ParameterModel();

            model.name = processName(methodParameter);
            model.shortDescription = processShortDescription(methodParameter);

            model.required = processRequired(methodParameter);

            model.type = processType(methodParameter);

            return model;
        }
    }

    private class EntityModelTransformer implements Function<Class, EntityModel>
    {
        @Nonnull
        @Override
        public EntityModel apply(final @Nonnull @SuppressWarnings("NullableProblems") Class klass)
        {
            EntityModel model = new EntityModel();

            model.shortDescription  = processShortDescription(klass);
            model.properties        = processProperties(klass);

            return model;
        }

        @Nonnull
        private String processShortDescription(final @Nonnull Class<?> klass)
        {
            com.wordnik.swagger.annotations.ApiModel model =
                    klass.getAnnotation(com.wordnik.swagger.annotations.ApiModel.class);

            return model != null ? model.value() : "";
        }

        @Nonnull
        private List<PropertyModel> processProperties(final @Nonnull Class klass)
        {
            List<Field> declaredFields = Lists.newArrayList(klass.getDeclaredFields());

            return FluentIterable
                    .from(declaredFields).filter(
                            new Predicate<Field>()
                            {
                                @Override
                                public boolean apply(@Nullable final Field field)
                                {
                                    return field != null && field.getAnnotation(ApiModelProperty.class) != null;
                                }
                            })
                    .transform(
                            new Function<Field, PropertyModel>()
                            {
                                @Nonnull
                                @Override
                                public PropertyModel apply(final @Nonnull @SuppressWarnings("NullableProblems") Field field)
                                {
                                    return new PropertyModel();
                                }
                            })
                    .toSortedList(
                            new Comparator<PropertyModel>()
                            {
                                @Override
                                public int compare(final PropertyModel property1, final PropertyModel property2)
                                {
                                    return ComparisonChain.start().compare(property1.position, property2.position).result();
                                }
                            });
        }
    }

    @Nonnull
    private String processPath(final @Nonnull Class<?> klass)
    {
        Path path = klass.getAnnotation(Path.class);

        return path != null ? path.value() : "";
    }

    @Nonnull
    private String processPath(final @Nonnull String parentPath, final @Nonnull Method method)
    {
        Path path = method.getAnnotation(Path.class);

        StringBuilder result = new StringBuilder();
        if (!parentPath.isEmpty())
        {
            result.append(parentPath);
        }
        if (!parentPath.endsWith("/"))
        {
            result.append("/");
        }
        if (path != null)
        {
            result.append(path.value());
        }

        return result.toString();
    }

    @Nonnull
    private String[] processConsumes(final @Nonnull Class<?> klass)
    {
        Consumes consumes = klass.getAnnotation(Consumes.class);

        return consumes != null ? consumes.value() : new String[]{};
    }

    @Nonnull
    private String[] processProduces(final @Nonnull Class<?> klass)
    {
        Produces produces = klass.getAnnotation(Produces.class);

        return produces != null ? produces.value() : new String[]{};
    }

    @Nonnull
    private String processShortDescription(final @Nonnull Class<?> klass)
    {
        Api api = klass.getAnnotation(Api.class);

        return api != null ? api.value() : "";
    }

    @Nonnull
    private String processShortDescription(final @Nonnull Method method)
    {
        ApiOperation operation = method.getAnnotation(ApiOperation.class);

        return operation != null ? operation.value() : "";
    }

    private int processPosition(final @Nonnull Method method)
    {
        ApiOperation operation = method.getAnnotation(ApiOperation.class);

        return operation != null ? operation.position() : 0;
    }

    @Nonnull
    private String processHttpMethod(final @Nonnull Method method)
    {
        //noinspection unchecked
        List<Class<? extends Annotation>> annotations = Lists.newArrayList(GET.class, POST.class, DELETE.class);

        for (Class<? extends Annotation> annotation : annotations)
        {
            if (method.getAnnotation(annotation) != null)
            {
                return annotation.getSimpleName();
            }
        }

        return "";
    }

    @Nonnull
    private String processAuthorizations(final @Nonnull Method method)
    {
        ApiOperation operation = method.getAnnotation(ApiOperation.class);

        return operation != null ? operation.authorizations() : "";
    }

    @Nonnull
    private Class processResponseEntity(final @Nonnull Method method)
    {
        ApiOperation operation = method.getAnnotation(ApiOperation.class);

        return operation != null ? operation.response() : Void.class;
    }

    @Nonnull
    private Class processResponseWrapper(final @Nonnull Method method)
    {
        ApiOperation operation = method.getAnnotation(ApiOperation.class);
        if (operation == null || operation.responseContainer().isEmpty())
        {
            return Void.class;
        }

        try
        {
            return Class.forName(operation.responseContainer());
        }
        catch (ClassNotFoundException e)
        {
            throw new ApiBuilderException(e);
        }
    }

    @Nonnull
    private Map<Integer, String> processResponses(final @Nonnull Method method)
    {
        Map<Integer, String> results = Maps.newHashMap();

        ApiResponse response = method.getAnnotation(ApiResponse.class);
        if (response != null)
        {
            results.put(response.code(), response.message());
        }

        ApiResponses responses = method.getAnnotation(ApiResponses.class);
        if (responses != null)
        {
            for (ApiResponse resp : responses.value())
            {
                results.put(resp.code(), resp.message());
            }
        }

        return results;
    }

    @Nonnull
    private String processName(final @Nonnull MethodParameter methodParameter)
    {
        return methodParameter.param != null ? methodParameter.param.name() : "";
    }

    @Nonnull
    private String processShortDescription(final @Nonnull MethodParameter methodParameter)
    {
        return methodParameter.param != null ? methodParameter.param.value() : "";
    }

    @Nonnull
    private Boolean processRequired(final @Nonnull MethodParameter methodParameter)
    {
        return methodParameter.param != null && methodParameter.param.required();
    }

    @Nonnull
    private Class processType(final @Nonnull MethodParameter methodParameter)
    {
        return methodParameter.type != null ? methodParameter.type : Void.class;
    }

    @Nonnull
    private List<OperationModel> processOperations(final @Nonnull Class<?> klass, final ApiModel model)
    {
        List<Method> methods = Lists.newArrayList(klass.getDeclaredMethods());

        return FluentIterable
                .from(methods)
                .filter(
                        new Predicate<Method>()
                        {
                            @Override
                            public boolean apply(@Nullable final Method method)
                            {
                                return method != null && method.getAnnotation(ApiOperation.class) != null;
                            }

                        })
                .transform(new OperationModelTransform(model))
                .toSortedList(
                        new Comparator<OperationModel>()
                        {
                            @Override
                            public int compare(final OperationModel operation1, final OperationModel operation2)
                            {
                                return ComparisonChain.start().compare(operation1.position, operation2.position).result();
                            }

                        });
    }

    @Nonnull
    private List<ParameterModel> processParameters(final @Nonnull Method method)
    {
        List<ParameterModel> results = Lists.newArrayList();

        Class<?>[] types = method.getParameterTypes();
        Annotation[][] annotations = method.getParameterAnnotations();

        for (int i = 0; i < types.length; i++)
        {
            for (Annotation annotation : annotations[i])
            {
                if (annotation instanceof ApiParam)
                {
                    MethodParameter methodParameter = new MethodParameter();

                    methodParameter.type = types[i];
                    methodParameter.param = (ApiParam) annotation;

                    results.add(new ApiParamTransform().apply(methodParameter));
                }
            }
        }

        return results;
    }

    private class MethodParameter
    {
        private Class type;
        private ApiParam param;
    }

    private class ApiBuilderException extends RuntimeException
    {
        private ApiBuilderException(final Throwable cause)
        {
            super(cause);
        }
    }
}
