## 🌞项目介绍
- 本项目参考 Dubbo 开源项目自主设计实现的 Java 高性能 RPC 框架。开发者只需引入 Spring Boot Starter，就能通过注解和配置的方式快速使用框架，实现像调用本地方法一样轻松调用远程服务。

- 本项目有较多的实现亮点：基于 Vert.x TCP 服务器 + 自定义协议实现网络传输；基于 Etcd 实现注册中心以完成服务的注册消费；还支持通过 SPI 机制动态扩展序列化器、负载均衡器、重试和容错策略等。

## 👾项目所用技术
<div aligen="center"> 
  
  ![SpringBoot Badge](https://img.shields.io/badge/SpringBoot-FCC624?logo=SpringBoot&logoColor=000&style=flat)
  ![Etcd Badge](https://img.shields.io/badge/Etcd-0078D6?logo=Etcd&logoColor=fff&style=flat)
  ![Vert.x Badge](https://img.shields.io/badge/Vert.x-007ACC?logo=Vert.x&logoColor=fff&style=flat)
  ![Hutool Badge](https://img.shields.io/badge/Hutool-31A8FF?logo=Hutool&logoColor=fff&style=flat)
  ![ZooKeeper Badge](https://img.shields.io/badge/ZooKeeper-181717?logo=ZooKeeper&logoColor=fff&style=flat)
  ![SPI机制 Badge](https://img.shields.io/badge/SPI机制-5C2D91?logo=SPI机制&logoColor=fff&style=flat)
  ![GuavaRetrying Badge](https://img.shields.io/badge/GuavaRetrying-oriange?logo=GuavaRetrying&logoColor=fff&style=flat)
  
</div>


## 🏄	Rpc框架基础版流程

- **服务提供方通过注册中心进行服务注册后启动Vert.x服务器。**
  
    - 创建Vert.x实例，并通过Vert.x实例创建HTTP服务器。
    
    -  指定请求处理器(HttpServerHandle())进行请求的处理
    
        - Vert.x的处理器首先会对接收到的HttpServerRequest请求进行反序列化并赋值给RPC框架封装的RpcRequest, 并对RpcRequest进行非空校验。
      
        - 之后通过注册中心.get(rpcRequest.getServiceName())来获取服务的实现类，通过服务实现类的getMethod方法与RpcRequest里的方法名与参数类型拿到Method类。
      
        - **调用Method的invoke(服务实现类对象，服务实现类参数)方法(即之前传入的方法名，参数类型，实现类的实例，参数列表)拿到返回结果(这里是通过反射的原理来实现)**。
      
        - 将拿到的返回结果，返回类型等赋值给RPC框架的封装的RpcResponse里。
      
        - 将响应对象的请求头内容设置为JSON格式，之后将RpcResponse进行序列化为字节数组后，HttpServerResponse将字节数组转化为Buffer对象后使用end方法将其返回给客户端(很多HTTP服务器框架和库都设计成了使用特定的数据结构(如Buffer)来处理响应数据)。
      
    - 启动HTTP服务器并监听指定端口
    
- **服务消费方首先通过动态代理获取UserService的代理对象。**
  
    - ``` java
         public static <T> T getProxy(Class<T> serviceClass) {
                return (T) Proxy.newProxyInstance(
                        serviceClass.getClassLoader(),
                        new Class[]{serviceClass},
                        new ServiceProxy());
            }
         ```
    
      使用这个方法，传入serviceClass的类加载器(用于加载代理类), new Class[]{serviceClass}数组:表示代理类要实现的接口, new ServiceProxy()用于处理代理方法的调用。
    
  -  ``` java
     User user = new User();
     user.setName("yehuo");
     User newUser = userService.getUser(user);
     if(newUser!= null) {
           System.out.println(newUser.getName());
     }else {
           System.out.println("user == null");
     }
     ```
    
      创建User对象，并通过代理对象userService调用getUser方法，并传递user对象，**此时因为调用了代理类的方法 ===> 所以会执行ServiceProxy的invoke方法(用于处理代理方法的调用)**。
    
   - 在ServiceProxy的invoke方法中
    
        - 首先会将传入的服务类名，方法名，参数类型，参数列表构造出RpcRequest类。
      
        - 之后将RpcRequest序列化为字节数组，并通过Hutool包的HttpRequest.post方法，传入自己数组，拿到返回值(使用HttpResponse来接收), 并获取字节数组，再通过反序列化赋值给RpcResponse并返回响应数据。
      
        -
          ``` java
          User newUser = userService.getUser(user);
          ```
      
        即newUser对象。
