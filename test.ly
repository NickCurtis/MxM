\version "2.18.2"
\header {
	title = "Rhythm Trees"
	subtitle = "7 Total"
	composer = "Music ex Machina"
	tagline = "This piece was generated by MxM and engraved by LilyPond."
	copyright = "All Rights Reserved"
}
\score { 
	\new Staff {
		 c'2 c'2
	}
	\header {
		piece = "Rhythm Tree #1"
	}
}\score { 
	\new Staff {
		 \tuplet 3/2 { c'2 c'2 c'2}
	}
	\header {
		piece = "Rhythm Tree #2"
	}
}\score { 
	\new Staff {
		 c'4 c'4 c'4 c'4
	}
	\header {
		piece = "Rhythm Tree #3"
	}
}\score { 
	\new Staff {
		 \tuplet 5/4 { c'4 c'4 c'4 c'4 c'4}
	}
	\header {
		piece = "Rhythm Tree #4"
	}
}\score { 
	\new Staff {
		 \tuplet 3/2 { c'4 c'4 c'4} \tuplet 3/2 { c'4 c'4 c'4}
	}
	\header {
		piece = "Rhythm Tree #5"
	}
}\score { 
	\new Staff {
		 \tuplet 3/2 { c'4 c'4 c'4 c'4 c'4 c'4}
	}
	\header {
		piece = "Rhythm Tree #6"
	}
}\score { 
	\new Staff {
		 c'2 c'4 c'8 c'16 c'16
	}
	\header {
		piece = "Rhythm Tree #7"
	}
}