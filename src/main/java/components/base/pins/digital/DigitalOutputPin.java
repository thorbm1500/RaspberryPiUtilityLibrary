package components.base.pins.digital;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalOutput;
import com.pi4j.io.gpio.digital.DigitalOutputConfig;
import com.pi4j.io.gpio.digital.DigitalState;
import components.base.pins.Pin;
import org.jetbrains.annotations.NotNull;

public class DigitalOutputPin extends Pin {

    protected final Context pi4j;
    protected final DigitalOutput pin;

    public DigitalOutputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin) {
        super(pin);
        this.pi4j = pi4j;
        final int pinNumber = pin.getPin();
        this.pin = pi4j.create(DigitalOutputConfig
                .newBuilder(pi4j)
                .id("PIN#%d".formatted(pinNumber))
                .name("Pin %d".formatted(pinNumber))
                .address(pinNumber)
                .build());
    }

    public void on() {
        pin.high();
    }

    public boolean isOn() {
        return pin.state() == DigitalState.HIGH;
    }

    public void off() {
        pin.low();
    }

    public boolean isOff() {
        return pin.state() == DigitalState.LOW;
    }

    public DigitalOutput getPin() {
        return pin;
    }

    @Override
    protected Context getContext() {
        return pi4j;
    }
}
