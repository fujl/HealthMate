package com.mobile.healthmate.ui.main.adapter;

import com.mobile.healthmate.ui.main.ShowExceptionUI;

import java.lang.Thread.UncaughtExceptionHandler;

public class ExceptionCaughtAdapter implements UncaughtExceptionHandler {
    private UncaughtExceptionHandler uncaughtExceptionHandler;

    public ExceptionCaughtAdapter(UncaughtExceptionHandler uncaughtExceptionHandler) {
        this.uncaughtExceptionHandler = uncaughtExceptionHandler;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        ShowExceptionUI.showException(ex);
        if (uncaughtExceptionHandler != null) {
            uncaughtExceptionHandler.uncaughtException(thread, ex);
        }
    }
}
