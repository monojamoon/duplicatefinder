package org.learn.duplicatefinder.filesop;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.io.File;
import java.text.MessageFormat;
import java.util.Collection;

@Component
public class BaseFileOperations {
    private static Logger logger = LoggerFactory.getLogger(BaseFileOperations.class);
    public Collection getAllFilesFromDirectory(String path) {
        Collection files = FileUtils.listFiles(
                new File(path),
                new RegexFileFilter("^(.*?)"),
                DirectoryFileFilter.DIRECTORY
        );
        logger.info(MessageFormat.format("For path: {0} | number of files extracted: {1}", path, files.size()));
        return files;
    }
}
