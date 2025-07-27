package components.base;

import com.pi4j.context.Context;

public abstract class Component {

    protected final Context pi4j;

    protected Component(final Context pi4j) {
        this.pi4j = pi4j;
    }

}
