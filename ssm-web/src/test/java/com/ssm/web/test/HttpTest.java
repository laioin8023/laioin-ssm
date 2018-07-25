package com.ssm.web.test;

import com.ssm.common.utils.HttpClientUtil;

public class HttpTest {

    private static Object lock = new Object();
    private static volatile boolean flag = true;

    public static class ThreadA extends Thread {

        @Override
        public void run() {
            try {
                synchronized (lock) {

                    while (true) {
                        // 否则就 唤醒
                        if (!flag) {
                            System.out.println("----唤醒了");
                            break;
                        }
                    }

                    System.out.println(System.currentTimeMillis());
                    String data = HttpClientUtil.doGet("http://localhost:8080/ssm/user/findById?userId=1");
                    System.out.println(data);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String args[]) {
        try {
            ThreadA a = new ThreadA();
            a.start();
            ThreadA b = new ThreadA();
            b.start();

            Thread.sleep(2000);
            System.out.println("---start");
            // 唤醒所有线程
            flag = false;
            System.out.println("---end");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

