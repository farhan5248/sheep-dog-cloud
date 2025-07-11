package org.farhan.mbt.controller;

import java.util.List;

import org.farhan.mbt.asciidoctor.ConvertAsciidoctorToUML;
import org.farhan.mbt.core.ObjectRepository;
import org.farhan.mbt.model.TransformableFile;
import org.farhan.mbt.service.CucumberService;
import org.farhan.mbt.service.cucumber.ConvertCucumberToUML;
import org.farhan.mbt.service.cucumber.ConvertUMLToCucumber;
import org.farhan.mbt.service.cucumber.ConvertUMLToCucumberGuice;
import org.farhan.mbt.service.cucumber.ConvertUMLToCucumberSpring;
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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@ConfigurationProperties(prefix = "sheepdog")
@RestController
@RequestMapping("/cucumber")
public class CucumberController implements ApplicationListener<ApplicationReadyEvent> {

	Logger logger = LoggerFactory.getLogger(CucumberController.class);
	// TODO this repo and any mojo should be in the service layer, not the
	// controller
	private final ObjectRepository repository;
	private final CucumberService service;

	@Value("${sheepdog.host:dev.sheepdogdev.io}")
	private String serverHost;

	@Value("${sheepdog.port:80}")
	private int serverPort;

	@Autowired
	public CucumberController(ObjectRepository repository, CucumberService service) {
		this.repository = repository;
		this.service = service;
	}

	@DeleteMapping("/clearConvertCucumberToUMLObjects")
	public void clearConvertCucumberToUMLObjects(
			@RequestParam(value = "tags", defaultValue = "") String tags) {
		logger.info("Starting clearConvertCucumberToUMLObjects");
		logger.info("tags:" + tags);
		service.clearObjects(new ConvertAsciidoctorToUML(tags, repository, new LoggerImpl(logger)));
		logger.info("Ending clearConvertCucumberToUMLObjects");
	}

	@GetMapping("/getConvertUMLToCucumberGuiceObjectNames")
	public List<TransformableFile> getConvertUMLToCucumberGuiceObjectNames(
			@RequestParam(value = "tags", defaultValue = "") String tags) {
		logger.info("Starting getConvertUMLToCucumberGuiceObjectNames");
		List<TransformableFile> fileList = service.getObjectNames(
				new ConvertUMLToCucumberGuice(tags, repository, new LoggerImpl(logger), serverHost, serverPort),
				tags);
		logger.info("Ending getConvertUMLToCucumberGuiceObjectNames");
		return fileList;
	}

	@GetMapping("/getConvertUMLToCucumberObjectNames")
	public List<TransformableFile> getConvertUMLToCucumberObjectNames(
			@RequestParam(value = "tags", defaultValue = "") String tags) {
		logger.info("Starting getConvertUMLToCucumberObjectNames");
		List<TransformableFile> fileList = service.getObjectNames(
				new ConvertUMLToCucumber(tags, repository, new LoggerImpl(logger), serverHost, serverPort), tags);
		logger.info("Ending getConvertUMLToCucumberObjectNames");
		return fileList;
	}

	@GetMapping("/getConvertUMLToCucumberSpringObjectNames")
	public List<TransformableFile> getConvertUMLToCucumberSpringObjectNames(
			@RequestParam(value = "tags", defaultValue = "") String tags) {
		logger.info("Starting getConvertUMLToCucumberSpringObjectNames");
		List<TransformableFile> fileList = service.getObjectNames(
				new ConvertUMLToCucumberSpring(tags, repository, new LoggerImpl(logger), serverHost, serverPort),
				tags);
		logger.info("Ending getConvertUMLToCucumberSpringObjectNames");
		return fileList;
	}

	@PostMapping("/runConvertCucumberToUML")
	public TransformableFile runConvertCucumberToUML(
			@RequestParam(value = "tags", defaultValue = "") String tags,
			@RequestParam(value = "fileName") String fileName, @RequestBody String contents) {
		logger.info("Starting runConvertCucumberToUML");
		logger.info("tags:" + tags);
		logger.info("fileName:" + fileName);
		TransformableFile mtr = new TransformableFile(fileName, contents, tags);
		service.convertSourceObject(
				new ConvertCucumberToUML(tags, repository, new LoggerImpl(logger)),
				mtr);
		logger.info("Ending runConvertCucumberToUML");
		return mtr;
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
	}

	@PostMapping("/runConvertUMLToCucumber")
	public TransformableFile runConvertUMLToCucumber(
			@RequestParam(value = "tags", defaultValue = "") String tags,
			@RequestParam(value = "fileName") String fileName, @RequestBody(required = false) String contents) {
		logger.info("Starting runConvertUMLToCucumber");
		logger.info("tags:" + tags);
		logger.info("fileName:" + fileName);
		TransformableFile mtr = service.convertObject(
				new ConvertUMLToCucumber(tags, repository, new LoggerImpl(logger), serverHost, serverPort),
				fileName, contents);
		logger.debug("response: " + mtr.toString());
		logger.info("Ending runConvertUMLToCucumber");
		return mtr;
	}

	@PostMapping("/runConvertUMLToCucumberGuice")
	public TransformableFile runConvertUMLToCucumberGuice(
			@RequestParam(value = "tags", defaultValue = "") String tags,
			@RequestParam(value = "fileName") String fileName, @RequestBody(required = false) String contents) {
		logger.info("Starting runConvertUMLToCucumberGuice");
		logger.info("tags:" + tags);
		logger.info("fileName:" + fileName);
		TransformableFile mtr = service.convertObject(
				new ConvertUMLToCucumberGuice(tags, repository, new LoggerImpl(logger), serverHost, serverPort),
				fileName, contents);
		logger.debug("response: " + mtr.toString());
		logger.info("Ending runConvertUMLToCucumberGuice");
		return mtr;
	}

	@PostMapping("/runConvertUMLToCucumberSpring")
	public TransformableFile runConvertUMLToCucumberSpring(
			@RequestParam(value = "tags", defaultValue = "") String tags,
			@RequestParam(value = "fileName") String fileName, @RequestBody(required = false) String contents) {
		logger.info("Starting runConvertUMLToCucumberSpring");
		logger.info("tags:" + tags);
		logger.info("fileName:" + fileName);
		TransformableFile mtr = service.convertObject(
				new ConvertUMLToCucumberSpring(tags, repository, new LoggerImpl(logger), serverHost, serverPort),
				fileName, contents);
		logger.debug("response: " + mtr.toString());
		logger.info("Ending runConvertUMLToCucumberSpring");
		return mtr;
	}

}