# Java知识点

## static、final

参见 [java中的static、final、static final各种用法](https://blog.csdn.net/qq_44543508/article/details/102691425)

代码说明参见 [com.lulala.jfk.staticAndFinal](#com.lulala.jfk.staticAndFinal) 

### static

> <font color='red'>**static 修饰类变量时，该变量属于类，可以直接通过类进行改变，但不属于类的对象**</font>

加载：static在类加载时初始化（加载）完成

含义：static意为静态的，但凡<font color='red'>被static 修饰说明属于类，不属于类的对象</font>。

可修饰：static 可以修饰 `内部类、方法、成员变量、代码块`。

不可修饰：static不可修饰`外部类、局部变量`【static 属于类的，局部变量属于其方法，并不属于类】

注意：`static` 方法不能兼容`this`关键字【static代表类层次，this代表当前类的对象】

引发问题：构造方法是静态方法吗？【不是，构造方法可以有this】

**static主要作用：方便调用没有创建对象的方法/变量。**

### final

> <font color='red'>**final 修饰类变量时，如果编译时初始化，则不能再改变，类的对象的该变量值或引用都一样，如果是运行时初始化，则类的对象的该变量可以赋不同值，但赋值后，不能改变。注意：不能通过像 setXxx 这样的方法进行初始化，只能在编译或类实例化时初始化。**</font>

加载：final可以在编译（类加载）时初始化，也可以在运行时初始化，<font color='red'>初始化后不能被改变</font>。

可修饰：`类、内部类、方法、成员变量、局部变量、基本类型、引用类型`。

含义：final“最终的”的意思，在Java中又有意为常量的意思，也就是被final修饰的只能进行一次初始化！

被final修饰各种所蕴含的特殊意义：

> 1、 final 修饰基本类型：值不能被修改；
>
> 2、final 修饰引用类型：<font color='red'>引用不可以被修改也就是说不能指向其他对象，但是该引用的对象内容可以被修改</font>；
>
> 3、final 修饰 方法，方法不可以重写，但是可以被子类访问 【前提：方法不是 private 类型】。
>
> 4、final 修饰 类，类不可以被继承。

### static final

> <font color='red'>**static final 修饰类变量时，需要在编译时就初始化，且不能改变**</font>

含义：从字面也可以知道，它代表static与final二者的共同体。

可修饰：依旧是取二者的共同体，所以只能修饰`成员变量、方法、内部类`，被static final修饰意义分别如下：

> 1、成员变量：属于类的变量且只能赋值一次，<font color='red'>声明变量时就需要先进行初始化，无法通过 `类.变量 = 初始化实例` 进行初始化</font> 。
>
> 2、方法：属于类的方法且不可以被重写。
>
> 3、内部类：属于外部类，且不能被继承

## 内部类和外部类

参见 [内部类和外部类](https://blog.csdn.net/qq_35688140/article/details/89195016)

外部类：

最普通的，我们平时见到的那种类，就是在一个后缀为.java的文件中，直接定义的类，比如

```java
public class Student {
  private String name;
  private int age;
}
```

内部类：

内部类，顾名思义，就是包含在外部类中的类，就叫做内部类。内部类有两种，一种是静态内部类，一种是非静态内部类。

```java
public class School {
  private static School instance = null;
  static class Teacher {}
}
public class School {
  private String name;
  class Teacher {}
}
```

静态内部类和非静态内部类之间的区别主要如下：

1、内部原理的区别：

静态内部类是属于外部类的类成员，是一种静态的成员，是属于类的，就有点类似于private static Singleton instance = null；`非静态内部类，是属于外部类的实例对象的一个实例成员,静态类则是属于所有外部共有的`，也就是说，每个非静态内部类，不是属于外部类的，是属于外部类的每一个实例的，创建非静态内部类的实例以后，非静态内部类实例，是必须跟一个外部类的实例进行关联和有寄存关系的。

2、创建方式的区别：

创建静态内部类的实例的时候，只要直接使用“外部类.内部类()”的方式，就可以，比如School.Teacher()；创建非静态内部类的实例的时候，必须要先创建一个外部类的实例，然后通过外部类的实例，再来创建内部类的实例，new School().Teacher()

通常来说，我们一般都会为了方便，会选择使用静态内部类。

匿名内部类：

```java
public interface ISayHello {
  String sayHello(String name);
}

public class SayHelloTest {
  
  public static void main(String[] args) {
    //实现上面的接口创建了一个匿名内部类
    ISayHello obj = new ISayHello() {
      public String sayHello(String name) { return "hello, " + name }
    }
    System.out.println(obj.sayHello("leo"))
  }

}
```

`匿名内部类的使用场景，通常来说，就是在一个内部类，只要创建一次，使用一次，以后就不再使用的情况下`，就可以。那么，此时，通常不会选择在外部创建一个类，而是选择直接创建一个实现了某个接口、或者继承了某个父类的内部类，而且通常是在方法内部，创建一个匿名内部类。

# 项目说明

## [java-fragment-knowledge](../java-fragment-knowledge)

Java 碎片知识，用于记录开发、学习过程中的一些小知识点

### com.lulala.jfk.thread

关于线程的知识点说明

### com.lulala.jfk.staticAndFinal

关于 static、final 关键字的用法说明