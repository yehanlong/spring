/*
 * Copyright 2002-2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.context;

/**
 * Callback interface for initializing a Spring {@link ConfigurableApplicationContext}
 * prior to being {@linkplain ConfigurableApplicationContext#refresh() refreshed}.
 *
 * <p>Typically used within web applications that require some programmatic initialization
 * of the application context. For example, registering property sources or activating
 * profiles against the {@linkplain ConfigurableApplicationContext#getEnvironment()
 * context's environment}. See {@code ContextLoader} and {@code FrameworkServlet} support
 * for declaring a "contextInitializerClasses" context-param and init-param, respectively.
 *
 * <p>{@code ApplicationContextInitializer} processors are encouraged to detect
 * whether Spring's {@link org.springframework.core.Ordered Ordered} interface has been
 * implemented or if the @{@link org.springframework.core.annotation.Order Order}
 * annotation is present and to sort instances accordingly if so prior to invocation.
 *
 * @author Chris Beams
 * @since 3.1
 * @see org.springframework.web.context.ContextLoader#customizeContext
 * @see org.springframework.web.context.ContextLoader#CONTEXT_INITIALIZER_CLASSES_PARAM
 * @see org.springframework.web.servlet.FrameworkServlet#setContextInitializerClasses
 * @see org.springframework.web.servlet.FrameworkServlet#applyInitializers
 */

//这个类的主要作用就是在ConfigurableApplicationContext类型(或者子类型)的
// ApplicationContext做refresh之前，允许我们对ConfiurableApplicationContext的实例做进一步的设置和处理

//是在spring容器刷新之前执行的一个回调函数。是在ConfigurableApplicationContext#refresh() 之前调用
// （当spring框架内部执行 ConfigurableApplicationContext#refresh() 方法的时候或者在SpringBoot的run()执行时），
// 作用是初始化Spring ConfigurableApplicationContext的回调接口

//在一个Springboot应用中，classpath上会包含很多jar包，
// 有些jar包需要在ConfigurableApplicationContext#refresh()调用之前对应用上下文做一些初始化动作，
// 因此它们会提供自己的ApplicationContextInitializer实现类，
// 然后放在自己的META-INF/spring.factories属性文件中，这样相应的ApplicationContextInitializer实现类就会被SpringApplication#initialize发现
public interface ApplicationContextInitializer<C extends ConfigurableApplicationContext> {

	/**
	 * Initialize the given application context.
	 * @param applicationContext the application to configure
	 */
	void initialize(C applicationContext);

}
