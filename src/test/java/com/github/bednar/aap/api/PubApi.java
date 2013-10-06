package com.github.bednar.aap.api;

import javax.annotation.Nonnull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import java.util.List;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

/**
 * <a href="https://github.com/wordnik/swagger-core/wiki/Java-grails2-Quickstart">Swagger Annotation Example</a>
 *
 * @author Jakub Bednář (05/10/2013 3:05 PM)
 */
@Path("pub")
@Api(value = "Api for my small pub.")
@Consumes("application/json")
@Produces("application/json")
public interface PubApi
{
    @GET
    @Path("mealByName")
    @ApiOperation(value = "Find meal by name.", response = Meal.class, position = 1)
    @ApiResponse(code = 400, message = "Cannot find meal by name.")
    Meal mealByName(final @Nonnull @ApiParam(name="name", value = "Name of Meal", required = true) String name);

    @GET
    @Path("allMeals")
    @ApiOperation(value = "Find all meals.", response = Meal.class, responseContainer = "java.util.List", position = 3)
    List<Meal> allMeals();

    @POST
    @Path("updateMeal")
    @ApiOperation(value = "Update Meal in pub.", authorizations = "Owner of Pub", position = 2)
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Success update."),
            @ApiResponse(code = 401, message = "Authorization violation.")
    })
    void updateMeal(final @Nonnull @ApiParam(name="meal", value = "Meal for update", required = true) Meal meal);

    @ApiModel("Meal for order in pub.")
    public class Meal
    {
        @ApiModelProperty(value = "Name", required = true, allowableValues = "max:10")
        private String name;

        @ApiModelProperty(value = "Price", required = true, allowableValues = "min:0")
        private Integer price;
    }
}
