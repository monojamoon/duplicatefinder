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

import java.util.*;

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
//		HashMap<String, ArrayList<FileMeta>> dbContentsInMap = getDbContentsInMap();


	}

	private HashMap<String, ArrayList<FileMeta>> getDbContentsInMap() {
		HashMap<String, ArrayList<FileMeta>> dbBaseContainer = new HashMap<>();
		Iterable<FileMeta> dbData = fileMetaRepository.findAll();

		Iterator<FileMeta> iterator = dbData.iterator();

		while (iterator.hasNext()) {
			FileMeta next = iterator.next();

			if (dbBaseContainer.containsKey(next.getMd5())) {
				ArrayList<FileMeta> fileMetas = dbBaseContainer.get(next.getMd5());
				fileMetas.add(next);
			} else {
				ArrayList<FileMeta> fileList = new ArrayList<>();
				fileList.add(next);
				dbBaseContainer.put(next.getMd5(), fileList);
			}
		}

		return dbBaseContainer;
	}
}
