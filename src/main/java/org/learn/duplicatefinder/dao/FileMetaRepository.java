package org.learn.duplicatefinder.dao;

import org.learn.duplicatefinder.sqlite.FileMeta;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FileMetaRepository extends CrudRepository<FileMeta, Long> {
}
