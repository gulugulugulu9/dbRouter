package cn.bugstack.middleware.test;

import cn.bugstack.middleware.db.router.annotation.DBRouter;
import org.junit.Test;

import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Method;

/**
 * 博客：https://bugstack.cn - 沉淀、分享、成长，让自己和他人都能有所收获！
 * 公众号：bugstack虫洞栈
 * Create by 小傅哥(fustack)
 */
public class ApiTest {

    public static void main(String[] args) {
            new Thread(new Wait(), "wait").start();
            new Thread(new Notify(), "notity").start();
    }
    static Object lock = new Object();
    static boolean flag = true;
    static class Wait implements Runnable {

        @Override
        public void run() {
            synchronized (lock) {
                while (flag) {
                    System.out.println(Thread.currentThread().getName() + " 持有锁 ");
                    try {
                        Thread.sleep(2000);
                        lock.wait();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
            System.out.println("最后的wait处：flag是：" + flag);
        }
    }

    static class Notify implements Runnable{
        public void run(){
            synchronized (lock){
                System.out.println(Thread.currentThread().getName() + "持有锁");
                System.out.println("一次虚假唤醒");

                lock.notify();
            }
            try{
                Thread.sleep(1000);
            } catch (Exception e) {
                System.out.println(e.getMessage());
            }
            synchronized (lock){
                System.out.println(Thread.currentThread().getName() + "持有锁");
                System.out.println(flag);

                lock.notify();
                flag = false;
            }

        }
    }

    @Test
    public void test_db_hash() {
        String key = "fustackgiii";

        int dbCount = 2, tbCount = 4;
        int size = dbCount * tbCount;
        // 散列
        int idx = (size - 1) & (key.hashCode() ^ (key.hashCode() >>> 16));

        int dbIdx = idx / tbCount + 1;
        int tbIdx = idx - tbCount * (dbIdx - 1);

        System.out.println(dbIdx);
        System.out.println(tbIdx);

    }

    @Test
    public void test_str_format() {
        System.out.println(String.format("db%02d", 1));
        System.out.println(String.format("_%02d", 25));
    }

    @Test
    public void test_annotation() throws NoSuchMethodException {
        Class<IUserDao> iUserDaoClass = IUserDao.class;
        Method method = iUserDaoClass.getMethod("insertUser", String.class);

        DBRouter dbRouter = method.getAnnotation(DBRouter.class);

        System.out.println(dbRouter.key());

    }


}


