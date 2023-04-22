package cz.meind.microomega.User;

import java.util.LinkedList;

public class HookExchange {
    private User hookOne;
    private User hookTwo;
    private LinkedList<Hook> hooks;

    public HookExchange(User hookOne, User hookTwo) {
        this.hookOne = hookOne;
        this.hookTwo = hookTwo;
        hooks = new LinkedList<>();
    }

    public User getHookOne() {
        return hookOne;
    }

    public void setHookOne(User hookOne) {
        this.hookOne = hookOne;
    }

    public User getHookTwo() {
        return hookTwo;
    }

    public void setHookTwo(User hookTwo) {
        this.hookTwo = hookTwo;
    }

    public LinkedList<Hook> getHooks() {
        return hooks;
    }

    public void setHooks(LinkedList<Hook> hooks) {
        this.hooks = hooks;
    }
}
