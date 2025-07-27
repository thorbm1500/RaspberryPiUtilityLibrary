package components;

import com.pi4j.context.Context;
import components.base.Component;
import components.base.pins.Pin;
import components.base.pins.digital.DigitalOutputPin;
import org.jetbrains.annotations.NotNull;

import java.util.concurrent.TimeUnit;

@SuppressWarnings("unused")
public class LED extends Component {

    private final DigitalOutputPin pin;

    public LED(@NotNull final Context pi4j, @NotNull final Pin.PinIdentifier pin) {
        this(pi4j,pin,false);
    }

    public LED(@NotNull final Context pi4j, @NotNull final Pin.PinIdentifier pin, final boolean alwaysOn) {
        super(pi4j);
        this.pin = new DigitalOutputPin(pi4j,pin,alwaysOn);
    }

    public void on() {
        pin.on();
    }

    public void off() {
        pin.off();
    }

    public boolean isOn() {
        return pin.isOn();
    }

    public boolean isOff() {
        return pin.isOff();
    }

    public boolean toggle() {
        if(isOn()) off();
        else on();
        return isOn();
    }

    public void blink(final int interval, @NotNull final TimeUnit unit) {
        pin.getPin().blink(interval,unit);
    }

    public void blink(final int initialDelay, final int interval, @NotNull final TimeUnit unit) {
        pin.getPin().blink(initialDelay,interval,unit);
    }

    public void blinkAsync(final int interval, @NotNull final TimeUnit unit) {
        pin.getPin().blinkAsync(interval,unit);
    }

    public void blinkAsync(final int initialDelay, final int interval, @NotNull final TimeUnit unit) {
        pin.getPin().blinkAsync(initialDelay,interval,unit);
    }

    public void blinkOff() {
        off();
    }

    public void pulse(final int interval, @NotNull final TimeUnit unit) {
        pin.getPin().pulse(interval,unit);
    }

    public void pulseAsync(final int interval, @NotNull final TimeUnit unit) {
        pin.getPin().pulseAsync(interval,unit);
    }

    public void pulseLow(final int interval, @NotNull final TimeUnit unit) {
        pin.getPin().pulseLow(interval,unit);
    }

    public void pulseHigh(final int interval, @NotNull final TimeUnit unit) {
        pin.getPin().pulseHigh(interval,unit);
    }

    public void pulseOff() {
        off();
    }
}
