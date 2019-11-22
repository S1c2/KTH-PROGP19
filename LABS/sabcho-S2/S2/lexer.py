import re
from tokens import *
from syntaxfel import Syntaxfel


# regex

tokenRegex= "down|up|color\s|forw\s|back\s|left\s|right\s|rep\s+[1-9]\d*\s+|\.|\d+|#[A-Fa-f0-9]{6}|\s+|\"|%.*\n|."
commentRegex= "%.*\n?"

class Lexer:
    def __init__(self,file):
        self.finalTokenList = []
        self.currentToken = 0
        self.file = file
        self.createTokens()

    def createTokens(self):
        #currently at row 1 in the input file 
        row = 1 
        #remove comments and replace them with newline
        withoutComments = re.sub(commentRegex, '\n', self.file.lower())
        #turn the string into a list of strings (each element is a token)
        tokenList = re.findall(tokenRegex, withoutComments)
        for token in tokenList:
            #add to the rowcount
            if "\n" in token:            row+=token.count("\n")
            #add to the finalTokenList an element of type Token with input (rowNumber, "forw")
            if token[0:4] == "forw":     self.finalTokenList.append(Token(row, "forw"))
            elif token[0:4] == "back":   self.finalTokenList.append(Token(row, "back"))
            elif token == "down":        self.finalTokenList.append(Token(row, "down"))
            elif token == "up":          self.finalTokenList.append(Token(row, "up"))
            elif token[0:4] == "left":   self.finalTokenList.append(Token(row, "left"))
            elif token[0:5] == "right":  self.finalTokenList.append(Token(row, "right"))
            elif token[0:3] == "rep":    self.finalTokenList.append(Token(row, "rep",token))
            elif token == ".":           self.finalTokenList.append(Token(row, "period"))
            elif token == '"':           self.finalTokenList.append(Token(row, "quote",'"'))
            elif token[0:5] == "color":  self.finalTokenList.append(Token(row, "color"))
            elif token[0] == "#":        self.finalTokenList.append(Token(row, "hex", token[0:7]))
            elif token[0] == '\n':        pass
            elif token.isdigit() and int(token[0]) != 0:
                self.finalTokenList.append(Token(row, "digit", int(token)))
            elif token.isspace():        pass
            else:
                self.finalTokenList.append(Token(row, "error"))
                return

    def hasMoreTokens(self):
        #we call this method when we want to see if we are at the last token
        return self.currentToken < len(self.finalTokenList)

    def getCurrentToken(self):
        #we call this method when we want to get the index of the current token
        return self.finalTokenList[self.currentToken]

    def getNextToken(self):
        #we call this token when we want to get the index of the next token
	    res = self.getCurrentToken()
	    self.currentToken += 1
	    return res
