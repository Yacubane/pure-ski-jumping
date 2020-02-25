package pl.cyfrogen.skijumping.game.physics;

public class RootsCalculator {

    public static Roots calculateRoots(double a, double b, double c) {
        double root1, root2;
        double determinant = b * b - 4 * a * c;
        if (determinant > 0) {
            root1 = ((-b + Math.sqrt(determinant)) / (2 * a));
            root2 = ((-b - Math.sqrt(determinant)) / (2 * a));
            return Roots.of(root1, root2);
        } else if (determinant == 0) {
            root1 = -b / (2 * a);
            return Roots.of(root1);
        } else {
            return Roots.none();
        }
    }

}
