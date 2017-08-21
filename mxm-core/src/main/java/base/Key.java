package base;

import base.relative.KeyClass;
import base.relative.PitchClass;

import java.util.Set;

/**
 * Created by celenp on 5/13/2017.
 */
public class Key extends Harmony {
    PitchClass tonic;
    KeyClass keyClass;

    public Key(Set<PitchClass> pitchClasses) {
        super(pitchClasses);
    }
}
