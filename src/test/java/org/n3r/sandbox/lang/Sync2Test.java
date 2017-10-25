package org.n3r.sandbox.lang;

public class Sync2Test  implements Runnable {
    int b = 100;

    synchronized void m1() throws InterruptedException {
        b = 1000; // 对于属性变量 int b =100由于使用了synchronized也不会存在可见性问题（也没有必要再使用volatile申明）
        Thread.sleep(500); //6
        System.out.println("b=" + b);
    }

    synchronized void m2() throws InterruptedException {
        Thread.sleep(250); //5
        b = 2000;
    }

    public static void main(String[] args) throws InterruptedException {
        Sync2Test tt = new Sync2Test();
        Thread t = new Thread(tt);  //1
        t.start(); //2

        tt.m2(); //3
        System.out.println("main thread b=" + tt.b); //4

        // 输出可能1：
        // main thread b=1000
        // b=1000

        // 输出可能2：
        // b=1000
        // main thread b=2000
    }

    @Override
    public void run() {
        try {
            m1();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

}
