**Spring 中的事务是以AOP 为基础的**
在JDBC中，事务默认是自动提交的，每次执行一个 SQL 语句时，如果执行成功，就会向数据库自动提交，而不能回滚。
为了让多个 SQL 语句作为一个事务执行，需调用 Connection 对象的 setAutoCommit(false); 以取消自动提交事务：
在所有的 SQL 语句都成功执行后，调用 commit(); 方法提交事务；在出现异常时，调用 rollback(); 方法回滚事务，一般再catch模块中执行回滚操作。
可以通过Connection的getAutoCommit()方法来获得当前事务的提交方式。

有的时候可能并不需要将一整个事务进行回滚，一个复杂的事务可能由几个一致性的阶段组成
保存点就是在一个事务中，插入几个还原点，再出现问题时，可以及时的撤回到这个地方来
当撤回到一个还原点时，事务还在，仍在进行中，所以还需要再次的COMMIT，这次的COMMIT，保存点以下的执行相当于不存在。

TransactionAspectSupport里面有个transactionInfoHolder的ThreadLocal对象，
用于把TxInfo绑定到线程。那么这样在我们的业务代码或者其他切面中，我们可以拿到TxInfo，也能拿到TxStatus。
拿到TxStatus我们就可以调用setRollbackOnly来打标以手动控制事务必须回滚

TxNamespaceHandler.init() -> AnnotationDrivenBeanDefinitionParser.parse() 
--> configureAutoProxyCreator()
      { -> AopNamespaceUtils.registerAutoProxyCreatorIfNecessary()} 
      { -> TransactionInterceptor.invoke() 
            -> TransactionAspectSupport.invokeWithinTransaction() 
                    { --> getTransactionAttribute() -> TransactionAttributeSource.getTransactionAttribute() 
                           -> AbstractFallbackTransactionAttributeSource.computeTransactionAttribute()
                           -> AnnotationTransactionAttributeSource.findTransactionAttribute() --> determineTransactionAttribute()
                           --> parseTransactionAnnotation() -> SpringTransactionAnnotationParser.parseTransactionAnnotation()
                           --> parseTransactionAnnotation()
                    }
                    { --> createTransactionIfNecessary() -> AbstractPlatformTransactionManager.getTransaction()
                           { --> doGetTransaction()
                           }
                           { --> handleExistingTransaction() --> suspend()
                           }
                           { --> doBegin() --> prepareConnectionForTransaction()
                           }
                           { --> prepareSynchronization()
                           }     
                    }
                    { --> completeTransactionAfterThrowing() --> rollback() 
                                 --> processRollback()
                    }
                    { --> commitTransactionAfterReturning() ->AbstractPlatformTransactionManager.commit()
                                 --> processCommit()
                    }
      }