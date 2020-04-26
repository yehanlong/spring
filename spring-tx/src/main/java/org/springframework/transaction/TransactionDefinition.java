/*
 * Copyright 2002-2018 the original author or authors.
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

package org.springframework.transaction;

import java.sql.Connection;

import org.springframework.lang.Nullable;

/**
 * Interface that defines Spring-compliant transaction properties.
 * Based on the propagation behavior definitions analogous to EJB CMT attributes.
 *
 * <p>Note that isolation level and timeout settings will not get applied unless
 * an actual new transaction gets started. As only {@link #PROPAGATION_REQUIRED},
 * {@link #PROPAGATION_REQUIRES_NEW} and {@link #PROPAGATION_NESTED} can cause
 * that, it usually doesn't make sense to specify those settings in other cases.
 * Furthermore, be aware that not all transaction managers will support those
 * advanced features and thus might throw corresponding exceptions when given
 * non-default values.
 *
 * <p>The {@link #isReadOnly() read-only flag} applies to any transaction context,
 * whether backed by an actual resource transaction or operating non-transactionally
 * at the resource level. In the latter case, the flag will only apply to managed
 * resources within the application, such as a Hibernate {@code Session}.
 *
 * @author Juergen Hoeller
 * @since 08.05.2003
 * @see PlatformTransactionManager#getTransaction(TransactionDefinition)
 * @see org.springframework.transaction.support.DefaultTransactionDefinition
 * @see org.springframework.transaction.interceptor.TransactionAttribute
 */
public interface TransactionDefinition {

	//事务传播行为类型：如果当前没有事务，就新建一个事务，如果已经存在一个事务中，加入到这个事务中。
	int PROPAGATION_REQUIRED = 0;

	//事务传播行为类型：支持当前事务，如果当前没有事务，就以非事务方式执行。
	int PROPAGATION_SUPPORTS = 1;

	//事务传播行为类型：当前如果有事务，Spring就会使用该事务；否则会抛出异常
	int PROPAGATION_MANDATORY = 2;

	//事务传播行为类型：新建事务，如果当前存在事务，把当前事务挂起。
	int PROPAGATION_REQUIRES_NEW = 3;

	//事务传播行为类型：以非事务方式执行操作，如果当前存在事务，就把当前事务挂起。
	int PROPAGATION_NOT_SUPPORTED = 4;

	//事务传播行为类型：即使当前有事务，Spring也会在非事务环境下执行。如果当前有事务，则抛出异常
	int PROPAGATION_NEVER = 5;

	//隔离级别：默认的隔离级别（对mysql数据库来说就是ISOLATION_ READ_COMMITTED，可以重复读）
	int PROPAGATION_NESTED = 6;


	//隔离级别：读未提交(最低)
	int ISOLATION_DEFAULT = -1;

	//隔离级别：读未提交(最低)
	int ISOLATION_READ_UNCOMMITTED = Connection.TRANSACTION_READ_UNCOMMITTED;

	//隔离级别：读提交
	int ISOLATION_READ_COMMITTED = Connection.TRANSACTION_READ_COMMITTED;

	//隔离级别：可重复度
	int ISOLATION_REPEATABLE_READ = Connection.TRANSACTION_REPEATABLE_READ;

	//隔离级别：序列化操作（最高）
	int ISOLATION_SERIALIZABLE = Connection.TRANSACTION_SERIALIZABLE;


	//默认事务的超时时间
	int TIMEOUT_DEFAULT = -1;


	//获取事务的传播行为
	int getPropagationBehavior();

	//获取事务的隔离级别
	int getIsolationLevel();

	//获取超时时间
	int getTimeout();

	//是否只读
	boolean isReadOnly();

	//事务名称
	@Nullable
	String getName();

}
