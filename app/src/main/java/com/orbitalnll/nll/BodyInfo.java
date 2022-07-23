package com.orbitalnll.nll;

import static java.lang.Math.atan2;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;

import com.google.mlkit.vision.pose.Pose;
import com.google.mlkit.vision.pose.PoseLandmark;
import com.orbitalnll.nll.camera.GraphicOverlay;

public class BodyInfo {
    private static final String PUSH_UP = "Push Up";
    private static final String SQUAT = " Squat";
    private static final String SIT_UP = "Sit Up";
    private static final String LUNGES = "Lunges";

    private Pose pose;

    public BodyInfo(Pose p) {
        this.pose = p;
    }

    static double getAngle(PoseLandmark firstPoint, PoseLandmark midPoint, PoseLandmark lastPoint) {
        double result =
                Math.toDegrees(
                        atan2(lastPoint.getPosition().y - midPoint.getPosition().y,
                                lastPoint.getPosition().x - midPoint.getPosition().x)
                                - atan2(firstPoint.getPosition().y - midPoint.getPosition().y,
                                firstPoint.getPosition().x - midPoint.getPosition().x));
        result = Math.abs(result); // Angle should never be negative
        if (result > 180) {
            result = (360.0 - result); // Always get the acute representation of the angle
        }
        return result;
    }

    public static void drawEx(GraphicOverlay ov, String ex, Pose p) {
        switch(ex) {
            case PUSH_UP:
                ov.add(new GraphicOverlay.Graphic(ov) {
                    @Override
                    public void draw(Canvas canvas) {
                        Paint tempPaint = new Paint();
                        tempPaint.setColor(Color.WHITE);
                        tempPaint.setTextSize(100f);

                    canvas.drawText("elbow angle: " + String.valueOf((int) getElbowAng(p)),
                            50,
                            1000,
                            tempPaint);
                    }
                });
                break;

            case SQUAT:
                break;

            case SIT_UP:
                break;

            case LUNGES:
                break;

            default:
        }
    }

    public static double getLeftBicepAng(Pose p) {
        if (p != null) {
            if (p.getAllPoseLandmarks().isEmpty()) {
                return 0;}

            return getAngle(p.getPoseLandmark(PoseLandmark.LEFT_SHOULDER),
                    p.getPoseLandmark(PoseLandmark.LEFT_ELBOW),
                    p.getPoseLandmark(PoseLandmark.LEFT_WRIST));
        }
        return 0; // return 0 by default
    }

    public static double getElbowAng(Pose p) {
        if (p != null) {
            if (p.getAllPoseLandmarks().isEmpty()) {
                return 0;}

            return getAngle(p.getPoseLandmark(PoseLandmark.LEFT_ELBOW),
                    p.getPoseLandmark(PoseLandmark.LEFT_SHOULDER),
                    p.getPoseLandmark(PoseLandmark.LEFT_HIP));
        }
        return 0; // return 0 by default
    }
}
