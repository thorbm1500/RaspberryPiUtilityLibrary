package components.base.pins.digital;

import com.pi4j.context.Context;
import com.pi4j.io.IOType;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;
import com.pi4j.io.gpio.digital.DigitalState;
import components.base.pins.Pin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class DigitalOutputPin extends Pin {

    private final boolean alwaysOn;

    protected final DigitalOutput pin;

    public DigitalOutputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin) {
        this(pi4j,pin,false);
    }

    public DigitalOutputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin, final boolean alwaysOn) {
        super(pi4j,pin);
        this.alwaysOn = alwaysOn;
        final int pinNumber = pin.getPin();
        this.pin = pi4j.create(DigitalOutputConfig
                .newBuilder(pi4j)
                .id("PIN#%d".formatted(pinNumber))
                .name("Pin %d".formatted(pinNumber))
                .address(pinNumber)
                .build());
        if (alwaysOn) on();
    }

    public void set(final boolean turnOn) {
        if (alwaysOn) return;
        if (turnOn) on();
        else off();
    }

    public void set(final Number value) {
        if (alwaysOn) return;
        if (value.intValue() > 0) on();
        else off();
    }

    public void on() {
        pin.high();
    }

    public boolean isOn() {
        return pin.state() == DigitalState.HIGH;
    }

    public void off() {
        if (alwaysOn) return;
        pin.low();
    }

    public boolean isOff() {
        return pin.state() == DigitalState.LOW;
    }

    public boolean isHigh() {
        return pin.isHigh();
    }

    public  boolean isLow() {
        return pin.isLow();
    }

    public DigitalState getState() {
        return pin.state();
    }

    public DigitalOutput getPin() {
        return pin;
    }

    public Number address() {
        return pin.getAddress();
    }

    public IOType ioType() {
        return this.pin.type();
    }
}
