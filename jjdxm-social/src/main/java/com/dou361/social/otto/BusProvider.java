package com.dou361.social.otto;

/**
 * ========================================
 * <p>
 * 版 权：dou361.com 版权所有 （C） 2015
 * <p>
 * 作 者：陈冠明
 * <p>
 * 个人网站：http://www.dou361.com
 * <p>
 * 版 本：1.0
 * <p>
 * 创建日期：2016/6/27 22:50
 * <p>
 * 描 述：Otto Bus Provider
 * <p>
 * <p>
 * 修订历史：
 * <p>
 * ========================================
 */
public class BusProvider {

    private static MainThreadBus bus;

    public static MainThreadBus getInstance() {
        if (bus == null)
            bus = new MainThreadBus();

        return bus;
    }

}
