package org.tdtu.ecommerceapi.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.NoRepositoryBean;

@NoRepositoryBean
public interface BaseRepository<M, ID>
        extends MongoRepository<M, ID>, QuerydslPredicateExecutor<M> {
}