package components.base.pins.analog;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.analog.AnalogOutput;
import com.pi4j.io.gpio.analog.AnalogOutputConfig;
import components.base.pins.Pin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class AnalogOutputPin extends Pin {

    public static final int defaultMinimumValue = 0;
    public static final int defaultMaximumValue = 1023;
    public static final int defaultTwelveBitMaximumValue = 4095;
    private final int minimumValue;
    private final int maximumValue;

    protected final Context pi4j;
    protected final AnalogOutput pin;

    public AnalogOutputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin) {
        this(pi4j, pin, false);
    }

    public AnalogOutputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin, final boolean externalDacChip) {
        this(pi4j, pin, null, null, externalDacChip);
    }

    public AnalogOutputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin, final int minimum, final int maximum) throws IllegalArgumentException {
        this(pi4j, pin, minimum, maximum, null);
    }

    public AnalogOutputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin, @Nullable final Integer minimum, @Nullable final Integer maximum, final Boolean externalDacChip) throws IllegalArgumentException {
        super(pin);
        this.pi4j = pi4j;

        if (minimum == null) this.minimumValue = defaultMinimumValue;
        else if (minimum < 0) throw new IllegalArgumentException("Minimum value cannot be less than 0!");
        else {
            if (minimum < 1024) this.minimumValue = minimum;
            else if (externalDacChip && minimum < 4095) this.minimumValue = minimum;
            else throw new IllegalArgumentException("Cannot assign a maximum value higher than 4095!");
        }

        this.pin = createPinInstance(pin.getPin());
    }

    private AnalogOutput createPinInstance(final int pin) {
        return pi4j.create(AnalogOutputConfig
                .newBuilder(pi4j)
                .id("PIN#%d".formatted(pin))
                .name("Pin %d".formatted(pin))
                .address(pin)
                .min(minimumValue)
                .max(maximumValue)
                .build());
    }

    /**
     * Sets the current value to the maximum defined value.
     * The default maximum value is 1023, has no minimum value been defined.
     */
    public void on() {
        on(false);
    }

    public void on(final boolean force) {
        if (force) pin.setValue(defaultMaximumValue);
        else pin.setValue(maximumValue);
    }

    /**
     * Sets the current value to the minimum defined value.
     * The default minimum value is 0, has no minimum value been defined.<br>
     * Note: In case the minimum value defined is higher than 0, set force to true, to force it to 0.
     * This will not change the minimum allowed value, but rather simply override it this once.<br>
     * Has a minimum value not been defined, then calling this method without setting force to true,
     * is enough to turn off the pin as the minimum value will then be 0.
     */
    public void off() {
        off(false);
    }

    /**
     * Sets the current value to the minimum defined value.
     * The default minimum value is 0, has no minimum value been defined.<br>
     * Note: In case the minimum value defined is higher than 0, set force to true, to force it to 0.
     * This will not change the minimum allowed value, but rather simply override it this once.
     * Has a minimum value not been defined, then calling this method without setting force to true,
     * is enough to turn off the pin as the minimum value will then be 0.
     *
     * @param force True, if the pin should override the defined minimum, and be forced to 0, otherwise false.
     */
    public void off(final boolean force) {
        if (force) pin.setValue(0);
        else pin.setValue(minimumValue);
    }

    @Override
    protected Context getContext() {
        return pi4j;
    }
}