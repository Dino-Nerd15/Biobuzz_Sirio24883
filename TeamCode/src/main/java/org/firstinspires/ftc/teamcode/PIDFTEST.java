package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp public class PIDFTEST extends OpMode {

    DcMotorEx shooterPollen;
    double HighVel=5000;
    double LowVel=3000;
    double curTargetVelocity = HighVel;
    double F=0;
    double P=0;
    double [] stepSizes={10.0,1.0,0.1,0.001, 0.00001};
    int stepIndex = 0;
    @Override
    public void init(){

        shooterPollen=hardwareMap.get(DcMotorEx.class,"shooterPollen");
        shooterPollen.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterPollen.setDirection(DcMotorSimple.Direction.REVERSE);
        PIDFCoefficients pidfCoefficients=new PIDFCoefficients(P,0,0,F);
        shooterPollen.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,pidfCoefficients);
}

    @Override
    public void loop() {
    if (gamepad1.yWasPressed()){
        if (curTargetVelocity==HighVel) {
            curTargetVelocity=LowVel;}
        else {curTargetVelocity=HighVel;}
    }

    if (gamepad1.backWasPressed()){
        stepIndex=(stepIndex+1)%stepSizes.length;}

    if(gamepad1.dpadLeftWasPressed()){
        F-=stepSizes[stepIndex];}

    if(gamepad1.dpadRightWasPressed()){
        F+=stepSizes[stepIndex];}

    if (gamepad1.dpadUpWasPressed()) {
    P+=stepSizes[stepIndex];}

        PIDFCoefficients pidfCoefficients=new PIDFCoefficients(P,0,0,F);
        shooterPollen.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER,pidfCoefficients);

        shooterPollen.setVelocity(curTargetVelocity);
        double curVelocity=shooterPollen.getVelocity();
        double error=curTargetVelocity-curVelocity;

        telemetry.addData("Target Vel", curTargetVelocity);
        telemetry.addData("Current Vel","%.2f", curVelocity);
        telemetry.addData("Error","%.2f", error);
        telemetry.addData("Tuning P", "%.4f(D-Pad U/D)",P);
        telemetry.addData("Tuning F", "%.4f (D-Pad L/R)",F);
        telemetry.addData("Step Size, %.4d (B Button)", stepSizes[stepIndex]);
    }
    }