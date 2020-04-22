*TODO 表示可进一步点击进去看注释*

ApplicationContext和BeanFactory两者都是用于加载Bean的，但是相比之下，ApplicationContext提供了更多的扩展功能，
简单一点说：ApplicationContext包含BeanFactory 的所有功能。通常建议比BeanFactory优先


查看类的主要顺序：ClassPathXmlApplicationContext 
--> refresh() -> AbstractApplicationContext.refresh() 
--> {prepareRefresh()}
--> {obtainFreshBeanFactory() --> refreshBeanFactory() -> AbstractRefreshableApplicationContext.customizeBeanFactory() 
                              -> XmlWebApplicationContext.loadBeanDefinitions() 
     }
--> {prepareBeanFactory()}
--> {registerBeanPostProcessors() -> PostProcessorRegistrationDelegate.registerBeanPostProcessors()}
--> {finishBeanFactoryInitialization() -> DefaultListableBeanFactory.preInstantiateSingletons() }