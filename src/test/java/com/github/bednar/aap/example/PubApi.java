package com.github.bednar.aap.example;

import javax.annotation.Nonnull;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import java.util.List;

import com.wordnik.swagger.annotations.Api;
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
@Api(value = "Public Pub", description = "Long description of Public Pub!", position = 15)
@Consumes("application/json")
@Produces("application/json")
public interface PubApi
{
    @GET
    @Path("mealByName")
    @ApiOperation(
            position = 1,
            value = "Find meal by name.",
            response = Meal.class)
    @ApiResponse(
            code = 400,
            message = "Cannot find meal by name.")
    Meal mealByName(final @Nonnull @ApiParam(name="name", value = "Name of Meal", required = true) String name);

    @GET
    @Path("allMeals")
    @ApiOperation(
            position = 3,
            value = "Find all meals.",
            response = Meal.class,
            responseContainer = "java.util.List")
    @ApiResponse(
            code = 200,
            message = "[" +
                        "{\"name\": \"Hamburger\", \"price\": 1000}," +
                        "{\"name\": \"Chocolate\", \"price\": 500}" +
                       "]")
    List<Meal> allMeals();

    @POST
    @Path("updateMeal")
    @ApiOperation(
            position = 2,
            value = "Update Meal in pub.",
            authorizations = "Owner of Pub")
    @ApiResponses(value = {
            @ApiResponse(
                    code = 200,
                    message = "Success update."),
            @ApiResponse(
                    code = 401,
                    message = "Authorization violation.")
    })
    @Consumes(MediaType.APPLICATION_FORM_URLENCODED)
    void updateMeal(final @Nonnull @ApiParam(name="meal", value = "Meal for update", required = true) Meal meal);
}
