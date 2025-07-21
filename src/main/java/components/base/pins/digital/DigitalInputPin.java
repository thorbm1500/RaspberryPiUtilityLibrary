package components.base.pins.digital;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.digital.DigitalInput;
import com.pi4j.io.gpio.digital.DigitalInputConfig;
import components.base.pins.Pin;
import org.jetbrains.annotations.NotNull;

public class DigitalInputPin extends Pin {

    protected final Context pi4j;
    protected final DigitalInput pin;

    public DigitalInputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin) {
        super(pin);
        this.pi4j = pi4j;
        final int pinNumber = pin.getPin();
        this.pin = pi4j.create(DigitalInputConfig
                .newBuilder(pi4j)
                .id("PIN#%d".formatted(pinNumber))
                .name("Pin %d".formatted(pinNumber))
                .address(pinNumber)
                .build());
    }

    @Override
    protected Context getContext() {
        return pi4j;
    }
}
