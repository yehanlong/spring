入口 JdbcTemplate.execute()作为数据库操作的核心入口，采用模板模式将大多数数据库操作相同的步骤统一封装

在JDBC编程中，常用Statement、PreparedStatement 和 CallableStatement三种方式来执行查询语句，
其中 Statement 用于通用查询， PreparedStatement 用于执行参数化查询，而 CallableStatement则是用于存储过程。

PreparedStatement 和Statement 之间的关系和区别.
    关系：PreparedStatement继承自Statement,都是接口
    区别：PreparedStatement可以使用占位符，是预编译的，批处理比Statement效率高
          **PreparedStatement对象可以防止sql注入，而Statement不能防止sql注入**
    优势: PreparedStatement对象防止sql注入的方式是整个参数用引号包起来，并把参数中的引号作为转义字符，从而达到了防止sql注入的目的(setString()方法);
          PreparedStatement还是预编译的(不用改变一次参数就要重新编译整个sql语句，效率高);
          PreparedStatement它执行查询语句得到的结果集是离线的，连接关闭后，仍然可以访问结果集。
注：Mybatis的#{}是经过预编译的，是安全的；${}是未经过预编译的，仅仅是取变量的值，是非安全的，存在SQL注入


execute(String sql):运行语句，返回是否有结果集
executeQuery(String sql)：运行select语句，返回ResultSet结果集。
executeUpdate(String sql)：运行insert/update/delete操作，返回更新的行数。
addBatch(String sql) ：把多条sql语句放到一个批处理中。
executeBatch()：向数据库发送一批sql语句执行。


JdbcTemplate.execute() --> { getConnection() -> DataSourceUtils.getConnection() --> doGetConnection()
                           }
                       --> { applyStatementSettings() 
                           }
                       --> { handleWarnings()  
                           }
                       --> { releaseConnection() -> DataSourceUtils.releaseConnection() --> doReleaseConnection() 
                           } 