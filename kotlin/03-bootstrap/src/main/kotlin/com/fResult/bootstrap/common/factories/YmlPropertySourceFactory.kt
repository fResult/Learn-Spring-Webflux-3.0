package com.fResult.bootstrap.common.factories

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean
import org.springframework.core.env.PropertiesPropertySource
import org.springframework.core.env.PropertySource
import org.springframework.core.io.support.EncodedResource
import org.springframework.core.io.support.PropertySourceFactory

class YmlPropertySourceFactory : PropertySourceFactory {
  override fun createPropertySource(name: String?, encodedResource: EncodedResource): PropertySource<*> {
    val fileName = name ?: encodedResource.resource.filename
    val factoryBean = YamlPropertiesFactoryBean()
    factoryBean.setResources(encodedResource.resource)
    val properties = factoryBean.`object`

    return fileName?.let { properties?.let { props -> PropertiesPropertySource(it, props) } }
      ?: throw NoSuchElementException()
  }
}
