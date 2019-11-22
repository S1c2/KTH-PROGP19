from syntaxfel import Syntaxfel
from lexer import *
from parser import readProgram
from translatetree import *
import sys
from sys import stdin

sys.setrecursionlimit(10000000)

def main():
    #read the input file
    file = stdin.read()
    try:
        #call the Lexer class with the input file as parameter, and get a list of tokens
        lexer=Lexer(file)
        #call the readProgram method with the list of tokens as parameter
        tree=readProgram(lexer)
        if lexer.hasMoreTokens() and lexer.getCurrentToken().t_type == 'quote':
            tree=readProgram(lexer)
        result=treeCalc(tree)
        return result
    except Syntaxfel as fel:
        if lexer.hasMoreTokens() == False:
            print( str(fel) + str(lexer.finalTokenList[-1].row))
        else:
            print( str(fel) + str(lexer.getCurrentToken().row))

if __name__ == '__main__':
    main()

