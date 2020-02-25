package pl.cyfrogen.skijumping.game.physics;

public class Roots {
    private final double[] roots;

    public Roots(double... roots) {
        this.roots = roots;
    }

    public static Roots of(double root1, double root2) {
        return new Roots(root1, root2);
    }

    public static Roots of(double root1) {
        return new Roots(root1);
    }

    public static Roots none() {
        return new Roots();
    }

    public double[] getRoots() {
        return roots;
    }


    public double getSmallestPositiveRoot() {
        boolean foundRoot = false;
        double foundedRoot = 0;

        for (double root : roots) {
            if (root >= 0) {
                if (!foundRoot) {
                    foundRoot = true;
                    foundedRoot = root;
                } else {
                    if (root < foundedRoot) {
                        foundedRoot = root;
                    }
                }
            }
        }

        if (!foundRoot) {
            throw new ArithmeticException("There is not positive root");
        }
        return foundedRoot;
    }

    public boolean isPositiveRoot() {
        for (double root : roots) {
            if (root >= 0) {
                return true;
            }
        }
        return false;
    }
}
