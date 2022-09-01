package no.sparebank1.turbo.stubs;

import org.codehaus.plexus.logging.Logger;

public class LoggerStub implements org.codehaus.plexus.logging.Logger{
    @Override
    public void debug(final String s) {

    }

    @Override
    public void debug(final String s, final Throwable throwable) {

    }

    @Override
    public boolean isDebugEnabled() {
        return false;
    }

    @Override
    public void info(final String s) {
        System.out.println(s);
    }

    @Override
    public void info(final String s, final Throwable throwable) {

    }

    @Override
    public boolean isInfoEnabled() {
        return false;
    }

    @Override
    public void warn(final String s) {

    }

    @Override
    public void warn(final String s, final Throwable throwable) {

    }

    @Override
    public boolean isWarnEnabled() {
        return false;
    }

    @Override
    public void error(final String s) {

    }

    @Override
    public void error(final String s, final Throwable throwable) {

    }

    @Override
    public boolean isErrorEnabled() {
        return false;
    }

    @Override
    public void fatalError(final String s) {

    }

    @Override
    public void fatalError(final String s, final Throwable throwable) {

    }

    @Override
    public boolean isFatalErrorEnabled() {
        return false;
    }

    @Override
    public int getThreshold() {
        return 0;
    }

    @Override
    public void setThreshold(final int i) {

    }

    @Override
    public Logger getChildLogger(final String s) {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }
}
