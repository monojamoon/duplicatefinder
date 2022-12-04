package org.learn.duplicatefinder;

import org.learn.duplicatefinder.dao.FileMetaRepository;
import org.learn.duplicatefinder.filesop.BaseFileOperations;
import org.learn.duplicatefinder.sqlite.FileMeta;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;

@SpringBootApplication
public class DuplicatefinderApplication implements CommandLineRunner {
	@Autowired
	private BaseFileOperations baseFileOperations;

	@Autowired
	private FileMetaRepository fileMetaRepository;

	private static Logger logger = LoggerFactory.getLogger(DuplicatefinderApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DuplicatefinderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		baseFileOperations.populateFileData();
	}
}
