package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.util.ElapsedTime;

/**
 * Class which manages a PID controller.
 * <br>
 * Kp, Ki, and Kd should be tuned and passed into the constructor.
 * <br>
 * For more information, see https://www.ctrlaltftc.com/the-pid-controller
 */
public class PIDController {
    private double target;
    private double integralSum = 0;
    private double lastError;

    private double Kp;
    private double Ki;
    private double Kd;

    private final ElapsedTime timer = new ElapsedTime();
    private boolean started = false;

    public PIDController(double Kp, double Ki, double Kd, double target) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        this.target = target;
    }

    public double calculate(double current) {
        // The PID loop has not been started yet, so ignore any time calculated before this point
        if (!started) {
            timer.reset();
            started = true;
        }

        double error = target - current; //should be target - current

        double deltaTime = timer.seconds();

        // derivative, AKA rate of change of the error
        double derivative = (error - lastError) / deltaTime;
        // calculate the Riemann sum of the error, also known as Forward Euler Integration.
        integralSum += error * deltaTime;

        // add all the parts together
        double out = (Kp * error) + (Ki * integralSum) + (Kd * derivative);

        // track error over time
        lastError = error;
        // set timer back to 0 and resume counting
        timer.reset();

        return out;
    }

    /**
     * Resets the controller to state 0 (not started, no integralSum, no lastError).
     * Useful after changing controller parameters.
     * @see #setParams(double, double, double, double)
     */
    public void reset() {
        started = false;
        integralSum = 0;
        lastError = 0;
    }

    /**
     * This method is for setting parameters to new values.
     * May be helpful to call {@link #reset()} after this.
     * @see #reset()
     * @param Kp     Proportional gain
     * @param Ki     Integral gain
     * @param Kd     Derivative gain
     * @param target Target point to reach
     */
    public void setParams(double Kp, double Ki, double Kd, double target) {
        this.Kp = Kp;
        this.Ki = Ki;
        this.Kd = Kd;
        this.target = target;
    }
}
