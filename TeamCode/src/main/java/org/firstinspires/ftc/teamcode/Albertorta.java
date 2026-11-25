package org.firstinspires.ftc.teamcode;

import static java.lang.Thread.sleep;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

//IMU, Sensores, DcMotorEx, setVel
//Biobuzz

@TeleOp(name="Albertorta")
public class Albertorta extends OpMode {

    //Definir Chassis
    DcMotorEx RF;//Control Puerto 2
    DcMotorEx RB;//Control Puerto 3
    DcMotorEx LF;//Expansion Puerto 1
    DcMotorEx LB;//Expansion Puerto 0

    //Definir mecanismos
    DcMotorEx shooterPollen;
    DcMotorEx shooterNectar;
    DcMotorEx intake;

    double RPM_POLLEN = 3500;
    double RPM_NECTAR = 5500;

    double kP = 10.0;
    double TOLERANCIA_TICKS = 25;

    final double TICKS_POR_REV = 28;
    final double RPM_MAX_MOTOR = 6000;

    ElapsedTime shootReset=new ElapsedTime();

    //Definir Variables

    //Time
    private ElapsedTime runTime = new ElapsedTime();

    //Variables Chassis
    double drive,
            strafe,
            turn;
    double Breaker = 0.75;

    //Definir IMU y parámetros
    private IMU imu; //Inertial Measurement Unit o Unidad de Medición Inercial
    private IMU.Parameters MyParameters;

    double InitYaw,//Dirección inicial
            Angle,//Ángulo inicial InitYaw
            AjustedYaw,//Ángulo de diferencia entre actual a InitYaw
            ZerodYaw,//Dirección original
            heading,//Orientación actual
            adjustedDrive,//Drive-IMU
            adjustedStrafe;//Strafe-IMU


    //ticks por Revolución
    private double ticksPerRevfromRF;
    private double ticksPerRevfromRB;
    private double ticksPerRevfromLF;
    private double ticksPerRevfromLB;

    double rpmATicks(double rpm) {
        return rpm * TICKS_POR_REV / 60.0;
    }
    void rpmMotor(DcMotorEx motor){
        double maxTicks = rpmATicks(RPM_MAX_MOTOR);
        motor.setVelocityPIDFCoefficients(kP, 0 , 0, 32767.0/maxTicks);
    }

   // public double getMotorRevsfromRF(){return RF.getCurrentPosition()/ticksPerRevfromRF;
  //  }
    //public double getMotorRevsfromRB(){return RB.getCurrentPosition()/ticksPerRevfromRB;
    //}

    //public double getMotorRevsfromLF(){return LF.getCurrentPosition()/ticksPerRevfromLF;
   // }
    //public double getMotorRevsfromLB(){return LB.getCurrentPosition()/ticksPerRevfromLB;
   // }


    //ticks por minuto TPM
    //double ticksPerMinfromRF = RF.getVelocity()*60;
   // double ticksPerMinfromRB = RB.getVelocity()*60;

    //double ticksPerMinfromLF = LF.getVelocity()*60;
   // double ticksPerMinfromLB = LB.getVelocity()*60;



    //RPM
   // public double getRevsfromRF(){return (ticksPerMinfromRF/ticksPerRevfromRF);}
   // public double getRevsfromRB() {return (ticksPerMinfromRB / ticksPerRevfromRB);}

   // public double getRevsfromLF(){return (ticksPerMinfromLF/ticksPerRevfromLF);}
  //  public double getRevsfromLB() {return (ticksPerMinfromLB / ticksPerRevfromLB);}




    @Override
    public void init() {

        //Mapear Chassis
        RF = hardwareMap.get(DcMotorEx.class, "RF");
        RB = hardwareMap.get(DcMotorEx.class, "RB");
        LF = hardwareMap.get(DcMotorEx.class, "LF");
        LB = hardwareMap.get(DcMotorEx.class, "LB");


        //Mapear mecanismos
        shooterPollen=hardwareMap.get(DcMotorEx.class,"shooterPollen");
        shooterNectar=hardwareMap.get(DcMotorEx.class,"shooterNectar");
        intake=hardwareMap.get(DcMotorEx.class,"intake");

        //Invertir izq
        LF.setDirection(DcMotor.Direction.REVERSE);
        LB.setDirection(DcMotor.Direction.REVERSE);

        shooterPollen.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterPollen.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        shooterNectar.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);



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

        //IMU mapear y direccionar
        imu = hardwareMap.get(IMU.class, "imu");//mapear
        MyParameters = new IMU.Parameters(new RevHubOrientationOnRobot(//direccionar
                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
                RevHubOrientationOnRobot.UsbFacingDirection.UP)
        );
        imu.initialize(MyParameters);//Establece que la posición actual es la posición incial frontal

        InitYaw = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);





            //Motor TPR
          //  ticksPerRevfromRF= RF.getMotorType().getTicksPerRev();
           // ticksPerRevfromRB= RB.getMotorType().getTicksPerRev();

     //   ticksPerRevfromLF= LF.getMotorType().getTicksPerRev();
      //  ticksPerRevfromLB= LB.getMotorType().getTicksPerRev();

    }

    @Override
    public void loop(){

        //variables a gamepad,
        drive = gamepad1.left_stick_y;
        strafe = gamepad1.left_stick_x;
        turn = gamepad1.right_stick_x;

        Angle=imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        ZerodYaw=-InitYaw-imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);

        double Theta;
        double Real_Theta;
        double Power;

        Theta = Math.atan2(drive, strafe)*180/Math.PI;
        Real_Theta = (360-ZerodYaw)+Theta;
        Power = Math.hypot(drive, strafe);

        double Sin,
                Cos,
                MaxCosSin;

        Sin = Math.sin((Real_Theta*Math.PI/180)-(Math.PI/4));
        Cos = Math.cos((Real_Theta*Math.PI/180)-(Math.PI/4));
        MaxCosSin = Math.max(Math.abs(Sin), Math.abs(Cos));

        double RFpwr = Range.clip((Power * Sin/MaxCosSin - turn), -1,1);
        double RBpwr = Range.clip((Power * Cos/MaxCosSin - turn), -1,1);
        double LFpwr = Range.clip(-(Power * Sin/MaxCosSin + turn), -1,1);
        double LBpwr = Range.clip((Power * Cos/MaxCosSin + turn), -1,1);


        if (gamepad1.a){
            InitYaw = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        }

        if (gamepad1.right_bumper) {
            RF.setPower(RFpwr);
            RB.setPower(RBpwr);
            LF.setPower(LFpwr);
            LB.setPower(LBpwr);
        }
        else {
            RF.setPower((RFpwr) * Breaker);
            RB.setPower((RBpwr) * Breaker);
            LF.setPower((LFpwr) * Breaker);
            LB.setPower((LBpwr) * Breaker);
        }


        //Mecanismos
        //Pollen


        double velPollen = (RPM_POLLEN);
        double velNectar = (RPM_NECTAR);

        if(gamepad2.b){
            //shootReset.reset();
            //shooterPollen.setPower(-0.5);

            //if (shootReset.seconds()>=1){
            shooterPollen.setVelocity(velPollen);
            //}
        }
        else {
            shooterPollen.setPower(0);}



        //Nectar
        if(gamepad2.y){
            shooterNectar.setVelocity(velNectar);}
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
            intake.setPower(0);}


      //  telemetry.addData("RF Revs: ", getMotorRevsfromRF());
      //  telemetry.addData("RB Revs: ", getMotorRevsfromRB());
      //  telemetry.addData("LF Revs: ", getMotorRevsfromLF());
       // telemetry.addData("LB Revs: ", getMotorRevsfromLB());

        //telemetry.addData("RF Revs/Minute:",this::getRevsfromRF);
       // telemetry.addData("RB Revs/Minute:",this::getRevsfromRB);
       // telemetry.addData("LF Revs/Minute:",this::getRevsfromLF);
        //telemetry.addData("LB Revs/Minute:",this::getRevsfromLB);
        telemetry.addData("Adjusted Yaw", AjustedYaw);
        telemetry.addData("yaw", imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS));
        telemetry.addData("InitYaw", InitYaw);

        telemetry.update();


    }
}

