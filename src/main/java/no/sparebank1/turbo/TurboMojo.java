package no.sparebank1.turbo;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;

/**
 * The turbo plugin's functionality is delivered by the TurboBuildWrapper maven extension.
 *
 * This plugin is here to have an owner for the configuration in the pom.
 */
@Mojo(name = "turbo", defaultPhase = LifecyclePhase.VALIDATE, threadSafe = true, inheritByDefault = false, aggregator = true)
public class TurboMojo extends AbstractMojo {

    @Override
    public void execute() {

    }
}