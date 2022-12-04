package org.learn.duplicatefinder.filesop;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.filefilter.DirectoryFileFilter;
import org.apache.commons.io.filefilter.RegexFileFilter;
import org.learn.duplicatefinder.dao.FileMetaRepository;
import org.learn.duplicatefinder.sqlite.FileMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.DigestUtils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@Component
public class BaseFileOperations {
    private static Logger logger = LoggerFactory.getLogger(BaseFileOperations.class);

    @Autowired
    private FileMetaRepository fileMetaRepository;
    public Collection<File> getAllFilesFromDirectory(String path) {
        Collection<File> files = FileUtils.listFiles(
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
        logger.info(MessageFormat.format(" >> Locations to monitor: {0}", directoryList));
        return directoryList;
    }

    public void populateFileData() throws IOException {
        ArrayList<FileMeta> fileMetas = new ArrayList<>();

        logger.info(" > Getting directories to crawl!");
        ArrayList<String> directoriesToCrawl = getDirectoriesToCrawl();

        logger.info(" > Starting crawling! ");
        for (String dir : directoriesToCrawl) {
            Collection<File> allFilesFromDirectory = getAllFilesFromDirectory(dir);
            Iterator<File> iterator = allFilesFromDirectory.iterator();

            while (iterator.hasNext()) {
                File nextFile = iterator.next();
                BasicFileAttributes nextFileAttribute = Files.readAttributes(nextFile.toPath(), BasicFileAttributes.class);
                FileMeta currentFileData = new FileMeta(nextFile.getName(),
                        nextFile.getAbsolutePath(),
                        nextFileAttribute.size(),
                        nextFileAttribute.creationTime().toMillis(),
                        nextFileAttribute.lastModifiedTime().toMillis(),
                        getMd5Hash(nextFile));

                fileMetas.add(currentFileData);
            }
            logger.info(" > List created for: {0}", dir);
        }

        logger.info("> Triggering file save!");
        fileMetaRepository.saveAll(fileMetas);
    }

    public void appendListToFile(ArrayList<FileMeta> fileMetas) {
        for (FileMeta fileMeta : fileMetas) {
            try {
                Files.write(Paths.get("allFileRecords.csv"),
                        MessageFormat.format("\"{0}\", \"{1}\", \"{2}\", \"{3}\", \"{4}\", \"{5}\"", fileMeta.getFileName(),
                                fileMeta.getCreateDate(),
                                fileMeta.getModifiedDate(),
                                fileMeta.getSize(),
                                fileMeta.getAbsolutePath(),
                                fileMeta.getMd5()).getBytes(),
                        StandardOpenOption.APPEND);
            }catch (IOException e) {
                logger.error(e.getMessage(), e);
            }
        }

    }
}
