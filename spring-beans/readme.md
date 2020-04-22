*TODO 表示可进一步点击进去看注释*
IOC: IoC容器其实就是DefaultListableBeanFactory,它里面有一个map类型的beanDefinitionMap变量，来存储注册的bean


查看主要类的顺序：
bean的注册：XMLBeanFactory **DefaultListableBeanFactory** 
-> XmlBeanDefinitionReader.registerBeanDefinitions() -> DefaultBeanDefinitionDocumentReader.registerBeanDefinitions() 
--> doRegisterBeanDefinitions() -->parseBeanDefinitions() -->parseDefaultElement() --> processBeanDefinition()
->BeanDefinitionParserDelegate.parseBeanDefinitionElement()->BeanDefinitionReaderUtils.registerBeanDefinition()
->BeanDefinitionReaderUtils.registerBeanDefinition()->DefaultListableBeanFactory.registerBeanDefinition()
 
bean的加载：BeanFactory -> AbstractBeanFactory.doGetBean() 
{--> getSingleton()->DefaultSingletonBeanRegistry.getSingleton()}
{--> getObjectForBeanInstance()->FactoryBeanRegistrySupport.getObjectFromFactoryBean() -->doGetObjectFromFactoryBean()且postProcessObjectFromFactoryBean()}
{--> createBean() -> AbstractAutowireCapableBeanFactory.createBean() 
                  -->{prepareMethodOverrides() -> AbstractBeanDefinition.prepareMethodOverrides() --> prepareMethodOverride()}
                  -->{resolveBeforeInstantiation() --> applyBeanPostProcessorsBeforeInstantiation()}
                  -->{doCreateBean() --> {createBeanInstance() --> autowireConstructor() -> ConstructorResolver.autowireConstructor() -> SimpleInstantiationStrategy.instantiate()}
                                     --> {populateBean() {--> autowireByName()}
                                                         {--> autowireByType() -> DefaultListableBeanFactory.resolveDependency()}
                                          }
                                     --> {initializeBean()} 
                                     --> {registerDisposableBeanIfNecessary()}                   
                     }
}

注：（1）BeanFactory是一个Bean工厂，在一定程度上我们可以简单理解为它就是我们平常所说的Spring容器(注意这里说的是简单理解为容器)，它完成了Bean的创建、自动装配等过程，存储了创建完成的单例Bean。
而FactoryBean通过名字看，我们可以猜出它是Bean，但它是一个特殊的Bean，FactoryBean的特殊之处在于它可以向容器中注册两个Bean，一个是它本身，一个是FactoryBean.getObject()方法返回值所代表的Bean。
（2）当在IOC容器中的Bean实现了FactoryBean后，通过getBean(String BeanName)获取到的Bean对象并不是FactoryBean的实现类对象，而是这个实现类中的getObject()方法返回的对象。要想获取FactoryBean的实现类，就要getBean(&BeanName)，在BeanName之前加上&。