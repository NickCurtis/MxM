import passage.events.Note;
import base.relative.HarmonyClass;
import base.relative.PitchClass;
import base.Harmony;
import base.Pitch;
import base.time.Count;

import java.util.ArrayList;

/**
 * Created by celenp on 5/16/2017.
 */
public class LilyPondParser {

    public static void main(String[] args) {
        /*

        System.out.println(parseNoteLength("4"));
        System.out.println(parseNoteLength("4."));
        System.out.println(parseNoteLength("8"));
        System.out.println(parseNoteLength("8."));
        System.out.println(parseNoteLength("16"));
        */

        /*
        */


        ArrayList<Note> notes = parseNotes("c4 c'4 cis,128. d\\breve des'''8. e,1 eisis,,\\longa f,,,,4");
        for(Note note : notes) {
            System.out.println(note.getStart() + "\t\t" + note.getSound() + " " + note.getDuration() + "   ");
        }

    }


    private static Harmony parseChord(String chordString) {
        String[] tokens = chordString.split(":");
        if (tokens.length != 2) {
            throw new Error("LILY POND PARSER:\t ChordNote string \"" + chordString + "\" improperly formatted.");
        }
        PitchClass pitch        = parsePitchClass(tokens[0]);
        HarmonyClass chordClass   = parseChordClass(tokens[1]);

        return new Harmony(pitch,chordClass);
    }

    private static ArrayList<Note> parseNotes(String noteString) {
        ArrayList<Note> toReturn = new ArrayList<>();
        String[] notes = noteString.split(" ");

        Count curTime = Count.ZERO;
        for(String noteToken : notes) {
            Note note = parseNote(noteToken, curTime);
            toReturn.add(note);
            // Keep track of the base.time
            curTime = curTime.plus(note.getDuration());
        }
        return toReturn;
    }

    private static Note<Pitch> parseNote(String noteToken, Count curTime) {
        int divider = 0;
        for(divider = 0; divider < noteToken.length(); divider++ ) {
            if(noteToken.charAt(divider) == '\\' || Character.isDigit(noteToken.charAt(divider))) {
                break;
            }
        }
        Pitch pitch = parsePitch(noteToken.substring(0,divider));
        Count length = parseNoteLength(noteToken.substring(divider), Count.ONE);
        return new Note<Pitch>(curTime,length,pitch);
    }

    private static Pitch parsePitch(String pitchToken) {
        int octave = 4;
        while(pitchToken.charAt(pitchToken.length()-1) == '\'' ||
                pitchToken.charAt(pitchToken.length()-1) == ',' ) {
            if(pitchToken.charAt(pitchToken.length()-1) == '\'') {
                pitchToken = pitchToken.substring(0,pitchToken.length()-1);
                octave++;
            }
            else if(pitchToken.charAt(pitchToken.length()-1) == ',') {
                pitchToken = pitchToken.substring(0,pitchToken.length()-1);
                octave--;
            }
        }
        PitchClass pitchClass = parsePitchClass(pitchToken);
        return Pitch.get(pitchClass,octave);
    }

    private static PitchClass parsePitchClass(String pitchClassToken) {
        int value;
        switch(pitchClassToken.charAt(0)) {
            case 'c':   value = 0; break;
            case 'd':   value = 2; break;
            case 'e':   value = 4; break;
            case 'f':   value = 5; break;
            case 'g':   value = 7; break;
            case 'a':   value = 9; break;
            case 'b':   value = 11; break;
            default:    throw new Error("LilyPondParser:\tPitch class \""+ pitchClassToken +"\" cannot be parsed");
        }
        pitchClassToken = pitchClassToken.substring(1);
        // Keep cutting the string until there's nothing left
        while(pitchClassToken.length() >= 1) {
            // If the the next bit of the token matches the sharp token
            if(pitchClassToken.substring(0,2).equals("is")) {
                pitchClassToken = pitchClassToken.substring(2);
                value++;
            }
            // If the the next bit of the token matches the sharp token
            else if(pitchClassToken.substring(0,2).equals("es")) {
                pitchClassToken = pitchClassToken.substring(2);
                value--;
            }
            else throw new Error("LilyPondParser:\tAccidental \""+ pitchClassToken +"\" cannot be parsed");
        }
        return PitchClass.getInstance(value);
    }

    private static HarmonyClass parseChordClass(String chordClassString) {
        switch (chordClassString) {
            case "":
            case "5":           return HarmonyClass.MAJOR;
            case "m":           return HarmonyClass.MINOR;
            case "aug":         return HarmonyClass.AUGMENTED;
            case "dim":         return HarmonyClass.DIMINISHED;
            case "7":           return HarmonyClass.DOMINANT_SEVENTH;
            case "maj7":
            case "maj":         return HarmonyClass.MAJOR_SEVENTH;
            case "m7":          return HarmonyClass.MINOR_SEVENTH;
            case "dim7":        return HarmonyClass.DIMINISHED_SEVENTH;
            case "aug7":        return HarmonyClass.AUGMENTED_SEVENTH;
            case "m7.5-":       return HarmonyClass.HALF_DIMINISHED_SEVENTH;
            case "m7+":         return HarmonyClass.MINOR_MAJOR_SEVENTH;
            case "6":           return HarmonyClass.MAJOR_SIXTH;
            case "m6":          return HarmonyClass.MINOR_SIXTH;
            case "9":           return HarmonyClass.DOMINANT_NINTH;
            case "maj9":        return HarmonyClass.MAJOR_NINTH;
            case "m9":          return HarmonyClass.MINOR_NINTH;
            case "11":          return HarmonyClass.DOMINANT_ELEVENTH;
            case "maj11":       return HarmonyClass.MAJOR_ELEVENTH;
            case "m11":         return HarmonyClass.MINOR_ELEVENTH;
            case "13":
            case "13.11":       return HarmonyClass.DOMINANT_THIRTEENTH;
            case "maj13.11":    return HarmonyClass.MAJOR_THIRTEENTH;
            case "m13.11":      return HarmonyClass.MINOR_THIRTEENTH;
            case "sus2":        return HarmonyClass.SUSPENDED_SECOND;
            case "sus4":        return HarmonyClass.SUSPENDED_FOURTH;
            case "1.5":
            case "1.8":         return HarmonyClass.POWER_CHORD;
            default:
                String[] tokens = chordClassString.split(".");
                if (tokens.length != 2) {
                    throw new Error("LILY POND PARSER:\t ChordNote class string \"" + chordClassString + "\" improperly formatted.");
                }
        }
        return null;
    }

    private static Count parseNoteLength(String noteLengthString, Count tupletModifier) {
        int numerator = 1;
        int denominator = 1;

        if(noteLengthString.contains("128"))            denominator = 128;
        else if(noteLengthString.contains("64"))        denominator = 64;
        else if(noteLengthString.contains("32"))        denominator = 32;
        else if(noteLengthString.contains("16"))        denominator = 16;
        else if(noteLengthString.contains("8"))         denominator = 8;
        else if(noteLengthString.contains("4"))         denominator = 4;
        else if(noteLengthString.contains("2"))         denominator = 2;
        else if(noteLengthString.contains("1"))         denominator = 1;
        else if(noteLengthString.contains("\\longa"))   numerator = 2;
        else if(noteLengthString.contains("\\breve"))   numerator = 4;
        else throw new Error("LilyPondParser:\tUnknown note length \"" + noteLengthString + "\"");

        for(int i = 0; i < noteLengthString.length(); i++ ) {
            if(noteLengthString.charAt(i) == '.') {
                numerator   *= 2;
                denominator *= 2;
                numerator   += 1;
            }
        }

        // If we're in a tuplet, then modify the actual length accordingly
        numerator *= tupletModifier.getNumerator();
        denominator *= tupletModifier.getDenominator();

        return new Count(numerator,denominator);
    }
}