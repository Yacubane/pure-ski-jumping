package pl.cyfrogen.skijumping.game.physics;

import java.util.ArrayList;

import static pl.cyfrogen.skijumping.game.physics.PhysicsUtils.EPS;
import static pl.cyfrogen.skijumping.game.physics.PhysicsUtils.isZero;

public class SkiJumpPhysics {
    private final Point GRAVITY = new Point(0, -9.8f);
    private final double DEFAULT_KINETIC_FRICTION_COEFFICIENT = 0.01f;
    private double JUMPER_MASS = 60;

    private double kineticFrictionCoefficient = DEFAULT_KINETIC_FRICTION_COEFFICIENT;
    private double staticFrictionCoefficient = kineticFrictionCoefficient * 1.1f;

    private final double tickTime;
    private ArrayList<Edge> edges;

    private Point jumperPosition = new Point();
    private Point oldJumperPosition = new Point();
    private Point newJumperPosition = new Point();

    private Point jumperVelocity = new Point();
    private double overTime = 0;
    private CollidingListener collidingListener;
    private boolean pause;
    private Point force = new Point();
    private TickListener tickListener;


    public SkiJumpPhysics(double tickTime) {
        this.tickTime = tickTime;
        edges = new ArrayList<Edge>();
    }

    public SkiJumpPhysics() {
        this(1 / (double) 120);
    }

    public void addEdgeGroup(EdgeGroup edgeGroup) {
        edges.addAll(edgeGroup.getEdges());
    }

    public void setJumperPosition(double x, double y) {
        jumperPosition.set(x, y);
        newJumperPosition.set(jumperPosition);
        oldJumperPosition.set(jumperPosition);
        overTime = 0;
    }

    public Point getJumperPosition() {
        return jumperPosition;
    }


    public void update(double deltaTime) {
        if (pause) return;
        if (overTime > deltaTime) {
            overTime -= deltaTime;
            double timeOfTick = tickTime - overTime;
            double lerpPercentage = timeOfTick / tickTime;
            jumperPosition.set(oldJumperPosition.lerp(newJumperPosition, lerpPercentage));
            return;
        } else {
            deltaTime -= overTime;
            jumperPosition.set(newJumperPosition);
            overTime = 0;
        }

        int fullTicks = (int) (deltaTime / tickTime);
        int ticks = fullTicks + 1;
        overTime = (ticks * tickTime) - deltaTime;
        double timeOfLastTick = tickTime - overTime;

        for (int i = 0; i < fullTicks; i++) {
            update(tickTime, null, true);
        }

        oldJumperPosition.set(jumperPosition);
        update(tickTime, null, true);
        newJumperPosition.set(jumperPosition);

        double lerpPercentage = timeOfLastTick / tickTime;
        jumperPosition.set(oldJumperPosition.lerp(newJumperPosition, lerpPercentage));

    }


    private void beginTickInEdges() {
        for (Edge edge : edges) {
            edge.beginTick();
        }
    }


    private void endTickInEdges() {
        for (Edge edge : edges) {
            if (edge.endTick()) {
                collidingListener.endColliding(edge, jumperPosition); //todo jumperPosition isn't equal to collision point
            }
        }
    }

    private void update(double deltaTime, Edge ignoredEdge, boolean firstUpdateOfTick) {
        if (firstUpdateOfTick && tickListener != null) {
            tickListener.tickStart();
        }
        beginTickInEdges();

        Point newJumperPosition = new Point(jumperPosition);

        //x = x0 + v0t + at^2/2
        newJumperPosition.add(jumperVelocity.cpy().scl(deltaTime));
        newJumperPosition.add(GRAVITY.cpy().scl(deltaTime * deltaTime * 0.5));
        newJumperPosition.add(force.cpy().scl(deltaTime * deltaTime * 0.5 * (1 / JUMPER_MASS)));

        Collision collision = checkCollision(jumperPosition, newJumperPosition, ignoredEdge);

        if (!collision.collides()) {
            jumperPosition.set(newJumperPosition);
            // v = v0 + at
            jumperVelocity.add(GRAVITY.cpy().scl(deltaTime));
            jumperVelocity.add(force.cpy().scl(deltaTime * (1 / JUMPER_MASS)));

        } else {

            Point collisionEdgeDirectionVector = collision.getEdge().getDirection();
            Point velocityVector = jumperVelocity.cpy().nor();

            double angleRad = PhysicsUtils.getAngleBetweenTwoVectors(
                    velocityVector, collisionEdgeDirectionVector);

            double velocityScale = 1 - Math.sin(Math.abs(angleRad));

            if (Math.abs(angleRad) > Math.PI / 2) {
                velocityScale *= -1;
            }

            if (collision.isOnEdge()) {
                jumperPosition.set(collision.getPoint());
                handlePhysicOnEdge(collision.getEdge(), collision.getPoint(),
                        jumperVelocity.len() * velocityScale,
                        Time.of(deltaTime));
            } else {

                //s = s0 + vt
                //0 = vt + (s0-s)
                // t = (s-s0) / v
                double timeToGoToCollisionPoint =
                        jumperPosition.dst(collision.getPoint()) / jumperVelocity.len();
                double remainingTime = deltaTime - timeToGoToCollisionPoint;

                if (remainingTime >= 0) {
                    jumperPosition.set(collision.getPoint());

                    handlePhysicOnEdge(collision.getEdge(), collision.getPoint(),
                            jumperVelocity.len() * velocityScale,
                            Time.of(remainingTime));
                } else {
                    jumperPosition.set(collision.getPoint());
                }


            }
        }

        endTickInEdges();
        if (firstUpdateOfTick && tickListener != null) {
            tickListener.tickEnd();
        }
    }


    private Collision checkCollision(Point jumperPosition, Point newJumperPosition, Edge ignoredEdge) {
        ArrayList<Edge> edgesToCheckCollision = new ArrayList<Edge>();
        AABB jumperPositionChangeAABB = AABB.of(jumperPosition, newJumperPosition);

        for (Edge edge : edges) {
            if (AABB.intersects(edge.getAABB(), jumperPositionChangeAABB)) {
                edgesToCheckCollision.add(edge);
            }
        }

        double smallestDistanceToCollidingEdge = 0f;
        Edge collidingEdgeWithSmallestDistance = null;
        Point collisionPointWithSmallestDistance = null;

        Edge nearestEdge = null;
        double nearestEdgeDistance = 0;

        Point collisionPoint = new Point();

        for (Edge edge : edgesToCheckCollision) {
            if (edge == ignoredEdge) continue;

            double distance = PhysicsUtils.distanceSegmentPoint(
                    edge.getLine().getPoint1(),
                    edge.getLine().getPoint2(),
                    jumperPosition);
            if (nearestEdge == null) {
                nearestEdge = edge;
                nearestEdgeDistance = distance;
            } else {
                if (distance < nearestEdgeDistance) {
                    nearestEdge = edge;
                    nearestEdgeDistance = distance;
                }
            }
        }

        if (nearestEdge != null && PhysicsUtils.distanceSegmentPoint(
                nearestEdge.getLine().getPoint1(),
                nearestEdge.getLine().getPoint2(),
                jumperPosition) < EPS) {

            //if velocity is intersecting nearestEdge from one side
            if (PhysicsUtils.getAngleBetweenTwoVectors(jumperVelocity, nearestEdge.getDirection()) >= -EPS) {
                Point nearestPoint = new Point();
                PhysicsUtils.nearestLinePoint(
                        nearestEdge.getLine().getPoint1(),
                        nearestEdge.getLine().getPoint2(),
                        jumperPosition,
                        nearestPoint);
                return new Collision(nearestPoint, nearestEdge, true);
            }
        }


        for (Edge edge : edgesToCheckCollision) {
            if (edge == ignoredEdge) continue;

            double distanceToJumperPosition = PhysicsUtils.distanceSegmentPoint(
                    edge.getLine().getPoint1(),
                    edge.getLine().getPoint2(),
                    jumperPosition);

            double distanceToNewJumperPosition = PhysicsUtils.distanceSegmentPoint(
                    edge.getLine().getPoint1(),
                    edge.getLine().getPoint2(),
                    newJumperPosition);

            boolean collides = false;

            if (distanceToJumperPosition > EPS) {
                collides = PhysicsUtils.intersectSegments(
                        jumperPosition, newJumperPosition,
                        edge.getLine().getPoint1(), edge.getLine().getPoint2(),
                        collisionPoint);
            }

            if (!collides) {
                if (distanceToNewJumperPosition < EPS &&
                        distanceToNewJumperPosition < distanceToJumperPosition) {
                    collides = PhysicsUtils.intersectLines(
                            jumperPosition, newJumperPosition,
                            edge.getLine().getPoint1(), edge.getLine().getPoint2(),
                            collisionPoint);
                }
            }
            if (collides) {
                if (collidingEdgeWithSmallestDistance != null) {
                    // this isn't first colliding edge - check if distance is smaller than any other
                    // colliding edge
                    if (jumperPosition.dst(collisionPoint) < smallestDistanceToCollidingEdge) {
                        smallestDistanceToCollidingEdge = jumperPosition.dst(collisionPoint);
                        collidingEdgeWithSmallestDistance = edge;
                        collisionPointWithSmallestDistance = new Point(collisionPoint);
                    }
                } else {
                    // first colliding edge - just save it
                    smallestDistanceToCollidingEdge = jumperPosition.dst(collisionPoint);
                    collidingEdgeWithSmallestDistance = edge;
                    collisionPointWithSmallestDistance = new Point(collisionPoint);
                }
            }
        }
        return new Collision(collisionPointWithSmallestDistance, collidingEdgeWithSmallestDistance);
    }


    private void handlePhysicOnEdge(Edge collidingEdge, Point point, double velocity, Time remainingTime) {
        handleStartColliding(collidingEdge, point);

        double gravityForce = JUMPER_MASS * Math.abs(GRAVITY.y);

        //if edge.isElevation, then slidingForce goes towards P1 of Edge
        int gravityForceDirection = collidingEdge.isElevation() ? -1 : 1;

        int frictionForceDirection;
        if (isZero(velocity)) {
            frictionForceDirection = -gravityForceDirection;
        } else {
            frictionForceDirection = velocity > 0 ? -1 : 1;
        }

        double slidingForce = gravityForceDirection
                * gravityForce
                * Math.sin(collidingEdge.getInclinedPlaneAngle());

        double maxStaticFrictionForce = frictionForceDirection
                * gravityForce
                * Math.cos(collidingEdge.getInclinedPlaneAngle())
                * staticFrictionCoefficient;

        double kineticFrictionForce = frictionForceDirection
                * gravityForce
                * Math.cos(collidingEdge.getInclinedPlaneAngle())
                * kineticFrictionCoefficient;


        if (isZero(velocity) && Math.abs(slidingForce) < Math.abs(maxStaticFrictionForce)) {
            return;
        }

        double combinedForce = slidingForce + kineticFrictionForce;
        double acceleration = combinedForce / JUMPER_MASS;

        boolean slowingDown = acceleration * velocity < 0; //different signs

        Time timeToStopMoving = Time.none();
        if (!isZero(velocity) && !isZero(acceleration) && slowingDown) {
            //v = v0 + at
            //t = (0-v0)/a
            timeToStopMoving = Time.of(-velocity / acceleration);
        }

        Point approachingDirection = collidingEdge.getDirection().cpy();

        Point approachingPoint = null;
        Edge approachingEdge = null;

        int approachingDirectionInt = getDirectionInteger(velocity, acceleration);
        if (approachingDirectionInt == 1) {
            approachingPoint = collidingEdge.getLine().getPoint2();
            approachingEdge = collidingEdge.getNextEdge();
        } else if (approachingDirectionInt == -1) {
            approachingPoint = collidingEdge.getLine().getPoint1();
            approachingEdge = collidingEdge.getPrevEdge();
        }

        double distanceToEndOfEdge = approachingPoint.cpy().sub(jumperPosition).len();
        distanceToEndOfEdge *= approachingDirectionInt;

        Time timeToGetToEndOfEdge = getTimeOfGettingToEndOfEdge(
                acceleration,
                velocity,
                distanceToEndOfEdge);

        Time smallestTime = Time.getSmallest(timeToStopMoving, timeToGetToEndOfEdge, remainingTime);
        Time newRemainingTime = Time.of(remainingTime.get() - smallestTime.get());

        //s = at^2/2 + vt + s0
        double moveDistance = acceleration * smallestTime.get() * smallestTime.get() / 2f
                + velocity * smallestTime.get();
        jumperPosition.add(approachingDirection.cpy().scl(moveDistance));
        setJumperPositionAsNearestToEdge(collidingEdge);

        //v = at + v0
        double newVelocity = acceleration * smallestTime.get() + velocity;
        jumperVelocity.set(approachingDirection).scl(newVelocity);

        if (smallestTime == remainingTime) {
            //everything done :)
        } else if (smallestTime == timeToStopMoving) {
            handlePhysicOnEdge(collidingEdge, jumperPosition, 0, newRemainingTime);
        } else if (smallestTime == timeToGetToEndOfEdge) {
            jumperPosition.set(approachingPoint);
            handleEndColliding(collidingEdge, approachingEdge, jumperPosition);
            if (approachingEdge == null) {
                update(newRemainingTime.get(), collidingEdge, false);
            } else {
                handlePhysicOnEdge(approachingEdge, jumperPosition, newVelocity, newRemainingTime);
            }
        }
    }


    private void handleStartColliding(Edge collidingEdge, Point point) {
        if (!collidingEdge.isWasColliding()) {
            if (collidingListener != null) {
                collidingListener.startColliding(collidingEdge, point);
            }
        }
        collidingEdge.setColliding();
    }

    private void handleEndColliding(Edge collidingEdge, Edge approachingEdge, Point point) {
        if (collidingListener != null) {
            if (approachingEdge == null) {
                collidingListener.endColliding(collidingEdge, point);
            } else {
                collidingListener.nextColliding(collidingEdge, approachingEdge, point);
            }
        }
        collidingEdge.endColliding();
    }

    private int getDirectionInteger(double velocity, double acceleration) {
        if (isZero(velocity) && isZero(acceleration)) {
            return 0;
        } else if (isZero(velocity) && !isZero(acceleration)) {
            if (acceleration > 0) {
                return 1;
            } else if (acceleration < 0) {
                return -1;
            }
        } else if (velocity > 0) {
            return 1;
        } else if (velocity < 0) {
            return -1;
        }
        return 0;
    }


    public void setJumperVelocity(double x, double y) {
        jumperVelocity.set(x, y);
    }

    public Point getJumperVelocity() {
        return jumperVelocity;
    }

    public void setJumperPositionAsNearestToEdge(Edge collidingEdge) {
        Point nearest = new Point();
        PhysicsUtils.nearestLinePoint(
                collidingEdge.getLine().getPoint1(),
                collidingEdge.getLine().getPoint2(),
                jumperPosition,
                nearest);
        jumperPosition.set(nearest);
    }

    public Time getTimeOfGettingToEndOfEdge(double acceleration,
                                            double velocity,
                                            double distanceToEndOfEdge) {
        if (!isZero(acceleration)) {
            //s = at^2/2 + vt, t = ?
            //0 = at^2/2 + vt - s
            Roots roots = RootsCalculator.calculateRoots(
                    acceleration / 2f,
                    velocity,
                    -distanceToEndOfEdge);
            if (roots.isPositiveRoot()) {
                return Time.of(roots.getSmallestPositiveRoot());
            }
        } else if (!isZero(velocity)) {
            //t = s/v
            return Time.of(distanceToEndOfEdge / velocity);
        }
        return Time.none();
    }

    public void setCollidingListener(CollidingListener collidingListener) {
        this.collidingListener = collidingListener;
    }

    public void resume() {
        this.pause = false;

    }

    public void pause() {
        this.pause = true;
    }

    public void setForce(Point force) {
        this.force.set(force);
    }

    public double getTickTime() {
        return tickTime;
    }

    public void setTickListener(TickListener tickListener) {
        this.tickListener = tickListener;
    }

    public boolean isStopped() {
        return pause;
    }

    public RayCastResult rayCast(Point point, Point dir) {
        dir.scl(50); //todo change this 50 meters please
        Point point2 = point.cpy().add(dir);
        point.add(dir.cpy().scl(-1));
        Point tmp = new Point();
        Point result = new Point();
        double actualDistanceSqr = 0;
        boolean found = false;
        Edge collidedEdge = null;
        for (Edge edge : edges) {
            if (PhysicsUtils.intersectSegments(point, point2,
                    edge.getLine().getPoint1(),
                    edge.getLine().getPoint2(),
                    tmp)) {
                if (!found) {
                    found = true;
                    actualDistanceSqr = tmp.dst2(point);
                    result.set(tmp);
                    collidedEdge = edge;
                } else {
                    if (tmp.dst2(point) < actualDistanceSqr) {
                        actualDistanceSqr = tmp.dst2(point);
                        result.set(tmp);
                        collidedEdge = edge;
                    }
                }
            }
        }

        return new RayCastResult(result, collidedEdge, found);
    }

    public void reset() {
        force.set(0, 0);
        jumperVelocity.set(0, 0);
        setKineticFrictionCoefficient(DEFAULT_KINETIC_FRICTION_COEFFICIENT);
    }

    public void setKineticFrictionCoefficient(double kineticFrictionCoefficient) {
        this.kineticFrictionCoefficient = kineticFrictionCoefficient;
        staticFrictionCoefficient = kineticFrictionCoefficient * 1.1f;
    }


}

