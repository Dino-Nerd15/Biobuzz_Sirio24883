package org.firstinspires.ftc.teamcode;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad1;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.gamepad2;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;


@TeleOp (name = "Biobuzz")
public class Biobuzz extends OpMode {

    //Definir Chassis
    DcMotor RF;//Control Puerto 2
    DcMotor RB;//Control Puerto 3
    DcMotor LF;//Expansion Puerto 1
    DcMotor LB;//Expansion Puerto 0

    //Definir mecanismos
    DcMotor shooterPollen;
    DcMotor shooterNectar;
    DcMotorEx intake;

    //Definir Variables
    double drive;
    double strafe;
    double turn;

    @Override
    public void init() {

        //Chasis
        RF = hardwareMap.get(DcMotor.class, "RF");
        RB = hardwareMap.get(DcMotor.class, "RB");
        LF = hardwareMap.get(DcMotor.class, "LF");
        LB = hardwareMap.get(DcMotor.class, "LB");


        //Mapear mecanismos
        shooterPollen=hardwareMap.get(DcMotor.class,"shooterPollen");
        shooterNectar=hardwareMap.get(DcMotor.class,"shooterNectar");
        intake=hardwareMap.get(DcMotorEx.class,"intake");


        //Invertir izq
        LF.setDirection(DcMotor.Direction.REVERSE);
        LB.setDirection(DcMotor.Direction.REVERSE);

        shooterPollen.setDirection(DcMotorSimple.Direction.REVERSE);


        //Brake
        //Brake Freno
        RF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        RB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LF.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        LB.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        intake.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        LF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        LB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RF.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        RB.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        LF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        LB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RF.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RB.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        //shooterPollen.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        shooterNectar.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);

        //shooterPollen.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterNectar.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    }

    @Override
    public void loop(){

        //variables a gamepad,
        drive = -gamepad1.left_stick_y;
        strafe = -gamepad1.left_stick_x;
        turn = gamepad1.right_stick_x;

        //Motor power
        RF.setPower(drive-strafe-turn);
        RB.setPower(drive+strafe-turn);
        LF.setPower(-drive+strafe-turn);//Original=drive+strafe-turn
        LB.setPower(drive+strafe+turn);//Original=drive-strafe+turn

        //Mecanismos
        //Pollen
        if(gamepad2.b){
            shooterPollen.setPower(0.8);}
        else {
            shooterPollen.setPower(0);}

        //Nectar
        if(gamepad2.y){
            shooterNectar.setPower(1);}
        else {
            shooterNectar.setPower(0);}

        //intake
        if(gamepad2.dpad_up){
            intake.setVelocity(312*26);}
        else {
            intake.setPower(0);}

        //intake out
        if (gamepad2.dpad_down){
            intake.setPower(-1);}
        else {
            intake.setPower(0);}}}

