package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name="SensorTest")
public class SensorTest extends OpMode {
    //Variables
    private ElapsedTime runTime = new ElapsedTime();
    private double kShoot = 1.0;
    private double kIntake = 3.0;

    boolean SequenceRunning = false;


    //Sensores
    //Thrs=rango
    private double ArticleInThrs = 6;
    private double ClearThrs = 7;
    private double TopInThrs = 6;

    int CurrentCount = 0;
    double TopDistance;
    double IntakeDistance;

    boolean ArtHasEntered = false;
    boolean FullRamp = false;
    boolean ShooterActive = false;



    //Sensor
    ColorRangeSensor SenIntake;
    ColorRangeSensor SenTop;


// Posible añadir Servo


    public void init() {


        //Sensores
        SenIntake = hardwareMap.get(ColorRangeSensor.class, "SenIntake");
        SenTop = hardwareMap.get(ColorRangeSensor.class, "SenTop");
    }

    //Sensores
    public boolean CountThree (){
        IntakeDistance = SenIntake.getDistance(DistanceUnit.CM);
        Boolean IsBallHere = IntakeDistance<ArticleInThrs;
        Boolean IsClear = IntakeDistance<ClearThrs;

        if(IsBallHere&&!ArtHasEntered&&!FullRamp){
            CurrentCount ++;
            ArtHasEntered = true;
        }

        if(IsClear){
            ArtHasEntered = false;
        }
        if (CurrentCount == 3){
            FullRamp = true;
        }
        return FullRamp;

    }

    public void resetCount() {
        CurrentCount = 0;
        FullRamp = false;
    }



    public void loop() {

        if (gamepad2.a || gamepad2.b || gamepad2.x){
            if (!SequenceRunning){
                runTime.reset();
                SequenceRunning = true;
            }
            if (runTime.seconds() >= kShoot){
                //ConveyorPwr = 0.8;
                if (runTime.seconds() >= kIntake) {
                    //IntakePwr = 1;
                    if (runTime.seconds() >= 4){
                        //IntakePwr = 0;
                    }
                }
            }
        }

        else {
            //ShooterPwr = 0;

            if (gamepad2.right_bumper){
                //IntakePwr = 0.9;
            }

            //--Conveyor --
            if (gamepad2.dpad_up){
                //ConveyorPwr = 0.8;
            }
            else if (gamepad2.dpad_down){
                //ConveyorPwr = -0.8;
            }

            SequenceRunning = false;
        }

        //---telemetry---
        //telemetry.addData("current count", currentCount);
        if (FullRamp) {
            telemetry.addLine("Ramp is full");
        }
    }

}

