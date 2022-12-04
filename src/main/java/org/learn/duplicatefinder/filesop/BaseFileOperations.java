package org.learn.duplicatefinder.filesop;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.MessageFormat;
import java.util.ArrayList;
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

    public String getMd5Hash(File file) {
        try (InputStream is = Files.newInputStream(Paths.get(file.getAbsolutePath()))) {
            String checksum = DigestUtils.md5DigestAsHex(is);
            return checksum;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public ArrayList<String> getDirectoriesToCrawl() throws IOException {
        ArrayList<String> directoryList = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader("src/main/resources/directories.list"))) {
            String line;
            while ((line = br.readLine()) != null) {
                directoryList.add(line.trim());
            }
        }
        logger.info(MessageFormat.format("Locations to monitor: {0}", directoryList));
        return directoryList;
    }
}
