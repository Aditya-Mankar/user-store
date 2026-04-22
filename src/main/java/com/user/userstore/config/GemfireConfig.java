package com.user.userstore.config;

import com.user.userstore.model.User;
import org.springframework.data.gemfire.cache.config.EnableGemfireCaching;
import org.springframework.data.gemfire.config.annotation.ClientCacheApplication;
import org.springframework.data.gemfire.config.annotation.EnableCachingDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnableEntityDefinedRegions;
import org.springframework.data.gemfire.config.annotation.EnablePdx;
import org.springframework.data.gemfire.repository.config.EnableGemfireRepositories;

@EnableGemfireCaching
@EnableCachingDefinedRegions
@EnablePdx
@EnableEntityDefinedRegions(basePackageClasses = User.class)
@EnableGemfireRepositories(basePackages = "com.user.userstore.repository")
@ClientCacheApplication
public class GemfireConfig {
}
