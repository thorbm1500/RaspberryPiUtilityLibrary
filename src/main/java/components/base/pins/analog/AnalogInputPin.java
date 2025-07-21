package components.base.pins.analog;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.analog.AnalogInput;
import com.pi4j.io.gpio.analog.AnalogInputConfig;
import components.base.pins.Pin;
import org.jetbrains.annotations.NotNull;

public class AnalogInputPin extends Pin {

    protected final Context pi4j;
    protected final AnalogInput pin;

    public AnalogInputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin) {
        super(pin);
        this.pi4j = pi4j;
        final int pinNumber = pin.getPin();
        this.pin = pi4j.create(AnalogInputConfig
                .newBuilder(pi4j)
                .id("PIN#%d".formatted(pinNumber))
                .name("Pin %d".formatted(pinNumber))
                .address(pinNumber)
                .build()); //todo: Add min/max values
    }

    public Number read() {
        return pin.getValue();
    }

    @Override
    protected Context getContext() {
        return pi4j;
    }
}
