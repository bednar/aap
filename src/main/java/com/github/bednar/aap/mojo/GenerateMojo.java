package com.github.bednar.aap.mojo;

import java.io.File;

import com.github.bednar.aap.processor.doc.Apiary;
import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugin.MojoFailureException;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;

/**
 * @author Jakub Bednář (15/10/2013 4:24 PM)
 */
@Mojo(name = "generate")
public class GenerateMojo extends AbstractMojo
{
    @Parameter
    private ApiaryCfg apiary;

    /**
     * Project base dir
     */
    @Parameter(defaultValue = "${project.basedir}")
    private File basedir;

    @Override
    public void execute() throws MojoExecutionException, MojoFailureException
    {
        if (apiary != null)
        {
            getLog().info("Apiary Configuration: " + apiary);
            getLog().info("Apiary Output: " + basedir);

            Apiary
                    .create(basedir)
                    .generate(apiary.appName, apiary.apiBaseURL, apiary.appDescription);
        }
    }

    public static class ApiaryCfg
    {
        private String appName;
        private String appDescription;
        private String apiBaseURL;

        {
            appName         = "Demo App";
            appDescription  = "";
            apiBaseURL      = "https://www.example.com/api";
        }

        @Override
        public String toString()
        {
            return "ApiaryCfg{" +
                    "appName='" + appName + '\'' +
                    ", appDescription='" + appDescription + '\'' +
                    ", apiBaseURL='" + apiBaseURL + '\'' +
                    '}';
        }
    }
}
