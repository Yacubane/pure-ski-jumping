package pl.cyfrogen.skijumping.game.sound;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.audio.AudioDevice;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.MathUtils;

import java.io.IOException;

import pl.cyfrogen.skijumping.Main;
import pl.cyfrogen.skijumping.jumper.JumperController;
import pl.cyfrogen.skijumping.sound.wavfile.WavFile;
import pl.cyfrogen.skijumping.sound.wavfile.WavFileException;

public class SlidingSound {
    private final AudioDevice audioDevice;
    private final Thread thread;
    private boolean isDisposed;
    private JumperController jumperController;

    public SlidingSound() {
        audioDevice = Gdx.audio.newAudioDevice(44100, true);
        thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileHandle file = Gdx.files.internal("sounds/slide.wav");
                    WavFile wavFile = WavFile.openWavFile(file.read(), file.length());

                    int numChannels = wavFile.getNumChannels();

                    double[] buffer = new double[(int) wavFile.getFramesRemaining() * numChannels];

                    int framesRead;

                    do {
                        framesRead = wavFile.readFrames(buffer, buffer.length / numChannels);
                    } while (framesRead != 0);

                    float[] fBuffer = new float[buffer.length];
                    for (int i = 0; i < buffer.length; i++) {
                        fBuffer[i] = (float) buffer[i];
                    }


                    float position = 0;
                    float speed = 1f;
                    float volume = 1f;


                    float[] actualBuffer = new float[4096];
                    while (!isDisposed) {
                        try {
                            if (jumperController != null && jumperController.isSliding() && jumperController.isOnInRun()) {
                                float jumperSpeed = jumperController.getPhysicObject().getLinearVelocity().len();

                                float percentage = jumperSpeed / 30;
                                if (percentage > 1f) percentage = 1;
                                speed = MathUtils.lerp(0.25f, 0.50f, percentage);
                                volume = MathUtils.lerp(0f, 0.7f, percentage);
                            } else {
                                volume = 0;
                                speed = 1;
                            }
                        } catch (Exception e) {
                            volume = 0;
                            speed = 1;
                            Main.getInstance().getPlatformAPI().logCrash(e);
                        }

                        for (int i = 0; i < actualBuffer.length; i++) {
                            position = (position + speed) % buffer.length;
                            double d1 = buffer[(int) position];

                            double d2 = buffer[(int) position];
                            if (position < (buffer.length - 1))
                                d2 = buffer[(int) (position + 1)];

                            float percentage = position - (int) position;

                            actualBuffer[i] = MathUtils.lerp((float) d1, (float) d2, percentage) * volume;
                        }

                        synchronized (audioDevice) {
                            audioDevice.writeSamples(actualBuffer, 0, actualBuffer.length);
                        }

                    }

                } catch (IOException e) {
                    e.printStackTrace();
                } catch (WavFileException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void setJumperController(JumperController jumperController) {
        this.jumperController = jumperController;
    }

    public void dispose() {
        synchronized (audioDevice) {
            audioDevice.dispose();
        }
    }

    public void setDisposed(boolean b) {
        this.isDisposed = b;
    }

    public void start() {
        thread.start();
    }
}
