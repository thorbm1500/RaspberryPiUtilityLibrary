package components.base.pins.analog;

import com.pi4j.context.Context;
import com.pi4j.io.gpio.analog.AnalogOutput;
import com.pi4j.io.gpio.analog.AnalogOutputConfig;
import components.base.pins.Pin;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

@SuppressWarnings("unused")
public class AnalogOutputPin extends Pin {

    public static final int defaultMinimumValue = 0;
    public static final int defaultMaximumValue = 1023;
    public static final int defaultTwelveBitMaximumValue = 4095;
    private final boolean externalDac;
    private final int minimumValue;
    private final int maximumValue;

    protected final AnalogOutput pin;

    public AnalogOutputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin) {
        this(pi4j, pin, false);
    }

    public AnalogOutputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin, final boolean externalDacChip) {
        this(pi4j, pin, null, null, externalDacChip);
    }

    public AnalogOutputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin, final int minimum, final int maximum) {
        this(pi4j, pin, minimum, maximum, false);
    }

    /**
     * Create a new Analog Pin with the Type <b>Output.
     * </b><br>
     * This pin is useful if you're looking for a variable output.
     * It is both possible to use this pin
     * configured as a 10-bit (Default) or as a 12-bit (<i>Requires an external DAC Chip*</i>)
     * <br><br>
     * When configured as a 10-bit pin, the highest possible value is 1024.
     * 1024 is the same as turning the pin on fully.
     * However, if there is an external DAC Chip available, and the pin is configured as a 12-bit pin,
     * then the highest possible value is 4095.
     *
     *
     * @param pi4j                      The Pi4J Context.
     * @param pin                       The Pin Identifier to assign.
     * @param minimum                   The minimum value the pin should be allowed to be set to.
     * @param maximum                   The maximum value the pin should be allowed to be set to.
     * @param externalDacChip           Whether the Raspberry Pi has an External DAC Chip.
     * @throws IllegalArgumentException Should the minimum or maximum value be less than 0.
     * @throws IllegalArgumentException Should the minimum or maximum value be higher than 1024,
     *                                  while the externalDacChip is set to false.
     * @throws IllegalArgumentException Should the maximum value be higher than 1024, while the
     *                                  externalDacChip is set to false.
     * @apiNote                         <b>Voltage:</b> The pin is always only able to provide 3.3v, regardless of the use of external DAC chips.
     *                                  A higher range is only useful for finer control, tho often proves more noisy.
     *                                  <li><b>External DAC Chips:</b> You are manually required to set the externalDacChip argument to true if your
     *                                  Raspberry Pi is using such a chip, otherwise you will still be limited to 1024.</li>
     */
    public AnalogOutputPin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin, @Nullable final Integer minimum, @Nullable final Integer maximum, final boolean externalDacChip) throws IllegalArgumentException {
        super(pi4j,pin);

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

    private AnalogOutput createPinInstance(final int pin) {
        return pi4j.create(AnalogOutputConfig
                .newBuilder(pi4j)
                .id("PIN#"+pin)
                .name("Pin %d".formatted(pin))
                .address(pin)
                .min(minimumValue)
                .max(maximumValue)
                .build());
    }

    /**
     * Sets the pin's current value to the maximum defined value.
     * The default maximum value is 1023, has no minimum value been defined.
     */
    public void on() {
        on(false);
    }

    /**
     * Sets the pin's current value to the maximum defined value.
     * The default maximum value is 1023, has no minimum value been defined.
     * @param force True, if the pin should be set to the maximum possible value,
     *              otherwise false.
     * @apiNote The maximum value is defined by whether the Raspberry has an external DAC Chip.
     * If this is the case, then the maximum possible value is 4095, otherwise the highest possible
     * value is 1024.
     */
    public void on(final boolean force) {
        if (force) pin.setValue(defaultMaximumValue);
        else pin.setValue(maximumValue);
    }

    /**
     * Sets the pin's current value to the minimum defined value.
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
     * Sets the pin's current value to the minimum defined value.
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

    /**
     * Sets the pin's current value to the specified value.
     * The value passed to this method will be forced within
     * the minimum and maximum values defined.
     * @param value The pin's new value.
     */
    public void set(final int value) {
        pin.setValue(Math.clamp(value,minimumValue,maximumValue));
    }
}