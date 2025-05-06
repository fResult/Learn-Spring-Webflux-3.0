package com.fResult.bootstrap.common.factories

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.Resource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory
import java.util.*

class YmlPropertySourceFactory : PropertySourceFactory {
  override fun createPropertySource(name: String?, encodedResource: EncodedResource): PropertySource<*> {
    val fileName = name ?: encodedResource.resource.filename ?: "yamlPropertySource"
    val factoryBean = YamlPropertiesFactoryBean().apply {
      setResources(encodedResource.resource)
    }
    val properties = encodedResource.resource
      .takeIf(Resource::exists)
      ?.let { factoryBean.`object` }
      ?: Properties()

    return PropertiesPropertySource(fileName, properties)
  }
}
