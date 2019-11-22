Grammatik av Sabrina Chowdhury, sabcho


readProgram --> (readInstruction)* | quote 
readInstruction --> readRep | readPen | readMovement | readColor 
readRep --> REP QUOTE (readProgram) QUOTE | REP (readInstruction)
readPen --> (UP | DOWN) PERIOD 
readMovement --> (LEFT | RIGHT | BACK | FORW) DIGIT PERIOD
readColor --> COLOR HEX PERIOD 


LEFT --> "left\s"
RIGHT --> "right\s"
DOWN --> "down"
UP --> "up"
COLOR --> "color\s"
FORW --> "forw\s"
BACK --> "back\s"
QUOTE --> '"'
REP --> "rep\s"
HEX --> "#[A-Fa-f0-9]{6}"
DIGIT --> "[0-9]+"
PERIOD --> "."