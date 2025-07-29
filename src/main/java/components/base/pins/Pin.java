package components.base.pins;

import com.pi4j.context.Context;
import com.pi4j.exception.ShutdownException;
import components.base.pins.components.PinIdentifier;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

import static components.base.pins.components.PinType.*;

@SuppressWarnings("unused")
public abstract class Pin {

    private static final Set<PinIdentifier> activePins = new HashSet<>();

    protected final Context pi4j;
    protected final PinIdentifier identifier;

    protected Pin(@NotNull final Context pi4j, @NotNull final PinIdentifier pin) throws IllegalStateException, IllegalArgumentException {
        if (!isPinAvailable(pin)) throw new IllegalStateException("Failed to initiate Pin %d. Pin is already in use!".formatted(pin.getPin()));
        else if (!isPinLegal(pin)) throw new IllegalArgumentException("Failed to initiate Pin %d. Pin is not configurable!".formatted(pin.getPin()));
        this.pi4j = pi4j;
        this.identifier = pin;
        activePins.add(this.identifier);
    }

    protected void destroy(@NotNull final Pin instance) throws ShutdownException {
        instance.pi4j.shutdown();
        activePins.remove(instance.identifier);
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
        return pin.getType() == GPIO;
    }
}