# **VMO Microservice Food Ordering System**

**Generate Graph**: `mvn com.github.ferstl:depgraph-maven-plugin:4.0.1:aggregate -DcreateImage=true -Dscope=compile "
-Dincludes=com.food.ordering.system*:*"`

## _Order Service_

![img.png](img/img.png)

### Domain Event

![img_1.png](img/img_1.png)

### Domain Service

![img_4.png](img/img_4.png)

### Application Service

![img_5.png](img/img_5.png)

![img_3.png](img/img_3.png)
![img_2.png](img/img_2.png)

### **1. Implementing Domain Events in Order Service domain layer**

### Question?

#### Where to fire the event ? `--> In Application Service. Domain Layer should not know about how to fire the event`.

#### Where to create the event ? `--> Domain Service or Entities`