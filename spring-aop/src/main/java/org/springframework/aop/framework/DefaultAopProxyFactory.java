/*
 * Copyright 2002-2015 the original author or authors.
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

package org.springframework.aop.framework;

import java.io.Serializable;
import java.lang.reflect.Proxy;

import org.springframework.aop.SpringProxy;

/**
 * Default {@link AopProxyFactory} implementation, creating either a CGLIB proxy
 * or a JDK dynamic proxy.
 *
 * <p>Creates a CGLIB proxy if one the following is true for a given
 * {@link AdvisedSupport} instance:
 * <ul>
 * <li>the {@code optimize} flag is set
 * <li>the {@code proxyTargetClass} flag is set
 * <li>no proxy interfaces have been specified
 * </ul>
 *
 * <p>In general, specify {@code proxyTargetClass} to enforce a CGLIB proxy,
 * or specify one or more interfaces to use a JDK dynamic proxy.
 *
 * @author Rod Johnson
 * @author Juergen Hoeller
 * @since 12.03.2004
 * @see AdvisedSupport#setOptimize
 * @see AdvisedSupport#setProxyTargetClass
 * @see AdvisedSupport#setInterfaces
 */
@SuppressWarnings("serial")
public class DefaultAopProxyFactory implements AopProxyFactory, Serializable {

	@Override
	public AopProxy createAopProxy(AdvisedSupport config) throws AopConfigException {
		//三个方面影响spring选择哪个代理
		//(1)optimize：用来控制通过CGLIB 创建的代理是否使用激进的优化策略。除非完全了解
		//			   AOP代理如何处理优化，否则不推荐用户使用这个设直。
		//			   目前这个属性仅用于CGLIB代理，对于JDK 动态代理（默认代理）无效
		//(2)proxyTargetClass：这个属性为true时，目标类本身被代理而不是目标类的接口。
		// 					   如果为true, CGLIB 代理将被创建
		//(3)hasNoUserSuppliedProxyInterfaces ：是否存在代理接口。



		// 对生成代理策略进行优化||强制使用CGLIB来实现代理||不适用接口代理
		if (config.isOptimize() || config.isProxyTargetClass() || hasNoUserSuppliedProxyInterfaces(config)) {
			Class<?> targetClass = config.getTargetClass();
			if (targetClass == null) {
				throw new AopConfigException("TargetSource cannot determine target class: " +
						"Either an interface or a target is required for proxy creation.");
			}
			// 目标类为接口，JDK代理
			if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
				return new JdkDynamicAopProxy(config);
			}
			// CGLIB代理

			//CGLIB 中对于方法的拦截是通过将自定义的拦截器（实现MethodInterceptor接口）
			// 加入Callback中并在调用代理时直接激活拦截器中的intercept 方法来实现的，
			// 那么在getCallback中正是实现了这样一个目的，DynamicAdvisedInterceptor继承自MethodInterceptor ，
			// 加入Callback 中后，在再次调用代理时会直接调用DynamicAdvisedInterceptor 中的intercept 方法，由此推断，对于CGLIB 方式实现的代理，
			// 其核心逻辑必然在DynamicAdvisedInterceptor中的intercept中
			// intercept中实现代理中的invoke 方法大同小异，都是首先构造链，然后封装
			// 此链进行串联调用，稍有些区别就是在JDK 中直接构造ReflectiveMethodInvocation ，
			// 而在cglib中使用CglibMethodInvocation 。CglibMethodInvocation继承自ReflectiveMethodInvocation，
			// 但是proceed 方法并没有重写

			return new ObjenesisCglibAopProxy(config);
		}
		else {
			// JDK代理


			//JDK代理对于InvocationHandler的创建是最为核心的，在自定义的InvocationHandler中需要重写3个函数。
			//构造函数，将代理的对象传入。
			//invoke方法，此方法中实现了AOP 增强的所有逻辑。
			//getProxy方法，此方法千篇一律，但是必不可少

			//TODO invoke
			return new JdkDynamicAopProxy(config);
		}
	}

	/**
	 * Determine whether the supplied {@link AdvisedSupport} has only the
	 * {@link org.springframework.aop.SpringProxy} interface specified
	 * (or no proxy interfaces specified at all).
	 */
	private boolean hasNoUserSuppliedProxyInterfaces(AdvisedSupport config) {
		Class<?>[] ifcs = config.getProxiedInterfaces();
		return (ifcs.length == 0 || (ifcs.length == 1 && SpringProxy.class.isAssignableFrom(ifcs[0])));
	}

}
