package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;


@TeleOp (name = "VideoServo")
public class VideoServo {


    Servo Servo;
    double Garra_abierta;
    double Garra_cerrada;


    public void init() {
        Servo = hardwareMap.get(Servo.class,"Servo");


        Garra_abierta = 180;
        Garra_cerrada = 90;

    }


    public void loop() {
        if (gamepad1.a){
        Servo.setPosition(Garra_abierta);}

            if (gamepad1.b){
        Servo.setPosition(Garra_cerrada);}

}}