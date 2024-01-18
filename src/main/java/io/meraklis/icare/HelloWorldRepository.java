package io.meraklis.icare;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "hello", path = "hello")
public interface HelloWorldRepository extends MongoRepository<HelloWorld, String> {
}
