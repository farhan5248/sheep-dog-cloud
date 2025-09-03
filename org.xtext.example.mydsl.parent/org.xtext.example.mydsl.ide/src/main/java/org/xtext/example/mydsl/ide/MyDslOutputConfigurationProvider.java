package org.xtext.example.mydsl.ide;

import java.util.Set;

import org.eclipse.xtext.generator.IOutputConfigurationProvider;
import org.eclipse.xtext.generator.OutputConfiguration;

/**
 * Custom output configuration provider for MyDsl language server.
 * This configures the output directory for code generation to use
 * the correct project-relative src-gen directory.
 */
public class MyDslOutputConfigurationProvider implements IOutputConfigurationProvider {

    public static final String DEFAULT_OUTPUT = "DEFAULT_OUTPUT";
    public static final String SRC_GEN_OUTPUT = "SRC_GEN_OUTPUT";

    @Override
    public Set<OutputConfiguration> getOutputConfigurations() {
        OutputConfiguration defaultOutput = new OutputConfiguration(DEFAULT_OUTPUT);
        defaultOutput.setDescription("Default Output");
        defaultOutput.setOutputDirectory("src-gen");
        defaultOutput.setOverrideExistingResources(true);
        defaultOutput.setCreateOutputDirectory(true);
        defaultOutput.setCleanUpDerivedResources(true);
        defaultOutput.setSetDerivedProperty(true);
        defaultOutput.setKeepLocalHistory(true);
        
        return Set.of(defaultOutput);
    }
}