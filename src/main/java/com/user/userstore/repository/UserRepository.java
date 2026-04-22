package com.user.userstore.repository;

import com.user.userstore.model.User;
import org.springframework.data.gemfire.repository.GemfireRepository;

public interface UserRepository extends GemfireRepository<User, String> {

}
