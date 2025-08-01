package components.base.pins.components;

import components.base.pins.Pin;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;

import static components.base.pins.components.GpioType.*;
import static components.base.pins.components.PinType.*;

/**
 * The PinIdentifier enum class contains definitions of all 40 GPIO Pins on the Raspberry Pi 5.<br>
 * Each Pin has an Integer {@link PinIdentifier#pin} value corresponding to the pin-number on the Pi.
 * Each Pin also contains a {@link PinType}, of either Ground, 3 Volt, 5 Volt or GPIO, and
 * a {@link GpioType} defining its capabilities. If a Pin does not have any extra capabilities, it will be
 * defined with {@link GpioType#STANDARD}.<br>
 * @apiNote The Enum contains a value by the name {@link PinIdentifier#BLANK}, with a pin-number of -1,
 * and is the first value defined in the enum. Keep this in mind if you decide to iterate all values.
 * A safe method for retrieving all values without {@link PinIdentifier#BLANK}
 * is provided through <b>{@link PinIdentifier#valuesSafe()}</b>
 * @see PinIdentifier#valuesSafe()
 */
@SuppressWarnings("unused")
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
    PinIdentifier(final int pin, final PinType type, @Nullable final GpioType gpioType) {
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
        return PinIdentifier.BLANK;
    }

    public static PinType getPinType(final int pin) {
        return PinIdentifier.getPin(pin).getType();
    }

    public boolean isValid() {
        return type==GPIO;
    }

    public static boolean isValid(final PinIdentifier pin) {
        return pin.isValid();
    }

    public static Collection<PinIdentifier> valuesSafe() {
        final HashSet<PinIdentifier> values = new HashSet<>(List.of(PinIdentifier.values()));
        values.remove(PinIdentifier.BLANK);
        return values;
    }
}