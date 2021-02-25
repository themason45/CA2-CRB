package support.menu;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * A menu option is an item in a Menu ({@link BaseMenu}) which can be either selectable, or not.
 * If it is not selectable, then it will display as a sort of sub heading within the list.
 * If it is selectable, then it will display with an option number before it, and when selected, will execute the
 * desired method on a new instance (instantiated using the {@link #classArgs} attribute) of the desired class.
 */
public class BaseMenuOption {
    public String title;
    public Class<?> methodClass;
    public String methodIdentifier;

    protected Object[] classArgs;
    public Object[] funcArgs;
    public boolean selectable;  // If it is a sub header in the list

    public BaseMenuOption(String title) {
        this.title = title;
        this.selectable = false;

        this.classArgs = new Object[0];
    }

    public BaseMenuOption(String title, Class<?> methodClass, String methodIdentifier) {
        this.title = title;
        this.methodClass = methodClass;
        this.methodIdentifier = methodIdentifier;

        this.selectable = true;
        this.classArgs = new Object[0];
    }

    public BaseMenuOption(String title, Class<?> methodClass, String methodIdentifier, Object... initArgs) {
        this.title = title;
        this.methodClass = methodClass;
        this.methodIdentifier = methodIdentifier;

        this.selectable = true;
        this.classArgs = new Object[0];
        this.funcArgs = initArgs;
    }

    /**
     * Creates an instance of the given class {@link #methodClass}, using given arguments in {@link #classArgs},
     * and invokes the method given by {@link #methodIdentifier}.
     *
     * @param initArgs Any arguments to be passed to the method when it is invoked
     */
    public void execute(Object... initArgs) {
        // Use initArgs passed here, if none are passed earlier
        if (this.funcArgs != null) if (this.funcArgs.length > 0) initArgs = this.funcArgs;

        try {
            Method method = methodClass.getMethod(methodIdentifier, Arrays.stream(initArgs).map(Object::getClass).toArray(Class<?>[]::new));

            Class<?>[] argTypes = Arrays.stream(classArgs).map(Object::getClass).toArray(Class<?>[]::new);
            Constructor<?> constructor = methodClass.getConstructor(argTypes);
            method.invoke(constructor.newInstance(classArgs), initArgs);
        } catch (NoSuchMethodException | IllegalAccessException | InstantiationException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }

    /**
     * Invokes the method given by {@link #methodIdentifier} on a given instance.
     *
     * @param instance The instance to invoke the method on
     * @param initArgs Any arguments to be passed to the method when it is invoked
     * @return The result, if applicable, of the method when it is invoked
     * @throws Exception If the invoked method could throw an execution, this is passed to the process that called this
     * method
     */
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

    /**
     * @param message Method to redraw with
     * @return A {@link BaseMenuOption} which has the redraw method identifier of the {@link NoOpClass} which is
     * recognised after the option is invoked in {@link BaseMenu#selectOption(int)}.
     */
    public static BaseMenuOption REDRAW(String message) {
        return new BaseMenuOption("Redraw", NoOpClass.class, "redraw", message);
    }

    /**
     * @return The title of this option
     */
    @Override
    public String toString() {
        return this.title;
    }
}

