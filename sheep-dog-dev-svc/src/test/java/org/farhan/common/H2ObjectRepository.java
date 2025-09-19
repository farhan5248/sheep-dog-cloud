package org.farhan.common;

import java.util.ArrayList;
import java.util.List;

import org.farhan.mbt.core.IObjectRepository;
import org.farhan.mbt.model.ModelSourceFile;
import org.farhan.mbt.repository.ModelSourceFileRepository;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Repository;

@Repository
@Profile("surefire")
public class H2ObjectRepository implements IObjectRepository {

	private final ModelSourceFileRepository repository;

	public H2ObjectRepository(ModelSourceFileRepository repository) {
		this.repository = repository;
	}

	@Override
	public ArrayList<String> list(String tags, String path, String extension) {
		ArrayList<String> files = new ArrayList<>();
		String pattern = tags + "/" + path + "%" + extension;
		List<ModelSourceFile> results = repository.findByFileNameLike(pattern);

		for (ModelSourceFile msf : results) {
			files.add(msf.getFileName().replace(tags + "/", ""));
		}
		return files;
	}

	@Override
	public String get(String tags, String path) throws Exception {
		ModelSourceFile result = repository.findByFileName(tags + "/" + path);
		return result == null ? null : result.getFileContent();
	}

	@Override
	public void put(String tags, String path, String content) throws Exception {
		String fullPath = tags + "/" + path;
		ModelSourceFile file = repository.findByFileName(fullPath);

		if (file != null) {
			file.setFileContent(content);
			repository.save(file);
		} else {
			repository.save(new ModelSourceFile(fullPath, content));
		}
	}

	@Override
	public boolean contains(String tags, String path) {
		return repository.findByFileName(tags + "/" + path) != null;
	}

	@Override
	public void clear(String tags) {
		List<ModelSourceFile> filesToDelete = repository.findByFileNameLike(tags + "/%");
		repository.deleteAll(filesToDelete);
	}

	@Override
	public void delete(String tags, String path) {
		// TODO Auto-generated method stub
		
	}
}
