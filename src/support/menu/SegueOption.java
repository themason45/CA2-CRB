package support.menu;

import java.util.Arrays;

/**
 * List option which acts as a segue to a given class
 */
public class SegueOption extends BaseMenuOption {

    public SegueOption(String title, Class<? extends BaseMenu> viewClass, BaseMenu previousMenu) {
        super(title);
        this.methodClass = viewClass;
        this.methodIdentifier = "draw";
        this.defaultArgs = new Object[]{previousMenu};

        this.selectable = true;
    }

    public SegueOption(String title, Class<? extends BaseMenu> viewClass, BaseMenu previousMenu, Object ... extraArgs) {
        super(title);
        this.methodClass = viewClass;
        this.methodIdentifier = "draw";

        this.defaultArgs = new Object[]{previousMenu};
        Object[] copy = Arrays.copyOf(this.defaultArgs, this.defaultArgs.length + extraArgs.length);
        if (copy.length - 1 >= 0) System.arraycopy(extraArgs, 0, copy, 1, copy.length - 1);
        this.defaultArgs = copy;

        this.selectable = true;
    }
}
