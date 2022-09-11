package erchat;

import java.io.File;
import java.io.IOException;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineListener;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;


public class AudioPlayer implements LineListener {
	boolean playCompleted;
	Clip audioClip;

	public void play(String audioFilePath) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
		File audioFile = new File(audioFilePath);

		AudioInputStream audioStream = AudioSystem.getAudioInputStream(audioFile);
		AudioFormat format = audioStream.getFormat();
		DataLine.Info info = new DataLine.Info(Clip.class, format);

		audioClip = (Clip) AudioSystem.getLine(info);
		audioClip.addLineListener(this);
		audioClip.open(audioStream);
		audioClip.start();
		
		playCompleted = false;	
		while (!playCompleted) {
			// wait for the playback completes
			try {
				System.out.println("sleep");
				Thread.sleep(1000);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
				audioClip.stop();
				break;
				
			}
		}
		audioClip.close();
	}

	
	/**
	 * Listens to the audio line events to know when the playback completes.
	 */
	@Override
	public void update(LineEvent event) {
		LineEvent.Type type = event.getType();
		if (type == LineEvent.Type.STOP) {
			playCompleted = true;
		}
	}
}

