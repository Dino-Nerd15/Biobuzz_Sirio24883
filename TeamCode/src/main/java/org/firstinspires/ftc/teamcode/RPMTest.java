// Importar paquetes de librerías de códigos
package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.configuration.typecontainers.MotorConfigurationType;

//Código
@TeleOp (name="RPMTest")
public class RPMTest extends OpMode {


    //Declarar motores
    DcMotorEx Pwr;
    DcMotorEx RPM;

    //Revoluciones esperadas por el motor
    double Revsexpected = 5000;

    //ticks por Revolución
    private double ticksPerRevfromPwr;
    private double ticksPerRevfromRPM;

    //ticks por minuto TPM
    double ticksPerMinfromPwr = Pwr.getVelocity()*60;
    double ticksPerMinfromRPM = RPM.getVelocity()*60;

    //Fail counter
    public double getMotorRevsfromPwr(){
        return Pwr.getCurrentPosition()/ticksPerRevfromPwr;
    }
    public double getMotorRevsfromRPM(){
        return RPM.getCurrentPosition()/ticksPerRevfromRPM;
    }

    //RPM
    public double getRevsfromPwr(){return (ticksPerMinfromPwr/ticksPerRevfromPwr);}
    public double getRevsfromRPM(){
        return (ticksPerMinfromRPM/ticksPerRevfromRPM);
    }

    @Override
    public void init() {

        //Map
        Pwr = hardwareMap.get(DcMotorEx.class, "Pwr");
        RPM = hardwareMap.get(DcMotorEx.class, "RPM");

        //Encoder
        Pwr.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        RPM.setMode(DcMotorEx.RunMode.RUN_USING_ENCODER);

        //Motor TPR
        ticksPerRevfromPwr= Pwr.getMotorType().getTicksPerRev();
        ticksPerRevfromRPM= RPM.getMotorType().getTicksPerRev();

    }

    @Override
    public void loop() {


        if (gamepad1.a){
            Pwr.setPower (1);

        }

        if (gamepad1.b){
            Pwr.setPower (0);
        }


        if (gamepad1.x){
            RPM.setVelocity((Revsexpected*ticksPerRevfromRPM)/60);
        }

        if (gamepad1.y){
            RPM.setVelocity(0);
        }

        telemetry.addData("Pwr Revs: ", getMotorRevsfromPwr());
        telemetry.addData("RPM Revs: ", getMotorRevsfromRPM());
        telemetry.addData("Pwr Revs/Minute:",this::getRevsfromPwr);
        telemetry.addData("RPM Revs/Minute:",this::getRevsfromRPM);
        telemetry.update();
    }
}



