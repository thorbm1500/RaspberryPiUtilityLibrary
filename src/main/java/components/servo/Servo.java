package components.servo;

import com.pi4j.context.Context;
import com.pi4j.util.Console;
import components.base.pins.analog.AnalogOutputPin;
import components.base.pins.components.PinIdentifier;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.concurrent.*;

public abstract class Servo {

    public record Range(Number min, Number max) {}

    protected final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1);
    protected final ExecutorService executor = Executors.newVirtualThreadPerTaskExecutor();

    private final Context pi4j;
    protected final Console console;

    protected Servo(@NotNull final Context pi4j) throws IllegalStateException {
        this.pi4j = pi4j;
        this.console = new Console();
    }

    protected void high(@NotNull final AnalogOutputPin... pins) {
        high(false, pins);
    }

    protected void high(final boolean ignoreLimit, @NotNull final AnalogOutputPin... pins) {
        for (final AnalogOutputPin pin : pins) pin.on(ignoreLimit);
    }

    protected void high(final int delay, @NotNull final AnalogOutputPin... pins) {
        high(false,delay,pins);
    }

    protected void high(final boolean ignoreLimit, final int delay, @NotNull final AnalogOutputPin... pins) {
        high(ignoreLimit,delay,TimeUnit.MILLISECONDS,pins);
    }

    protected void high(final int delay, @NotNull final TimeUnit unit, @NotNull final AnalogOutputPin... pins) {
        high(false,delay,unit,pins);
    }

    protected void high(final boolean ignoreLimit, final int delay, @NotNull final TimeUnit unit, @NotNull final AnalogOutputPin... pins) {
        scheduler.schedule(() -> executor.submit(() -> high(ignoreLimit, pins)),delay,unit);
    }

    protected void low(@NotNull final AnalogOutputPin... pins) {
        low(false, pins);
    }

    protected void low(final boolean ignoreLimit, @NotNull final AnalogOutputPin... pins) {
        for (final AnalogOutputPin pin : pins) pin.off(ignoreLimit);
    }

    protected void low(final int delay, @NotNull final AnalogOutputPin... pins) {
        low(false,delay,pins);
    }

    protected void low(final boolean ignoreLimit, final int delay, @NotNull final AnalogOutputPin... pins) {
        low(ignoreLimit,delay,TimeUnit.MILLISECONDS,pins);
    }

    protected void low(final int delay, @NotNull final TimeUnit unit, @NotNull final AnalogOutputPin... pins) {
        low(false,delay,unit,pins);
    }

    protected void low(final boolean ignoreLimit, final int delay, @NotNull final TimeUnit unit, @NotNull final AnalogOutputPin... pins) {
        scheduler.schedule(() -> executor.submit(() -> low(ignoreLimit, pins)),delay,unit);
    }

    protected void set(Number value, @NotNull final AnalogOutputPin... pins) {
        set(value,false,pins);
    }

    protected void set(Number value, boolean ignoreLimit, @NotNull final AnalogOutputPin... pins) {
        for (final AnalogOutputPin pin : pins) pin.set(value.intValue(), ignoreLimit);
    }

    protected void set(Number value, int delay, @NotNull final AnalogOutputPin... pins) {
        set(value,false,delay,pins);
    }

    protected void set(Number value, int delay, TimeUnit unit, @NotNull final AnalogOutputPin... pins) {
        set(value,false,delay,unit,pins);
    }

    protected void set(Number value, boolean ignoreLimit, int delay, @NotNull final AnalogOutputPin... pins) {
        set(value,ignoreLimit,delay,TimeUnit.MILLISECONDS,pins);
    }

    protected void set(Number value, boolean ignoreLimit, int delay, TimeUnit unit, @NotNull final AnalogOutputPin... pins) {
        scheduler.schedule(() -> executor.submit(() -> set(value,ignoreLimit, pins)),delay,unit);
    }

    public abstract void high();
    public abstract void high(boolean ignoreLimit);
    public abstract void high(int delay);
    public abstract void high(boolean ignoreLimit, int delay);
    public abstract void high(int delay, TimeUnit unit);
    public abstract void high(boolean ignoreLimit, int delay, TimeUnit unit);
    public abstract void low();
    public abstract void low(boolean ignoreLimit);
    public abstract void low(int delay);
    public abstract void low(boolean ignoreLimit, int delay);
    public abstract void low(int delay, TimeUnit unit);
    public abstract void low(boolean ignoreLimit, int delay, TimeUnit unit);
    public abstract void set(Number value);
    public abstract void set(Number value, boolean ignoreLimit);
    public abstract void set(Number value, int delay);
    public abstract void set(Number value, boolean ignoreLimit, int delay);
    public abstract void set(Number value, int delay, TimeUnit unit);
    public abstract void set(Number value, boolean ignoreLimit, int delay, TimeUnit unit);

    private static final class ServoRaw extends Servo {

        private final AnalogOutputPin pin;

        public ServoRaw(@NotNull final Context pi4j,@NotNull final PinIdentifier powerPin,
                        final boolean externalDacChip, @Nullable final Range range) {
            super(pi4j);
            console.println("New Servo Instance created.");

            if (range == null) this.pin = new AnalogOutputPin(pi4j,powerPin,externalDacChip);
            else this.pin = new AnalogOutputPin(pi4j,powerPin,range.min.intValue(),range.max.intValue(),externalDacChip);
        }

        @Override
        public void high() {
            this.high(pin);
        }

        @Override
        public void high(final boolean ignoreLimit) {
            this.high(ignoreLimit,pin);
        }

        @Override
        public void high(final int delay) {
            this.high(delay,pin);
        }

        @Override
        public void high(final boolean ignoreLimit, final int delay) {
            this.high(ignoreLimit,delay,pin);
        }

        @Override
        public void high(final int delay, final @NotNull TimeUnit unit) {
            this.high(delay,unit,pin);
        }

        @Override
        public void high(final boolean ignoreLimit, final int delay, final @NotNull TimeUnit unit) {
            this.high(ignoreLimit,delay,unit,pin);
        }

        @Override
        public void low() {
            this.low(pin);
        }

        @Override
        public void low(final boolean ignoreLimit) {
            this.low(ignoreLimit,pin);
        }

        @Override
        public void low(final int delay) {
            this.low(delay,pin);
        }

        @Override
        public void low(final boolean ignoreLimit, final int delay) {
            this.low(ignoreLimit,delay,pin);
        }

        @Override
        public void low(final int delay, final @NotNull TimeUnit unit) {
            this.low(delay,unit,pin);
        }

        @Override
        public void low(final boolean ignoreLimit, final int delay, final @NotNull TimeUnit unit) {
            this.low(ignoreLimit,delay,unit,pin);
        }

        @Override
        public void set(@NotNull final Number value) {
            this.set(value,pin);
        }

        @Override
        public void set(@NotNull final Number value, final boolean ignoreLimit) {
            this.set(value,ignoreLimit,pin);
        }

        @Override
        public void set(@NotNull final Number value, final int delay) {
            this.set(value,delay,pin);
        }

        @Override
        public void set(final Number value, final boolean ignoreLimit, final int delay) {
            this.set(value,ignoreLimit,delay,pin);
        }

        @Override
        public void set(final Number value, final int delay, @NotNull final TimeUnit unit) {
            this.set(value,delay,unit,pin);
        }

        @Override
        public void set(@NotNull final Number value, final boolean ignoreLimit, final int delay, @NotNull final TimeUnit unit) {
            this.set(value,ignoreLimit,delay,unit,pin);
        }
    }

    private static final class ServoL298N extends Servo {

        public ServoL298N(@NotNull final Context pi4j,
                          @NotNull final PinIdentifier forwardOne, @NotNull final PinIdentifier backwardOne,
                          @NotNull final PinIdentifier forwardTwo, @NotNull final PinIdentifier backwardTwo,
                          final boolean externalDacChip, @Nullable final Range range) {
            super(pi4j,externalDacChip,range);
            console.println("New L298N Servo Instance created.");
        }
    }

    private static final class ServoL9110H extends Servo {

        public ServoL9110H(@NotNull final Context pi4j,
                           @NotNull final PinIdentifier forwardOne, @NotNull final PinIdentifier backwardOne,
                           @NotNull final PinIdentifier forwardTwo, @NotNull final PinIdentifier backwardTwo,
                           final boolean externalDacChip, @Nullable final Range range) {
            super(pi4j,externalDacChip,range);
            console.println("New L9110H Servo Instance created.");
        }
    }

    public enum Driver {
        NO_DRIVER,
        L298N,
        L9110H
    }

    public static @NotNull Builder builder(@NotNull final Context pi4j, @NotNull final Driver driver) throws IllegalStateException {
        return switch (driver) {
            case NO_DRIVER -> new Builder.RawBuilder(pi4j);
            case L298N -> new Builder.L298NBuilder(pi4j);
            case L9110H -> new Builder.L9110HBuilder(pi4j);
            default -> throw new IllegalStateException("Unknown driver: " + driver);
        };
    }

    public abstract static class Builder {

        protected Number min = 0;
        protected Number max = 1023;
        protected boolean externalDacChip = false;

        protected Builder min(@NotNull final Number minimum) {
            min = minimum;
            return this;
        }

        protected Builder max(@NotNull final Number maximum) {
            max = maximum;
            return this;
        }

        protected Builder range(@NotNull final Number minimum, @NotNull final Number maximum) {
            return min(minimum).max(maximum);
        }

        protected Builder externalDacChip(final boolean enable) {
            externalDacChip = enable;
            return this;
        }

        protected abstract Servo build();

        public static class RawBuilder extends Builder {

            protected final Context pi4j;
            private PinIdentifier powerPin;

            public RawBuilder(@NotNull final Context pi4j) {
                this.pi4j = pi4j;
            }

            public Builder pin(final int pin) {
                return pin(PinIdentifier.getPin(pin));
            }

            public Builder pin(@NotNull final PinIdentifier pin) {
                powerPin = pin;
                return this;
            }

            @Override
            public Servo build() throws IllegalArgumentException {
                if (powerPin == null) throw new IllegalArgumentException("Cannot create a Servo instance with no power pin defined!");

                return new ServoRaw(pi4j,powerPin,externalDacChip,new Range(min,max));
            }
        }

        protected static final class L298NBuilder extends Builder {

            public L298NBuilder(@NotNull final Context pi4j) {
                super(pi4j);
            }

            @Override
            protected Servo build() {
                return null;
            }
        }

        protected static final class L9110HBuilder extends Builder {

            public L9110HBuilder(@NotNull final Context pi4j) {
                super(pi4j);
            }

            @Override
            protected Servo build() {
                return null;
            }
        }
    }

    private static final class ServoUtil {
        public static Range validateRangeValues(Number min, Number max, final Boolean externalDacChip) throws IllegalArgumentException {
            final boolean dac = externalDacChip != null && externalDacChip;
            if (min == null) {
                min = 0;
            } else {
                if (min.intValue() < 0) throw new IllegalArgumentException("Minimum range cannot be less than 0!");
                else if (min.intValue() > 1023 && dac) throw new IllegalArgumentException("Minimum range cannot be higher than 1024 without the use of an external DAC Chip!");
                else if (min.intValue() > 4095) throw new IllegalArgumentException("Minimum range cannot be higher than 4095!");
            }
            if (max == null) {
                max = dac ? 4095 : 1024;
            } else {
                if (dac && max.intValue() > 4095) throw new IllegalArgumentException("Maximum range cannot be more than 0!");
                else if (max.intValue() > 1023) throw new IllegalArgumentException("Maximum range cannot be less than 1023!");
                if (min.doubleValue() > max.doubleValue()) throw new IllegalArgumentException("Minimum range cannot be more than maximum range!");
            }
            return new Range(min.intValue(), max.intValue());
        }
    }
}