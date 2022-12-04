package org.learn.duplicatefinder.filesop;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;

import java.io.File;
import java.util.Collection;

public class BaseFileOperations {
    public void getAllFilesFromDirectory(String path) {
        Collection files = FileUtils.listFiles(
                new File(path),
                new RegexFileFilter("^(.*?)"),
                DirectoryFileFilter.DIRECTORY
        );
    }
}
