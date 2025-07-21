package components.base.pins;

import com.pi4j.context.Context;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashSet;
import java.util.Set;

import static components.base.pins.Pin.GpioType.*;
import static components.base.pins.Pin.PinType.*;

@SuppressWarnings("unused")
public abstract class Pin {

    protected abstract Context getContext();

    private static final Set<PinIdentifier> activePins = new HashSet<>();

    protected final PinIdentifier identifier;

    public Pin() throws IllegalAccessException {
        throw new IllegalAccessException("Cannot instantiate Pin object by itself. A Pin Object must be accompanied by a pin type.");
    }

    protected Pin(@NotNull final PinIdentifier pin) throws IllegalStateException, IllegalArgumentException {
        if (!isPinAvailable(pin)) throw new IllegalStateException("Failed to initiate Pin %d. Pin is already in use!".formatted(pin.pin));
        if (!isPinLegal(pin)) throw new IllegalArgumentException("Failed to initiate Pin %d. Pin is not configurable!");
        this.identifier = pin;
        activePins.add(this.identifier);
    }

    protected void destroy(@NotNull final Pin instance) {
        //instance.getContext().destroy()
        activePins.remove(instance.identifier);
    }

    /**
     * Definations of all 40 GPIO Pins on the Raspberry Pi 5.<br>
     * Each Pin has an Integer {@link PinIdentifier#pin} value corresponding to the pin-number on the Pi.
     * Each Pin also contains a {@link PinType}, of either Ground, 3 Volt, 5 Volt or GPIO, and
     * a {@link GpioType} defining its capabilities. If a Pin does not have any extra capabilites, it will be
     * defined with {@link GpioType#STANDARD}.<br>
     * @apiNote The Enum contains a value by the name {@link PinIdentifier#BLANK}, with a pin-number of -1,
     * and is the very first value defined. Keep this in mind if you decide to iterate all values.
     */
    public enum PinIdentifier {
        BLANK(-1, UNKNOWN),
        PIN_1(1, THREE_VOLT), PIN_2(2, FIVE_VOLT),
        PIN_3(3, GPIO, SDA), PIN_4(4, FIVE_VOLT),
        PIN_5(5, GPIO, SCL), PIN_6(6, GROUND),
        PIN_7(7, GPIO, GPCLK0), PIN_8(8, GPIO, TXD),
        PIN_9(9, GROUND), PIN_10(10, GPIO, RXD),
        PIN_11(11, GPIO), PIN_12(12, GPIO, PCM_CLK),
        PIN_13(13, GPIO), PIN_14(14, GROUND),
        PIN_15(15, GPIO), PIN_16(16, GPIO),
        PIN_17(17, THREE_VOLT), PIN_18(18, GPIO),
        PIN_19(19, GPIO, MOSI), PIN_20(20, GROUND),
        PIN_21(21, GPIO, MISO), PIN_22(22, GPIO),
        PIN_23(23, GPIO, SCLK), PIN_24(24, GPIO, CE0),
        PIN_25(25, GROUND), PIN_26(26, GPIO, CE1),
        PIN_27(27, GPIO, ID_SD), PIN_28(28, GPIO, ID_SC),
        PIN_29(29, GPIO), PIN_30(30, GROUND),
        PIN_31(31, GPIO), PIN_32(32, GPIO, HW_PWM),
        PIN_33(33, GPIO, HW_PWM), PIN_34(34, GROUND),
        PIN_35(35, GPIO, PCM_FS), PIN_36(36, GPIO),
        PIN_37(37, GPIO), PIN_38(38, GPIO, PCM_DIN),
        PIN_39(39, GROUND), PIN_40(40, GPIO, PCM_DOUT),
        ;

        private final int pin;
        private final PinType type;
        private final GpioType gpioType;

        PinIdentifier(final int pin, final PinType type) {
            this(pin, type, null);
        }
        PinIdentifier(final int pin, final PinType type, @Nullable final Pin.GpioType gpioType) {
            this.pin = pin;
            this.type = type;
            this.gpioType = gpioType == null ? GpioType.STANDARD : gpioType;
        }

        public int getPin() {
            return pin;
        }

        public PinType getType() {
            return type;
        }

        public GpioType getGpioType() {
            return gpioType;
        }

        public boolean isStandardGpio() {
            return gpioType == GpioType.STANDARD;
        }

        public static PinIdentifier getPin(final int pin) {
            for (PinIdentifier p : PinIdentifier.values()) if (p.getPin() == pin) return p;
            return BLANK;
        }

        public static PinType getPinType(final int pin) {
            return getPin(pin).type;
        }
    }

    public enum PinType {
        GROUND,
        THREE_VOLT,
        FIVE_VOLT,
        GPIO,
        UNKNOWN
    }

    public enum GpioType {
        STANDARD,
        SDA,
        SCL,
        GPCLK0,
        TXD,
        RXD,
        PCM_CLK,
        MOSI,
        MISO,
        SCLK,
        CE0,
        CE1,
        ID_SD,
        ID_SC,
        HW_PWM,
        PCM_FS,
        PCM_DIN,
        PCM_DOUT
    }

    /**
     * Checks if the pin is available or already in use.
     *
     * @param pin Pin to check.
     * @return True | False
     */
    public static boolean isPinAvailable(@NotNull final PinIdentifier pin) {
        return !activePins.contains(pin);
    }

    /**
     * Checks if the pin is configurable.
     *
     * @param pin Pin type.
     * @return True | False
     */
    public static boolean isPinLegal(@NotNull final PinIdentifier pin) {
        return pin.getType().equals(GPIO);
    }
}
