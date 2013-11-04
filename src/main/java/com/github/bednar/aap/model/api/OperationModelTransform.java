package com.github.bednar.aap.model.api;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

import com.google.common.base.Function;
import com.google.common.collect.ComparisonChain;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
* @author Jakub Bednář (06/10/2013 6:08 PM)
*/
public class OperationModelTransform implements Function<Method, OperationModel>
{
    private final String parentPath;

    public OperationModelTransform(final @Nonnull String parentPath)
    {
        this.parentPath = parentPath;
    }

    @Nullable
    @Override
    public OperationModel apply(final @Nonnull @SuppressWarnings("NullableProblems") Method method)
    {
        Integer position = processPosition(method);

        String path          = processPath(parentPath, method);
        String httpMethod    = processHttpMethod(method);

        String shortDescription  = processShortDescription(method);
        String authorizations    = processAuthorizations(method);

        Class responseEntity            = processResponseEntity(method);
        Class responseWrapper           = processResponseWrapper(method);
        Map<String, String> responses   = processResponses(method);

        List<ParameterModel> parameters = processParameters(method);

        OperationModel model = new OperationModel();

        model
                .setPosition(position)
                .setPath(path)
                .setHttpMethod(httpMethod)
                .setShortDescription(shortDescription)
                .setAuthorizations(authorizations)
                .setResponseEntity(responseEntity)
                .setResponseWrapper(responseWrapper)
                .setResponses(responses)
                .setParameters(parameters);

        return model;
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
            throw new OperationModelTransformException(e);
        }
    }

    @Nonnull
    private Map<String, String> processResponses(final @Nonnull Method method)
    {
        Map<String, String> results = Maps.newTreeMap(new Comparator<String>()
        {
            @Override
            public int compare(final String key1, final String key2)
            {
                return ComparisonChain
                        .start()
                        .compare(key1, key2)
                        .result();
            }
        });

        ApiResponse response = method.getAnnotation(ApiResponse.class);
        if (response != null)
        {
            results.put(String.valueOf(response.code()), response.message());
        }

        ApiResponses responses = method.getAnnotation(ApiResponses.class);
        if (responses != null)
        {
            for (ApiResponse resp : responses.value())
            {
                results.put(String.valueOf(resp.code()), resp.message());
            }
        }

        return results;
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
                    ParameterModelTransform.MethodParameter methodParameter = new ParameterModelTransform.MethodParameter();

                    methodParameter.type = types[i];
                    methodParameter.param = (ApiParam) annotation;

                    results.add(new ParameterModelTransform().apply(methodParameter));
                }
            }
        }

        return results;
    }

    private class OperationModelTransformException extends RuntimeException
    {
        private OperationModelTransformException(final Throwable cause)
        {
            super(cause);
        }
    }
}
