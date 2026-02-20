package org.farhan.mbt.maven;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.apache.maven.plugin.AbstractMojo;
import org.apache.maven.plugin.MojoExecutionException;
import org.apache.maven.plugins.annotations.LifecyclePhase;
import org.apache.maven.plugins.annotations.Mojo;
import org.apache.maven.plugins.annotations.Parameter;
import org.apache.maven.project.MavenProject;

@Mojo(name = "run", defaultPhase = LifecyclePhase.GENERATE_SOURCES)
public class RunMojo extends AbstractMojo {

	@Parameter(defaultValue = "${project}", readonly = true)
	public MavenProject project;

	// Path properties
	@Parameter(property = "specsDir", defaultValue = "../../sheep-dog-qa/sheep-dog-specs")
	public String specsDir;

	@Parameter(property = "asciidocDir", defaultValue = "../../sheep-dog-qa/sheep-dog-specs/src/test/resources/asciidoc/specs/Ubiquitous Language")
	public String asciidocDir;

	@Parameter(property = "scenariosFile", defaultValue = "scenarios-list.txt")
	public String scenariosFile;

	// Server properties
	@Parameter(property = "host", defaultValue = "dev.sheepdogdev.io")
	public String host;

	@Parameter(property = "port", defaultValue = "80")
	public int port;

	@Parameter(property = "timeout", defaultValue = "300000")
	public int timeout;

	// Claude CLI properties
	@Parameter(property = "model", defaultValue = "sonnet")
	public String model;

	@Parameter(property = "coAuthor", defaultValue = "Claude Sonnet 4.5 <noreply@anthropic.com>")
	public String coAuthor;

	@Parameter(property = "maxRetries", defaultValue = "3")
	public int maxRetries;

	@Parameter(property = "retryWaitSeconds", defaultValue = "30")
	public int retryWaitSeconds;

	// Pipeline property
	@Parameter(property = "pipeline", defaultValue = "forward")
	public String pipeline;

	// Instance fields
	private String baseDir;
	private GitRunner git;
	private MavenRunner maven;

	private record ScenarioEntry(String file, String scenario, String tag) {}

	// =========================================================================
	// Execute
	// =========================================================================

	public void execute() throws MojoExecutionException {
		try {
			baseDir = project.getBasedir().getAbsolutePath();

			git = new GitRunner(getLog());
			maven = new MavenRunner(getLog());

			getLog().info("===========================================");
			getLog().info("RGR Automation Plugin");
			getLog().info("===========================================");
			getLog().info("");

			// Parse scenarios
			List<ScenarioEntry> scenarios = parseScenarios(baseDir + "/" + scenariosFile);
			getLog().info("Found " + scenarios.size() + " scenarios to process");
			getLog().info("");

			// Clean up
			getLog().info("===========================================");
			getLog().info("Running clean up...");
			getLog().info("===========================================");
			int cleanUpExit = runCleanUp();
			if (cleanUpExit != 0) {
				throw new MojoExecutionException("Clean up failed with exit code " + cleanUpExit);
			}
			getLog().info("");

			// Group scenarios by file (preserving order)
			LinkedHashMap<String, List<ScenarioEntry>> scenariosByFile = new LinkedHashMap<>();
			for (ScenarioEntry entry : scenarios) {
				scenariosByFile.computeIfAbsent(entry.file(), k -> new ArrayList<>()).add(entry);
			}

			getLog().info("Found " + scenariosByFile.size() + " feature files to process");
			getLog().info("");

			int totalProcessed = 0;
			int totalTagsAdded = 0;
			int totalFilesProcessed = 0;

			// Outer loop: each feature file
			for (Map.Entry<String, List<ScenarioEntry>> fileGroup : scenariosByFile.entrySet()) {
				String fileName = fileGroup.getKey();
				List<ScenarioEntry> fileScenarios = fileGroup.getValue();

				getLog().info("==========================================");
				getLog().info("FEATURE FILE: " + fileName);
				getLog().info("==========================================");
				getLog().info("Scenarios in this file: " + fileScenarios.size());
				getLog().info("");

				// Inner loop: each scenario
				for (ScenarioEntry entry : fileScenarios) {
					String scenarioName = entry.scenario();
					String tag = entry.tag();

					getLog().info("==========================================");
					getLog().info("Processing Scenario: " + scenarioName);
					getLog().info("Tag: " + tag);
					getLog().info("==========================================");

					if ("NoTag".equals(tag)) {
						getLog().info("  Skipping (NoTag)");
						getLog().info("");
						continue;
					}

					// Add tag to asciidoc file
					boolean tagAdded = addTagToAsciidoc(fileName, scenarioName, tag);
					if (tagAdded) {
						totalTagsAdded++;
					}

					// Run rgr-red
					getLog().info("");
					getLog().info("Running Red-Green workflow for tag: " + tag);
					getLog().info("  [1/2] Running rgr-red...");
					long redStart = System.currentTimeMillis();
					int redExitCode = runRgrRed(tag);
					long redDuration = System.currentTimeMillis() - redStart;

					if (redExitCode != 0 && redExitCode != 100) {
						throw new MojoExecutionException("rgr-red failed with exit code " + redExitCode);
					}
					getLog().info("  Completed rgr-red (Duration: " + formatDuration(redDuration) + ")");

					// Stage changes
					git.run(baseDir, "add", ".");

					// Run rgr-green if tests are failing
					long greenDuration = 0;
					if (redExitCode == 0) {
						getLog().info("  [2/2] Running rgr-green...");
						long greenStart = System.currentTimeMillis();
						int greenExitCode = runRgrGreen(tag);
						greenDuration = System.currentTimeMillis() - greenStart;
						if (greenExitCode != 0) {
							throw new MojoExecutionException("rgr-green failed with exit code " + greenExitCode);
						}
						getLog().info("  Completed rgr-green (Duration: " + formatDuration(greenDuration) + ")");
						git.run(baseDir, "add", ".");
					} else {
						getLog().info("  [2/2] Skipping rgr-green (tests already passing)");
					}

					long totalDuration = redDuration + greenDuration;
					getLog().info("");
					getLog().info("  Red-Green workflow completed for tag: " + tag + " (Total Duration: " + formatDuration(totalDuration) + ")");
					totalProcessed++;
					getLog().info("");
				}

				// After all scenarios in file: commit and refactor
				getLog().info("==========================================");
				getLog().info("All scenarios in file " + fileName + " processed");
				getLog().info("==========================================");

				int diffQuietExit = git.run(baseDir, "diff", "--cached", "--quiet");
				boolean hasStagedChanges = (diffQuietExit != 0);

				if (hasStagedChanges) {
					// Commit red-green changes
					String commitMsg = "run-rgr red-green " + fileName + "\n\nCo-Authored-By: " + coAuthor;
					getLog().info("Committing all red-green changes for file: " + commitMsg.split("\n")[0]);
					git.run(baseDir, "add", ".");
					git.run(baseDir, "commit", "-m", commitMsg);
					getLog().info("");

					// Check if src/main has changes (code was modified, not just tests)
					int mainDiffExit = git.run(baseDir, "diff", "HEAD~1", "--quiet", "--", "src/main");
					boolean hasMainChanges = (mainDiffExit != 0);

					if (hasMainChanges) {
						// Run rgr-refactor
						getLog().info("Running rgr-refactor for file: " + fileName);
						getLog().info("");
						long refactorStart = System.currentTimeMillis();
						int refactorExit = runRgrRefactor();
						long refactorDuration = System.currentTimeMillis() - refactorStart;

						if (refactorExit != 0) {
							throw new MojoExecutionException("rgr-refactor failed with exit code " + refactorExit);
						}

						// Commit refactor changes
						String refactorCommitMsg = "run-rgr-refactor " + fileName + "\n\nCo-Authored-By: " + coAuthor;
						getLog().info("Committing changes: " + refactorCommitMsg.split("\n")[0] + " (Duration: " + formatDuration(refactorDuration) + ")");
						git.run(baseDir, "add", ".");
						git.run(baseDir, "commit", "-m", refactorCommitMsg);
					} else {
						getLog().info("Skipping rgr-refactor (no src/main changes)");
					}
				} else {
					getLog().info("Skipping commit (nothing staged for this file)");
				}

				totalFilesProcessed++;
				getLog().info("");
			}

			getLog().info("");
			getLog().info("==========================================");
			getLog().info("RGR Automation Complete!");
			getLog().info("==========================================");
			getLog().info("Total feature files processed: " + totalFilesProcessed);
			getLog().info("Total scenarios processed: " + totalProcessed);
			getLog().info("Total tags added: " + totalTagsAdded);
			getLog().info("==========================================");
		} catch (MojoExecutionException e) {
			throw e;
		} catch (Exception e) {
			throw new MojoExecutionException(e.getMessage(), e);
		}
	}

	// =========================================================================
	// Scenario Parsing
	// =========================================================================

	private List<ScenarioEntry> parseScenarios(String scenariosFilePath) throws Exception {
		List<String> lines = Files.readAllLines(Path.of(scenariosFilePath), StandardCharsets.UTF_8);
		List<ScenarioEntry> result = new ArrayList<>();
		String currentFile = "";
		String currentScenario = "";

		for (String line : lines) {
			if (line.startsWith("File: ")) {
				currentFile = line.substring("File: ".length());
			} else if (line.startsWith("  Scenario: ")) {
				currentScenario = line.substring("  Scenario: ".length());
			} else if (line.startsWith("    Tag: ")) {
				String tag = line.substring("    Tag: ".length());
				result.add(new ScenarioEntry(currentFile, currentScenario, tag));
			}
		}
		return result;
	}

	// =========================================================================
	// AsciiDoc Tag Insertion
	// =========================================================================

	private boolean addTagToAsciidoc(String fileName, String scenarioName, String tag) throws Exception {
		String filePath = baseDir + "/" + asciidocDir + "/" + fileName + ".asciidoc";
		File file = new File(filePath);
		if (!file.exists()) {
			getLog().warn("File not found: " + fileName + ".asciidoc");
			return false;
		}

		List<String> content = Files.readAllLines(file.toPath(), StandardCharsets.UTF_8);
		List<String> newContent = new ArrayList<>();

		for (int i = 0; i < content.size(); i++) {
			String line = content.get(i);
			newContent.add(line);

			if (line.matches("^== Test-Case: .+$")) {
				String testCaseName = line.substring("== Test-Case: ".length());
				if (testCaseName.equals(scenarioName)) {
					// Check if tag already present on next line
					boolean alreadyHasTag = false;
					int nextLineIndex = i + 1;
					if (nextLineIndex < content.size()) {
						String nextLine = content.get(nextLineIndex).trim();
						if (nextLine.startsWith("@")) {
							alreadyHasTag = true;
						}
					}

					if (!alreadyHasTag) {
						newContent.add("");
						newContent.add("@" + tag);
						getLog().debug("  Added tag @" + tag + " to file");
					} else {
						getLog().debug("  Tag already present in file");
					}

					// Append remaining lines and write
					for (int j = i + 1; j < content.size(); j++) {
						newContent.add(content.get(j));
					}
					writeFileWithLF(filePath, newContent);
					return !alreadyHasTag;
				}
			}
		}

		getLog().warn("Scenario not found in file: " + scenarioName);
		return false;
	}

	// =========================================================================
	// RGR Workflow Methods
	// =========================================================================

	private int runRgrRed(String pattern) throws Exception {
		String runnerClassName = pattern + "Test";
		ServiceClient service = new ServiceClient(getLog(), host, port, timeout);

		getLog().debug("RGR-Red: Pattern=" + pattern + ", Runner=" + runnerClassName);

		// Step 1: AsciiDoctor to UML (direct REST call to service)
		getLog().debug("  STEP 1: AsciiDoctor to UML Conversion");
		service.executeToUML("asciidoctor/", "ConvertAsciidoctorToUML", pattern,
				baseDir + "/" + specsDir, ".asciidoc");

		// Step 2: UML to Cucumber-Guice (direct REST call to service)
		getLog().debug("  STEP 2: UML to Cucumber-Guice Conversion");
		service.executeFromUML("cucumber/", "ConvertUMLToCucumberGuice", pattern, baseDir);

		// Step 3: Generate runner class
		getLog().debug("  STEP 3: Generate Runner Class");
		String runnerClassPath = baseDir
			+ "/src/test/java/org/farhan/suites/" + runnerClassName + ".java";
		Files.createDirectories(Path.of(runnerClassPath).getParent());
		String runnerContent = generateRunnerClassContent(pattern, runnerClassName);
		writeFileWithLF(runnerClassPath, List.of(runnerContent.split("\n", -1)));
		getLog().debug("  Created runner class: " + runnerClassPath);

		// Step 4: Run tests
		getLog().debug("  STEP 4: Running tests with " + runnerClassName);
		int testExitCode = maven.run(baseDir, "test", "-Dtest=" + runnerClassName);

		if (testExitCode == 0) {
			getLog().debug("  Tests are PASSING - no failing tests to fix (returning 100)");
			return 100;
		} else {
			getLog().debug("  Tests are FAILING - ready for green phase (returning 0)");
			return 0;
		}
	}

	private int runRgrGreen(String pattern) throws Exception {
		getLog().debug("RGR-Green: Pattern=" + pattern);
		ClaudeRunner claude = new ClaudeRunner(getLog(), model, maxRetries, retryWaitSeconds);
		return claude.run(baseDir + "/../..", "/rgr-green sheep-dog-test " + pattern);
	}

	private int runRgrRefactor() throws Exception {
		getLog().debug("RGR-Refactor: " + pipeline + " sheep-dog-test");
		ClaudeRunner claude = new ClaudeRunner(getLog(), model, maxRetries, retryWaitSeconds);
		return claude.run(baseDir + "/../..", "/rgr-refactor " + pipeline + " sheep-dog-test");
	}

	private int runCleanUp() throws Exception {
		Path sheepDogMain = Path.of(baseDir, "../..").normalize();

		getLog().debug("Deleting NUL files...");
		int deleted = deleteNulFiles(sheepDogMain);
		getLog().debug("Deleted " + deleted + " NUL files");

		getLog().debug("Deleting target directory...");
		deleteDirectory(Path.of(baseDir, "target"));
		return 0;
	}

	private int deleteNulFiles(Path root) throws Exception {
		int[] count = { 0 };
		Files.walk(root)
			.filter(Files::isRegularFile)
			.filter(p -> {
				String name = p.getFileName().toString();
				return "NUL".equals(name) || "nul".equals(name);
			})
			.forEach(p -> {
				try {
					getLog().debug("  Deleting: " + p);
					Files.delete(p);
					count[0]++;
				} catch (Exception e) {
					getLog().debug("  Warning: Could not delete " + p + ": " + e.getMessage());
				}
			});
		return count[0];
	}

	private void deleteDirectory(Path dir) throws Exception {
		if (!Files.exists(dir)) {
			return;
		}
		Files.walk(dir)
			.sorted(java.util.Comparator.reverseOrder())
			.forEach(p -> {
				try {
					Files.delete(p);
				} catch (Exception e) {
					getLog().debug("  Warning: Could not delete " + p + ": " + e.getMessage());
				}
			});
	}

	// =========================================================================
	// File I/O Helpers
	// =========================================================================

	private void writeFileWithLF(String filePath, List<String> lines) throws Exception {
		String content = String.join("\n", lines) + "\n";
		Files.writeString(Path.of(filePath), content, StandardCharsets.UTF_8);
	}

	private String generateRunnerClassContent(String pattern, String runnerClassName) {
		return "package org.farhan.suites;\n"
			+ "\n"
			+ "import org.junit.platform.suite.api.ConfigurationParameter;\n"
			+ "import org.junit.platform.suite.api.IncludeEngines;\n"
			+ "import org.junit.platform.suite.api.IncludeTags;\n"
			+ "import org.junit.platform.suite.api.SelectClasspathResource;\n"
			+ "import org.junit.platform.suite.api.Suite;\n"
			+ "import static io.cucumber.junit.platform.engine.Constants.PLUGIN_PROPERTY_NAME;\n"
			+ "import static io.cucumber.junit.platform.engine.Constants.GLUE_PROPERTY_NAME;\n"
			+ "\n"
			+ "@Suite\n"
			+ "@IncludeEngines(\"cucumber\")\n"
			+ "@SelectClasspathResource(\"cucumber/\")\n"
			+ "@ConfigurationParameter(key = PLUGIN_PROPERTY_NAME, value = \"pretty\")\n"
			+ "@ConfigurationParameter(key = GLUE_PROPERTY_NAME, value = \"org.farhan.common,org.farhan.objects,org.farhan.stepdefs,org.farhan.suites\")\n"
			+ "@IncludeTags(\"" + pattern + "\")\n"
			+ "public class " + runnerClassName + " {\n"
			+ "}";
	}

	private String formatDuration(long millis) {
		long seconds = millis / 1000;
		long hours = seconds / 3600;
		long minutes = (seconds % 3600) / 60;
		long secs = seconds % 60;
		return String.format("%02d:%02d:%02d", hours, minutes, secs);
	}
}
