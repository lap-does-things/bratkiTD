package ch.logixisland.anuto.engine.logic.loop;
// по сути это не нужно, если ты не кривоносый чурбан, который будет вызывать ошибки
public interface ErrorListener {
    void error(Exception e, int loopCount);
}
