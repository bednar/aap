package com.github.bednar.aap.it.setuplibrary;

import javax.ws.rs.Consumes;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import com.wordnik.swagger.annotations.Api;

@Path("pub")
@Api(value = "Library API", description = "Testing api in library.")
@Consumes("application/json")
@Produces("application/json")
public interface LibraryApi
{
}
