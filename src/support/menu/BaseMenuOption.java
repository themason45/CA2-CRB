package support.menu;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * A menu option is an item in a Menu ({@link BaseMenu}) which can be either selectable, or not.
 * If it is not selectable, then it will display as a sort of sub heading within the list.
 * If it is selectable, then it will display with an option number before it, and when selected, will execute the
 * desired method on a new instance (instantiated using the {@link #defaultArgs} attribute) of the desired class.
 */
public class BaseMenuOption {
    public String title;
    public Class<?> methodClass;
    public String methodIdentifier;

    protected Object[] defaultArgs;
    public Object[] funcArgs;
    public boolean selectable;  // If it is a sub header in the list

    public BaseMenuOption(String title) {
        this.title = title;
        this.selectable = false;

        this.defaultArgs = new Object[0];
    }

    public BaseMenuOption(String title, Class<?> methodClass, String methodIdentifier) {
        this.title = title;
        this.methodClass = methodClass;
        this.methodIdentifier = methodIdentifier;

        this.selectable = true;
        this.defaultArgs = new Object[0];
    }

    public BaseMenuOption(String title, Class<?> methodClass, String methodIdentifier, Object... initArgs) {
        this.title = title;
        this.methodClass = methodClass;
        this.methodIdentifier = methodIdentifier;

        this.selectable = true;
        this.defaultArgs = new Object[0];
        this.funcArgs = initArgs;
    }

    public void execute(Object... initArgs) {
        // Use initArgs passed here, if none are passed earlier
        if (this.funcArgs != null) if (this.funcArgs.length > 0) initArgs = this.funcArgs;

        try {
            Method method = methodClass.getMethod(methodIdentifier, Arrays.stream(initArgs).map(Object::getClass).toArray(Class<?>[]::new));

            Class<?>[] argTypes = Arrays.stream(defaultArgs).map(Object::getClass).toArray(Class<?>[]::new);
            Constructor<?> constructor = methodClass.getConstructor(argTypes);
            method.invoke(constructor.newInstance(defaultArgs), initArgs);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    @SuppressWarnings("RedundantThrows")
    public Object executeOnInstance(Object instance, Object... initArgs) throws Exception {
        Object[] args = Arrays.copyOf(funcArgs, funcArgs.length + initArgs.length);

        if (args.length - funcArgs.length >= 0)
            System.arraycopy(initArgs, 0, args, funcArgs.length, args.length - funcArgs.length);

        try {
            Method method = methodClass.getMethod(methodIdentifier, Arrays.stream(args).map(Object::getClass).toArray(Class<?>[]::new));
            return method.invoke(instance, args);
        } catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static BaseMenuOption NOOP = new BaseMenuOption("No operation", NoOpClass.class, "noop");
    public static BaseMenuOption REDRAW(String message) {
        return new BaseMenuOption("Redraw", NoOpClass.class, "redraw", message);
    }

    @Override
    public String toString() {
        return this.title;
    }
}

/**
 * Used by the NOOP static option, does absolutely nothing
 */
class NoOpClass {
    public NoOpClass() {}
    @SuppressWarnings("unused")
    public void noop() {}
    public void redraw() {}
}