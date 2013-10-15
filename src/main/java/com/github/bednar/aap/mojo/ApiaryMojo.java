package com.github.bednar.aap.mojo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * @author Jakub Bednář (15/10/2013 4:24 PM)
 */
@Mojo( name = "apiary")
public class ApiaryMojo extends AbstractMojo
{
    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        getLog().info("Apiary Documentation");
        getLog().info("Finished!");
    }
}
