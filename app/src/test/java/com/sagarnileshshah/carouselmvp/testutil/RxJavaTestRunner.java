package com.sagarnileshshah.carouselmvp.testutil;

import org.mockito.junit.MockitoJUnitRunner;

import java.lang.reflect.InvocationTargetException;

import rx.Scheduler;
import rx.plugins.RxJavaPlugins;
import rx.plugins.RxJavaSchedulersHook;
import rx.plugins.RxJavaTestPlugins;
import rx.schedulers.Schedulers;

public class RxJavaTestRunner extends MockitoJUnitRunner {
    public RxJavaTestRunner(Class<?> klass) throws InvocationTargetException {
        super(klass);
        RxJavaTestPlugins.resetPlugins();
        RxJavaPlugins.getInstance().registerSchedulersHook(new RxJavaSchedulersHook() {
            @Override
            public Scheduler getIOScheduler() {
                return Schedulers.immediate();
            }
        });
    }
}
