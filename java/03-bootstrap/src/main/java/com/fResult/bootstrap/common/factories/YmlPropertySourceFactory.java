package com.fResult.bootstrap.common.factories;

import jakarta.annotation.Nonnull;
import java.util.Optional;
import java.util.Properties;
import java.util.function.Function;
import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;
import org.springframework.lang.NonNull;
import org.springframework.lang.Nullable;

public class YmlPropertySourceFactory implements PropertySourceFactory {
  @Nonnull
  @Override
  public org.springframework.core.env.PropertySource<?> createPropertySource(
      @Nullable String name, @NonNull EncodedResource resource) {

    final var fileName = Optional.ofNullable(name).orElse(resource.getResource().getFilename());
    final var factoryBean = new YamlPropertiesFactoryBean();
    factoryBean.setResources(resource.getResource());
    final var properties = factoryBean.getObject();

    return Optional.ofNullable(fileName)
        .flatMap(buildPropertiesPropertySourceOpt(properties))
        .orElseThrow();
  }

  private Function<String, Optional<PropertiesPropertySource>> buildPropertiesPropertySourceOpt(
      @Nullable Properties props) {

    return fileName -> Optional.ofNullable(props).map(buildPropertiesPropertySource(fileName));
  }

  private Function<Properties, PropertiesPropertySource> buildPropertiesPropertySource(
      @Nonnull String fileName) {

    return props -> new PropertiesPropertySource(fileName, props);
  }
}
