/*
 * Copyright 2012-2015, the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * http://www.apache.org/licenses/LICENSE-2.0
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.flipkart.aesop.runtime.bootstrap;

import org.springframework.beans.factory.FactoryBean;

import com.flipkart.aesop.runtime.bootstrap.configs.BootstrapConfig;
import com.flipkart.aesop.runtime.bootstrap.consumer.SourceEventConsumer;
import com.flipkart.aesop.runtime.bootstrap.producer.BlockingEventProducer;
import com.linkedin.databus.core.util.ConfigLoader;
import com.linkedin.databus2.core.container.netty.ServerContainer;

/**
 * The Spring factory bean for creating {@link BlockingBootstrapServer} instances based on configured properties
 * @author nrbafna
 */
public class BlockingBootstrapServerFactory implements FactoryBean<BlockingBootstrapServer>
{
	private BootstrapConfig bootstrapConfig;
	private BlockingEventProducer producer;
	private SourceEventConsumer consumer;

	@Override
	public BlockingBootstrapServer getObject() throws Exception
	{
		ServerContainer.Config config = new ServerContainer.Config();
		ConfigLoader<ServerContainer.StaticConfig> configLoader =
		        new ConfigLoader<ServerContainer.StaticConfig>(BootstrapConfig.BOOTSTRAP_PROPERTIES_PREFIX, config);

		ServerContainer.StaticConfig staticConfig =
		        configLoader.loadConfig(this.bootstrapConfig.getBootstrapProperties());
		BlockingBootstrapServer bootstrapServer = new BlockingBootstrapServer(staticConfig);
		bootstrapServer.registerProducer(producer);
		bootstrapServer.registerConsumer(consumer);
		return bootstrapServer;
	}

	@Override
	public Class<?> getObjectType()
	{
		return BlockingBootstrapServer.class;
	}

	@Override
	public boolean isSingleton()
	{
		return true;
	}

	public void setBootstrapConfig(BootstrapConfig bootstrapConfig)
	{
		this.bootstrapConfig = bootstrapConfig;
	}

	public void setProducer(BlockingEventProducer producer)
	{
		this.producer = producer;
	}

	public void setConsumer(SourceEventConsumer consumer)
	{
		this.consumer = consumer;
	}
}
