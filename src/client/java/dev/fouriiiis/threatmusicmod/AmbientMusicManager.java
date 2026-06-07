package dev.fouriiiis.threatmusicmod;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.sound.MovingSoundInstance;
import net.minecraft.client.sound.SoundInstance;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import java.util.ArrayList;
import java.util.List;

public class AmbientMusicManager {

    private static List<String> availableAmbientTracks = new ArrayList<>();

    /**
     * Returns a list of all available ambient tracks.
     *
     * @return A list of ambient track names.
     */
    public static List<String> getAvailableAmbient() {
        return new ArrayList<>(availableAmbientTracks);
    }

    /**
     * Plays the specified ambient sound at the given volume.
     *
     * @param soundKey    The key of the sound to play.
     * @param targetVolume The volume at which to play the sound.
     */
    public static void playAmbientSound(String soundKey, float targetVolume) {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null && ModSounds.soundEvents.containsKey(soundKey)) {
            SoundEvent soundEvent = ModSounds.soundEvents.get(soundKey);
            SoundInstance soundInstance = new AmbientSoundInstance(soundEvent, targetVolume);
            client.getSoundManager().play(soundInstance);
        } else {
            System.out.println("Sound key not found: " + soundKey);
        }
    }

    /**
     * Stops all currently playing ambient sounds.
     */
    public static void stopAllAmbient() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client != null) {
            client.getSoundManager().stopSounds(null, SoundCategory.MUSIC);
        }
    }

    private static class AmbientSoundInstance extends MovingSoundInstance {
        AmbientSoundInstance(SoundEvent sound, float volume) {
            super(sound, SoundCategory.MUSIC, SoundInstance.createRandom());
            this.repeat = false;
            this.repeatDelay = 0;
            this.relative = true;
            this.volume = volume;
        }

        @Override
        public void tick() {
            if (this.isDone()) {
                return;
            }
        }
    }

    /**
     * Adds a new ambient track to the list of available tracks.
     *
     * @param trackName The name of the track to add.
     */
    public static void registerAmbientTrack(String trackName) {
        if (!availableAmbientTracks.contains(trackName)) {
            availableAmbientTracks.add(trackName);
        }
    }
}
