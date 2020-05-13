所有解析器，因为是对BeanDefinitionParser接口的统一实现，入口都是从parse函数开始的

查看类的顺序：
AspectJAutoProxyBeanDefinitionParser.parse() -> AopNamespaceUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary
-> AopConfigUtils.registerAspectJAnnotationAutoProxyCreatorIfNecessary() 
-> AnnotationAwareAspectJAutoProxyCreator类

AnnotationAwareAspectJAutoProxyCreator实现了BeanPostProcessor接口，
而实现 BeanPostProcessor 接口后，当 Spring加载这个Bean时
在实例化前会调用其 postProcessBeforeInstantiation 方法，
实例化结束后会调用 postProcessAfterInitialization 方法，
我们对于AOP逻辑的分析也由postProcessBeforeInstantiation方法开始。

AbstractAutoProxyCreator.postProcessAfterInitialization()
--> wrapIfNecessary()-->{ getAdvicesAndAdvisorsForBean() -> AbstractAdvisorAutoProxyCreator.getAdvicesAndAdvisorsForBean()
                                    --> findEligibleAdvisors() -->{ findCandidateAdvisors() -> AnnotationAwareAspectJAutoProxyCreator.findCandidateAdvisors()
                                                                                           -> BeanFactoryAspectJAdvisorsBuilder.buildAspectJAdvisors() 
                                                                                           -> ReflectiveAspectJAdvisorFactory.getAdvisors() --> getAdvisor()
                                                               }
                                                               --> { findAdvisorsThatCanApply() -> AopUtils.findAdvisorsThatCanApply()
                                                                                                --> canApply() --> canApply()
                                                               }
}
-->{ createProxy() --> { buildAdvisors() -> DefaultAdvisorAdapterRegistry.wrap()}
                   --> { getProxy() -> ProxyFactory.getProxy() -> ProxyCreatorSupport.createAopProxy() 
                                    -> DefaultAopProxyFactory.createAopProxy() -> JdkDynamicAopProxy.invoke() 
                                    -> ReflectiveMethodInvocation.proceed()
                       }
}


前置增强，大致的结构是在拦截器链中放置MethodBeforeAdviceInterceptor ，而在MethodBeforeAdviceInterceptor中又放置了AspectJMethodBeforeAdvice，并在调用invoke时首先串联调用。
但是在后置增强的时候却不一样，没有提供中间的类，而是直接在拦截器链中使用了中间的AspectJAfterAdvice

织入,创建bean实例的时候，在bean初始化完成后，再对其进行增强
doCreateBean() --> getEarlyBeanReference() -> AbstractAutowireCapableBeanFactory.getEarlyBeanReference()  -> AbstractAutoProxyCreator.getEarlyBeanReference
--> wrapIfNecessary()