package support.menu;

import java.util.Arrays;

/**
 * List option which acts as a segue to a given class. It simply runs
 * the {@link BaseMenu#draw()} method on a given Class ({@link #methodClass}, and allows extra constructor arguments.
 */
public class SegueOption extends BaseMenuOption {

    public SegueOption(String title, Class<? extends BaseMenu> viewClass, BaseMenu previousMenu, Object ... extraArgs) {
        super(title);
        this.methodClass = viewClass;
        this.methodIdentifier = "draw";

        this.classArgs = new Object[]{previousMenu};
        Object[] copy = Arrays.copyOf(this.classArgs, this.classArgs.length + extraArgs.length);
        if (copy.length - 1 >= 0) System.arraycopy(extraArgs, 0, copy, 1, copy.length - 1);
        this.classArgs = copy;

        this.selectable = true;
    }
}
