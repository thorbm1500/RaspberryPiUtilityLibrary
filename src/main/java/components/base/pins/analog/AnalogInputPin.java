package components.base.pins.analog;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.analog.AnalogInput;
import com.pi4j.io.gpio.analog.AnalogInputConfig;
import components.base.pins.Pin;
import org.jetbrains.annotations.NotNull;

@SuppressWarnings("unused")
public class AnalogInputPin extends Pin {

    public static final int defaultMinimumValue = 0;
    public static final int defaultMaximumValue = 1023;
    public static final int defaultTwelveBitMaximumValue = 4095;
    private final boolean externalDac;
    private final int minimumValue;
    private final int maximumValue;

    protected final AnalogInput pin;

    public AnalogInputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin) {
        this(pi4j, pin, null, null, false);
    }

    public AnalogInputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin, final boolean externalDacChip) {
        this(pi4j, pin, null, null, externalDacChip);
    }

    public AnalogInputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin, final int minimum, final int maximum) {
        this(pi4j,pin,minimum,maximum,false);
    }

    public AnalogInputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin, final Integer minimum, final Integer maximum, final boolean externalDacChip) {
        super(pi4j, pin);

        this.externalDac = externalDacChip;
        this.minimumValue = validateAndReturnMinimumValue(minimum);
        this.maximumValue = validateAndReturnMaximumValue(maximum);

        this.pin = createPinInstance(pin.getPin());
    }

    private int validateAndReturnMinimumValue(final Integer value) throws IllegalArgumentException {
        if (value == null) return defaultMinimumValue;
        else if (value < 0) throw new IllegalArgumentException("Minimum value cannot be less than 0!");
        else if (value > 4095) throw new IllegalArgumentException("Maximum value cannot be more than 4095!");

        if (externalDac) return value;
        else if (value > 1024) throw new IllegalArgumentException("Cannot assign a maximum value higher than 1024 if External DAC Chip is not enabled!");
        else return value;
    }

    private int validateAndReturnMaximumValue(final Integer value) throws IllegalArgumentException {
        if (value == null) return externalDac ? defaultTwelveBitMaximumValue : defaultMaximumValue;
        if (value < minimumValue) throw new IllegalArgumentException("Maximum value cannot be less than minimum value!");
        else if (value < 0) throw new IllegalArgumentException("Minimum value cannot be less than 0!");
        else if (value > 4095) throw new IllegalArgumentException("Maximum value cannot be more than 4095!");

        if (externalDac) return value;
        else if (value > 1024) throw new IllegalArgumentException("Cannot assign a maximum value higher than 1024 if External DAC Chip is not enabled!");
        else return value;
    }

    private AnalogInput createPinInstance(final int pin) {
        return pi4j.create(AnalogInputConfig
                .newBuilder(pi4j)
                .id("PIN#" + pin)
                .name("Pin %d".formatted(pin))
                .address(pin)
                .min(minimumValue)
                .max(maximumValue)
                .build());
    }

    public Number address() {
        return pin.address();
    }

    public Number read() {
        return Math.clamp(pin.getValue().doubleValue(),minimumValue,maximumValue);
    }
}
