package org.farhan.mbt.controller;

import java.util.List;

import org.farhan.mbt.asciidoctor.ConvertAsciidoctorToUML;
import org.farhan.mbt.asciidoctor.ConvertUMLToAsciidoctor;
import org.farhan.dsl.lang.IResourceRepository;
import org.farhan.mbt.model.TransformableFile;
import org.farhan.mbt.service.AsciiDoctorService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
@RequestMapping("/asciidoctor")
public class AsciiDoctorController implements ApplicationListener<ApplicationReadyEvent> {

	Logger logger = LoggerFactory.getLogger(AsciiDoctorController.class);
	// TODO this repo and any mojo should be in the service layer, not the
	// controller
	private final IResourceRepository repository;
	private final AsciiDoctorService service;

	@Autowired
	public AsciiDoctorController(IResourceRepository repository, AsciiDoctorService service) {
		this.repository = repository;
		this.service = service;
	}

	@DeleteMapping("/clearConvertAsciidoctorToUMLObjects")
	public void clearConvertAsciidoctorToUMLObjects(
			@RequestParam(value = "tags", defaultValue = "") String tags) {
		logger.info("Starting clearConvertAsciidoctorToUMLObjects");
		logger.info("tags:" + tags);
		service.clearObjects(new ConvertAsciidoctorToUML(tags, repository));
		logger.info("Ending clearConvertAsciidoctorToUMLObjects");
	}

	@GetMapping("/getConvertUMLToAsciidoctorObjectNames")
	public List<TransformableFile> getConvertUMLToAsciidoctorObjectNames(
			@RequestParam(value = "tags", defaultValue = "") String tags) {
		logger.info("Starting getConvertUMLToAsciidoctorObjectNames");
		List<TransformableFile> fileList = service.getObjectNames(
				new ConvertUMLToAsciidoctor(tags, repository),
				tags);
		logger.info("Ending getConvertUMLToAsciidoctorObjectNames");
		return fileList;
	}

	@Override
	public void onApplicationEvent(ApplicationReadyEvent event) {
	}

	@PostMapping("/runConvertAsciidoctorToUML")
	public TransformableFile runConvertAsciidoctorToUML(
			@RequestParam(value = "tags", defaultValue = "") String tags,
			@RequestParam(value = "fileName") String fileName, @RequestBody String contents) {
		logger.info("Starting runConvertAsciidoctorToUML");
		logger.info("tags:" + tags);
		logger.info("fileName:" + fileName);
		// TODO temp hack, nothing should be returned, update test code to not expect a
		// return value
		TransformableFile mtr = new TransformableFile(fileName, contents, tags);
		service.convertSourceObject(
				new ConvertAsciidoctorToUML(tags, repository),
				mtr);
		logger.info("Ending runConvertAsciidoctorToUML");
		return mtr;
	}

	@PostMapping("/runConvertUMLToAsciidoctor")
	public TransformableFile runConvertUMLToAsciidoctor(
			@RequestParam(value = "tags", defaultValue = "") String tags,
			@RequestParam(value = "fileName") String fileName, @RequestBody(required = false) String contents) {
		logger.info("Starting runConvertUMLToAsciidoctor");
		logger.info("tags:" + tags);
		logger.info("fileName:" + fileName);
		TransformableFile mtr = service.convertObject(
				new ConvertUMLToAsciidoctor(tags, repository),
				fileName, contents);
		logger.debug("response: " + mtr.toString());
		logger.info("Ending runConvertUMLToAsciidoctor");
		return mtr;
	}
}