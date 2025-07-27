package components.base.pins.digital;

import com.pi4j.context.Context;
import com.pi4j.io.IOType;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfig;
import com.pi4j.io.gpio.digital.DigitalState;
import com.pi4j.io.gpio.digital.PullResistance;
import components.base.pins.Pin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class DigitalInputPin extends Pin {

    protected final DigitalInput pin;

    public DigitalInputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin) {
        super(pi4j,pin);
        final int pinNumber = pin.getPin();
        this.pin = pi4j.create(DigitalInputConfig
                .newBuilder(pi4j)
                .id("PIN#%d".formatted(pinNumber))
                .name("Pin %d".formatted(pinNumber))
                .address(pinNumber)
                .build());
    }

    public PullResistance pull() {
        return pin.pull();
    }

    public int readAsInteger() {
        return pin.isHigh() ? 1 : 0;
    }

    public boolean readAsBoolean() {
        return pin.isHigh();
    }

    public DigitalState read() {
        return pin.state();
    }

    public boolean isHigh() {
        return pin.isHigh();
    }

    public  boolean isLow() {
        return pin.isLow();
    }

    public boolean isOn() {
        return pin.isOn();
    }

    public boolean isOff() {
        return pin.isOff();
    }

    public Number address() {
        return pin.getAddress();
    }

    public IOType ioType() {
        return this.pin.type();
    }
}
