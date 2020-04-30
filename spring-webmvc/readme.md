**(1)初始化阶段**
1.1 servlet 容器加载servlet类，把servlet类的.class 文件中的数据读到内存中。
1.2 servlet 容器创建一个ServletConfig对象。ServletConfig对象包含了servlet的初始化配直信息。
1.3 servlet 容器创建一个servlet 对象。
1.4 servlet 容器调用servlet对象的init方法进行初始化。

**(2)运行阶段**
当servlet容器接收到一个请求时，servlet容器会针对这个请求创建servletRequest和servletResponse对象，然后调用service方法。
并把这两个参数传递给service方法。service方法通过servletRequest对象获得请求的信息。并处理该请求。
再通过servletResponse 对象生成这个请求的响应结果。然后销毁servletRequest和servletResponse对象。
我们不管这个请求是post提交的还是get提交的，最终这个请求都会由service方法来处理。

**(3)销毁阶段**
当Web应用被终止时，servlet容器会先调用servlet对象的destroy方法，然后再销毁servlet对象，
同时也会销毁与servlet对象相关联的servletConfig对象。我们可以在destroy方法的实现中，
释放servlet所占用的资源，如关闭数据库连接，关闭文件输入输出流等。


DispatcherServlet -> FrameworkServlet -> HttpServletBean
HttpServletBean.init() --> initServletBean() -> FrameworkServlet.initServletBean()
--> initWebApplicationContext() --> onRefresh() -> DispatcherServlet.initStrategies()
->FrameworkServlet.doGet() --> processRequest() -> DispatcherServlet.doService()
--> doDispatch()