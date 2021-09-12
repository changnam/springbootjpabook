package com.honsoft.repository;

import java.util.UUID;

import org.springframework.data.repository.CrudRepository;

import com.honsoft.entity.BookEntity;

public interface BookRepository extends CrudRepository<BookEntity, UUID>{

}
