package org.farhan.common;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.farhan.mbt.convert.ObjectRepository;
import org.farhan.mbt.model.TransformableFile;
import org.springframework.context.annotation.Profile;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
@Profile("surefire")
public class H2ObjectRepository implements ObjectRepository {

	private JdbcTemplate jdbcTemplate;

	public H2ObjectRepository(JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	private TransformableFile mapRowToModelSourceFile(ResultSet row, int rowNum) throws SQLException {
		return new TransformableFile(row.getString("file_name"), row.getString("file_content"));
	}

	@Override
	public ArrayList<String> list(String tags, String path, String extension) {
		ArrayList<String> files = new ArrayList<String>();
		for (TransformableFile ms : jdbcTemplate
				.query("select file_name, file_content from model_source_files where file_name like '" + tags + "/"
						+ path + "%" + extension + "'", this::mapRowToModelSourceFile)) {
			files.add(ms.getFileName().replace(tags + "/", ""));
		}
		return files;
	}

	@Override
	public String get(String tags, String path) throws Exception {
		List<TransformableFile> results = jdbcTemplate.query(
				"select file_name, file_content from model_source_files where file_name='" + tags + "/" + path + "'",
				this::mapRowToModelSourceFile);
		return results.size() == 0 ? null : results.get(0).getFileContent();
	}

	@Override
	public void put(String tags, String path, String content) throws Exception {
		if (contains(tags, path)) {
			jdbcTemplate.update("update model_source_files set file_content = '" + content + "' where file_name='"
					+ tags + "/" + path + "'");
		} else {
			jdbcTemplate.update("insert into model_source_files (file_name, file_content) values (?, ?)",
					tags + "/" + path, content);
		}
	}

	@Override
	public boolean contains(String tags, String path) {
		List<TransformableFile> results = jdbcTemplate.query(
				"select file_name, file_content from model_source_files where file_name='" + tags + "/" + path + "'",
				this::mapRowToModelSourceFile);
		return results.size() != 0;
	}

	@Override
	public void clear(String tags) {
		jdbcTemplate.update("delete from model_source_files where file_name like '" + tags + "/%'");
	}

}
