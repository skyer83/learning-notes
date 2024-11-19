package com.lulala.jfk.staticAndFinal;

import lombok.Data;

/**
 * static、final 用法的说明
 * @author shenjh
 * @version 1.0
 * @since 2024/11/18 17:39
 */
@Data
public class User {

    private Car car;

    /** final，在运行时初始化，初始化后不能被改变，每次实例化 User 时，finalCar01 都能被初始化一次，且每次类引用地址都不一样 */
    private final Car finalCar01;

    /** static，在编译（类加载）时初始化，初始化后不能被改变，如果重新赋值，会报错，finalCar02 引用地址一直不变 */
    private final Car finalCar02 = new Car("finalCar02-compile");

    /** static在类加载时初始化（加载），属于类的变量，不属于实例的变量，能被修改 */
    private static Car staticCar01;

    /** static在类加载时初始化（加载），属于类的变量，不属于实例的变量，能被修改 */
    private static Car staticCar02 = new Car("staticCar02-compile");

    /** static final 修饰类变量时，需要在编译时就初始化，且不能改变 */
    public static final Car staticFinalCar = new Car("finalCar02-compile");

    public User(Car car) {
        this.car = car;

        // 运行时初始化，初始化后不能被改变
        this.finalCar01 = car;
        // 编译时已经初始化，不能被改变，不能再赋值，否则编译报错
//        this.finalCar02 = car;

        // 不能兼容 this 关键字【static代表类层次，this代表当前类的对象】
        staticCar01 = car;
        staticCar02 = car;
    }

    public static void main(String[] args) {
        Car car01 = new Car();
        car01.setName("finalCar01");

        // User.staticCar02:::User 实例化之前:::staticCar02-compile
        System.out.println("User.staticCar02:::User 实例化之前:::" + User.staticCar02.getName());
        // User.staticCar02:::User 实例化之前:::addr:::com.lulala.jfk.staticAndFinal.Car@52aeaca9
        System.out.println("User.staticCar02:::User 实例化之前:::addr:::" + User.staticCar02);
        User user01 = new User(car01);
        // user01.getFinalCar01():::finalCar01
        System.out.println("user01.getFinalCar01():::" + user01.getFinalCar01().getName());
        // user01.getFinalCar01():::addr:::com.lulala.jfk.staticAndFinal.Car@8f7ba93a
        System.out.println("user01.getFinalCar01():::addr:::" + user01.getFinalCar01());
        // user01.getFinalCar02():::addr:::com.lulala.jfk.staticAndFinal.Car@8cfee6a1
        System.out.println("user01.getFinalCar02():::addr:::" + user01.getFinalCar02());
        // User.staticCar01:::finalCar01
        System.out.println("User.staticCar01:::" + User.staticCar01.getName());
        // User.staticCar01:::addr:::com.lulala.jfk.staticAndFinal.Car@8f7ba93a
        System.out.println("User.staticCar01:::addr:::" + User.staticCar01);
        // User.staticCar02:::finalCar01
        System.out.println("User.staticCar02:::" + User.staticCar02.getName());
        // User.staticCar02:::addr:::com.lulala.jfk.staticAndFinal.Car@8f7ba93a
        System.out.println("User.staticCar02:::addr:::" + User.staticCar02);

        Car car02 = new Car();
        car02.setName("finalCar02");
        User user02 = new User(car02);
        // user02.getFinalCar01():::finalCar02
        System.out.println("user02.getFinalCar01():::" + user02.getFinalCar01().getName());
        // user02.getFinalCar01():::addr:::com.lulala.jfk.staticAndFinal.Car@8f7ba93b
        // 与 user01.getFinalCar01() 对象不同
        System.out.println("user02.getFinalCar01():::addr:::" + user02.getFinalCar01());
        // user02.getFinalCar02():::addr:::com.lulala.jfk.staticAndFinal.Car@8cfee6a1
        // 与 user01.getFinalCar02() 对象相同
        System.out.println("user02.getFinalCar02():::addr:::" + user02.getFinalCar02());
        // User.staticCar01:::finalCar02
        System.out.println("User.staticCar01:::" + User.staticCar01.getName());
        // User.staticCar01:::addr:::com.lulala.jfk.staticAndFinal.Car@8f7ba93b
        System.out.println("User.staticCar01:::addr:::" + User.staticCar01);
        // User.staticCar02:::finalCar02
        System.out.println("User.staticCar02:::" + User.staticCar02.getName());
        // User.staticCar02:::addr:::com.lulala.jfk.staticAndFinal.Car@8f7ba93b
        System.out.println("User.staticCar02:::addr:::" + User.staticCar02);

        User.staticCar01 = car01;
        // User.staticCar01:::car01:::finalCar01
        System.out.println("User.staticCar01:::car01:::" + User.staticCar01.getName());
        // User.staticCar01:::car01:::addr:::com.lulala.jfk.staticAndFinal.Car@8f7ba93a
        System.out.println("User.staticCar01:::car01:::addr:::" + User.staticCar01);
        // User.staticCar02:::car01:::finalCar02
        System.out.println("User.staticCar02:::car01:::" + User.staticCar02.getName());
        // User.staticCar02:::car01:::addr:::com.lulala.jfk.staticAndFinal.Car@8f7ba93b
        System.out.println("User.staticCar02:::car01:::addr:::" + User.staticCar02);
    }

}
