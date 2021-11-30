package com.example.elastic.shliffen.repository;

import com.example.elastic.shliffen.model.base.FileInDB;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import java.util.Optional;

public interface FilesRepository extends ElasticsearchRepository<FileInDB, Long> {

    Iterable<FileInDB> findAll();

    @Override
    Optional<FileInDB> findById(Long id);

    void deleteById(Long aLong);
}
