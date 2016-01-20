package org.n3r.sandbox.customclassloader;

public class StaticAccessor {
    /**
     * Accesses the static field from StaticHolder class.
     * It reads its value and after that sets it to 4.
     */
    public void runMe() {
        System.out.println("--- Starting runMe. Static value: "
                + StaticHolder.value);
        StaticHolder.value = 4;
        System.out.println("--- Finishing runMe. Static value: "
                + StaticHolder.value);
    }
}
