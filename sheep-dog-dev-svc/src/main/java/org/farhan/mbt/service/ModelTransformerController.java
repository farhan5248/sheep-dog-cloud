package org.farhan.mbt.service;

import java.io.PrintWriter;
import java.io.StringWriter;

import org.farhan.mbt.convert.Converter;
import org.farhan.mbt.convert.ConvertAsciidoctorToUML;
import org.farhan.mbt.convert.ConvertCucumberToUML;
import org.farhan.mbt.convert.ConvertUMLToAsciidoctor;
import org.farhan.mbt.convert.ConvertUMLToCucumber;
import org.farhan.mbt.convert.ConvertUMLToCucumberGuice;
import org.farhan.mbt.convert.ConvertUMLToCucumberSpring;
import org.farhan.mbt.convert.ObjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.ApplicationListener;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@ConfigurationProperties(prefix = "sheepdog")
@RestController
public class ModelTransformerController implements ApplicationListener<ApplicationReadyEvent> {

	Logger logger = LoggerFactory.getLogger(ModelTransformerController.class);
	private final ObjectRepository fa;

	@Value("${spring.datasource.url}")
	private String url;

	@Autowired
	public ModelTransformerController(ObjectRepository fa) {
		this.fa = fa;
	}

	@DeleteMapping("/clearConvertAsciidoctorToUMLObjects")
	public ModelTransformerResponse clearConvertAsciidoctorToUMLObjects(
			@RequestParam(value = "tags", defaultValue = "") String tags) {
		logger.info("Starting clearConvertAsciidoctorToUMLObjects");
		logger.info("tags:" + tags);
		ModelTransformerResponse mtr = clearObjects(new ConvertAsciidoctorToUML(tags, fa, new LoggerImpl(logger)));
		logger.debug("response: " + mtr.toString());
		logger.info("Ending clearConvertAsciidoctorToUMLObjects");
		return mtr;
	}

	@DeleteMapping("/clearConvertCucumberToUMLObjects")
	public ModelTransformerResponse clearConvertCucumberToUMLObjects(
			@RequestParam(value = "tags", defaultValue = "") String tags) {
		logger.info("Starting clearConvertCucumberToUMLObjects");
		logger.info("tags:" + tags);
		ModelTransformerResponse mtr = clearObjects(new ConvertAsciidoctorToUML(tags, fa, new LoggerImpl(logger)));
		logger.debug("response: " + mtr.toString());
		logger.info("Ending clearConvertCucumberToUMLObjects");
		return mtr;
	}

	private ModelTransformerResponse clearObjects(Converter mojo) {
		try {
			mojo.clearProjects();
			return new ModelTransformerResponse("", "");
		} catch (Exception e) {
			logger.error(getStackTraceAsString(e));
			return new ModelTransformerResponse("exception", getStackTraceAsString(e));
		}
	}

	private ModelTransformerResponse convertObject(Converter mojo, String fileName, String contents) {
		try {
			return new ModelTransformerResponse(fileName, mojo.convertFile(fileName, contents == null ? "" : contents));
		} catch (Exception e) {
			logger.error(getStackTraceAsString(e));
			return new ModelTransformerResponse("exception", getStackTraceAsString(e));
		}
	}

	@GetMapping("/getConvertUMLToAsciidoctorObjectNames")
	public ModelTransformerResponse getConvertUMLToAsciidoctorObjectNames(
			@RequestParam(value = "tags", defaultValue = "") String tags) {
		logger.info("Starting getConvertUMLToAsciidoctorObjectNames");
		ModelTransformerResponse mtr = getObjectNames(new ConvertUMLToAsciidoctor(tags, fa, new LoggerImpl(logger)),
				tags);
		logger.debug("response: " + mtr.toString());
		logger.info("Ending getConvertUMLToAsciidoctorObjectNames");
		return mtr;
	}

	@GetMapping("/getConvertUMLToCucumberGuiceObjectNames")
	public ModelTransformerResponse getConvertUMLToCucumberGuiceObjectNames(
			@RequestParam(value = "tags", defaultValue = "") String tags) {
		logger.info("Starting getConvertUMLToCucumberGuiceObjectNames");
		ModelTransformerResponse mtr = getObjectNames(new ConvertUMLToCucumberGuice(tags, fa, new LoggerImpl(logger)),
				tags);
		logger.debug("response: " + mtr.toString());
		logger.info("Ending getConvertUMLToCucumberGuiceObjectNames");
		return mtr;
	}

	@GetMapping("/getConvertUMLToCucumberObjectNames")
	public ModelTransformerResponse getConvertUMLToCucumberObjectNames(
			@RequestParam(value = "tags", defaultValue = "") String tags) {
		logger.info("Starting getConvertUMLToCucumberObjectNames");
		ModelTransformerResponse mtr = getObjectNames(new ConvertUMLToCucumber(tags, fa, new LoggerImpl(logger)), tags);
		logger.debug("response: " + mtr.toString());
		logger.info("Ending getConvertUMLToCucumberObjectNames");
		return mtr;
	}

	@GetMapping("/getConvertUMLToCucumberSpringObjectNames")
	public ModelTransformerResponse getConvertUMLToCucumberSpringObjectNames(
			@RequestParam(value = "tags", defaultValue = "") String tags) {
		logger.info("Starting getConvertUMLToCucumberSpringObjectNames");
		ModelTransformerResponse mtr = getObjectNames(new ConvertUMLToCucumberSpring(tags, fa, new LoggerImpl(logger)),
				tags);
		logger.debug("response: " + mtr.toString());
		logger.info("Ending getConvertUMLToCucumberSpringObjectNames");
		return mtr;
	}

	private ModelTransformerResponse getObjectNames(Converter mojo, String tags) {
		String fileList = "";
		ModelTransformerResponse mtr;
		try {
			// TODO this is temp, there should be a separate class like the ObjectRepository
			// if not the object repo itself. For a given tag, it should keep track of the
			// source files and output files checksums
			for (String fileName : (mojo).getFileNames()) {
				// TODO append to a string list for now but later make a proper JSON object
				fileList += "\n" + fileName;
			}
			fileList = fileList.replaceFirst("\n", "");
			mtr = new ModelTransformerResponse(fileList, "");
		} catch (Exception e) {
			logger.error(getStackTraceAsString(e));
			return new ModelTransformerResponse("exception", getStackTraceAsString(e));
		}
		return mtr;
	}

	private String getStackTraceAsString(Exception e) {
		StringWriter sw = new StringWriter();
		e.printStackTrace(new PrintWriter(sw));
		return sw.toString();
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
		logger.info("Starting onApplicationEvent");
		logger.info("spring.datasource.url:" + url);
		logger.info("Ending onApplicationEvent");
	}

	@PostMapping("/runConvertAsciidoctorToUML")
	public ModelTransformerResponse runConvertAsciidoctorToUML(
			@RequestParam(value = "tags", defaultValue = "") String tags,
			@RequestParam(value = "fileName") String fileName, @RequestBody String contents) {
		logger.info("Starting runConvertAsciidoctorToUML");
		logger.info("tags:" + tags);
		logger.info("fileName:" + fileName);
		ModelTransformerResponse mtr = convertObject(new ConvertAsciidoctorToUML(tags, fa, new LoggerImpl(logger)),
				fileName, contents);
		logger.debug("response: " + mtr.toString());
		logger.info("Ending runConvertAsciidoctorToUML");
		return mtr;
	}

	@PostMapping("/runConvertCucumberToUML")
	public ModelTransformerResponse runConvertCucumberToUML(
			@RequestParam(value = "tags", defaultValue = "") String tags,
			@RequestParam(value = "fileName") String fileName, @RequestBody String contents) {
		logger.info("Starting runConvertCucumberToUML");
		logger.info("tags:" + tags);
		logger.info("fileName:" + fileName);
		ModelTransformerResponse mtr = convertObject(new ConvertCucumberToUML(tags, fa, new LoggerImpl(logger)),
				fileName, contents);
		logger.debug("response: " + mtr.toString());
		logger.info("Ending runConvertCucumberToUML");
		return mtr;
	}

	@PostMapping("/runConvertUMLToAsciidoctor")
	public ModelTransformerResponse runConvertUMLToAsciidoctor(
			@RequestParam(value = "tags", defaultValue = "") String tags,
			@RequestParam(value = "fileName") String fileName, @RequestBody(required = false) String contents) {
		logger.info("Starting runConvertUMLToAsciidoctor");
		logger.info("tags:" + tags);
		logger.info("fileName:" + fileName);
		ModelTransformerResponse mtr = convertObject(new ConvertUMLToAsciidoctor(tags, fa, new LoggerImpl(logger)),
				fileName, contents);
		logger.debug("response: " + mtr.toString());
		logger.info("Ending runConvertUMLToAsciidoctor");
		return mtr;
	}

	@PostMapping("/runConvertUMLToCucumber")
	public ModelTransformerResponse runConvertUMLToCucumber(
			@RequestParam(value = "tags", defaultValue = "") String tags,
			@RequestParam(value = "fileName") String fileName, @RequestBody(required = false) String contents) {
		logger.info("Starting runConvertUMLToCucumber");
		logger.info("tags:" + tags);
		logger.info("fileName:" + fileName);
		ModelTransformerResponse mtr = convertObject(new ConvertUMLToCucumber(tags, fa, new LoggerImpl(logger)),
				fileName, contents);
		logger.debug("response: " + mtr.toString());
		logger.info("Ending runConvertUMLToCucumber");
		return mtr;
	}

	@PostMapping("/runConvertUMLToCucumberGuice")
	public ModelTransformerResponse runConvertUMLToCucumberGuice(
			@RequestParam(value = "tags", defaultValue = "") String tags,
			@RequestParam(value = "fileName") String fileName, @RequestBody(required = false) String contents) {
		logger.info("Starting runConvertUMLToCucumberGuice");
		logger.info("tags:" + tags);
		logger.info("fileName:" + fileName);
		ModelTransformerResponse mtr = convertObject(new ConvertUMLToCucumberGuice(tags, fa, new LoggerImpl(logger)),
				fileName, contents);
		logger.debug("response: " + mtr.toString());
		logger.info("Ending runConvertUMLToCucumberGuice");
		return mtr;
	}

	@PostMapping("/runConvertUMLToCucumberSpring")
	public ModelTransformerResponse runConvertUMLToCucumberSpring(
			@RequestParam(value = "tags", defaultValue = "") String tags,
			@RequestParam(value = "fileName") String fileName, @RequestBody(required = false) String contents) {
		logger.info("Starting runConvertUMLToCucumberSpring");
		logger.info("tags:" + tags);
		logger.info("fileName:" + fileName);
		ModelTransformerResponse mtr = convertObject(new ConvertUMLToCucumberSpring(tags, fa, new LoggerImpl(logger)),
				fileName, contents);
		logger.debug("response: " + mtr.toString());
		logger.info("Ending runConvertUMLToCucumberSpring");
		return mtr;
	}

}