package components.base;

public abstract class Component {

    protected abstract void enable();
    protected abstract void disable();
    protected abstract void setPwm();
    protected abstract void on();
    protected abstract void off();

}
