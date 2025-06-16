package org.farhan.mbt.service;

import java.util.ArrayList;

import org.farhan.mbt.convert.Converter;
import org.farhan.mbt.exception.TransformationException;
import org.farhan.mbt.model.TransformableFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class TransformationService {

    private static final Logger logger = LoggerFactory.getLogger(TransformationService.class);

    public TransformableFile convertObject(Converter mojo, String fileName, String contents) {
        try {
            TransformableFile mtr = new TransformableFile(fileName,
                    mojo.convertFile(fileName, contents == null ? "" : contents));
            logger.debug("response: " + mtr.toString());
            return mtr;
        } catch (Exception e) {
            logger.error("Error checking queue status", e);
            throw new TransformationException("Error converting object", e);
        }
    }

    public void clearObjects(Converter mojo) {
        try {
            mojo.clearProjects();
        } catch (Exception e) {
            logger.error("Error checking queue status", e);
            throw new TransformationException("Error clearing objects", e);
        }
    }

    public ArrayList<TransformableFile> getObjectNames(Converter mojo, String tags) {
        ArrayList<TransformableFile> fileList = new ArrayList<TransformableFile>();
        try {
            // TODO this is temp, there should be a separate class like the ObjectRepository
            // if not the object repo itself. For a given tag, it should keep track of the
            // source files and output files checksums
            for (String fileName : (mojo).getFileNames()) {
                fileList.add(new TransformableFile(fileName, ""));
                logger.debug("fileName: " + fileName);
            }
            return fileList;
        } catch (Exception e) {
            logger.error("Error checking queue status", e);
            throw new TransformationException("Error getting object names", e);
        }
    }
}
