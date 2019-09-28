# demo_cloud
# 本工程是搭建的一个spring cloud2.0的基础项目
# eureka注册中心
# zuul网关：路由配置，熔断，降级（未完全完善功能，本工程主要使用gateway做网关）
# gateway网关：路由配置，限流，熔断，降级，重试，自定义过滤器，全局过滤器等
# config配置中心：git配置源，rabbitMQ，消息总线等
# log日志中心：利用rabbitMQ实现服务日志收集
# common公共组件：一些公共的设计和依赖
# auth：利用oauth2的安全认证中心（未完全实现，只搭建了服务器，还未和网关集成，因为跟gateway集成不上）
# client+file：两个测试应用（包含zipkin的调用链追踪）
