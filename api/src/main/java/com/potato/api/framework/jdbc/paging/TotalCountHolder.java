package com.potato.api.framework.jdbc.paging;

/**
 * 使每个线程拥有自己的 totalCount 变量
 *
 * @author huangyong
 * @since 1.0.0
 */
public class TotalCountHolder {

    private static ThreadLocal<Integer> holder = new ThreadLocal<Integer>() {
        @Override
        protected Integer initialValue() {
            return 0;
        }
    };

    public static void set(int value) {
        holder.set(value);
    }

    public static int get() {
        return holder.get();
    }

    public static void remove() {
        holder.remove();
    }
}
