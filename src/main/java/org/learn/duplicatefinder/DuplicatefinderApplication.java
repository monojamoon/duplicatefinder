package org.learn.duplicatefinder;

import org.learn.duplicatefinder.filesop.BaseFileOperations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.io.File;
import java.util.Collection;
import java.util.Iterator;

@SpringBootApplication
public class DuplicatefinderApplication implements CommandLineRunner {
	@Autowired
	private BaseFileOperations baseFileOperations;

	private static Logger logger = LoggerFactory.getLogger(DuplicatefinderApplication.class);

	public static void main(String[] args) {
		SpringApplication.run(DuplicatefinderApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		Collection filesCollection = baseFileOperations.getAllFilesFromDirectory("/home/kakarot/Documents/dump");
		Iterator iterator = filesCollection.iterator();

		while (iterator.hasNext()) {
			logger.info(String.valueOf(((File)iterator.next()).lastModified()));
		}

		logger.info("Final Size: " + filesCollection.size());
	}
}
