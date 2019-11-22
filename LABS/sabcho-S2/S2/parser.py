
from syntaxfel import Syntaxfel
from lexer import *
from tree import Tree


def readProgram(lexer):
    tree = readInstruction(lexer)
    if lexer.hasMoreTokens() and lexer.getCurrentToken().t_type == 'quote':
        return tree
    if not lexer.hasMoreTokens():
        return tree
    else:
        tree.next=readProgram(lexer)
        return tree

def readInstruction(lexer):
    if lexer.hasMoreTokens():
        if lexer.hasMoreTokens() and lexer.getCurrentToken().t_type == 'rep':
            tree = readRep(lexer)
            return tree
        elif lexer.hasMoreTokens() and \
            lexer.getCurrentToken().t_type == 'down' or lexer.getCurrentToken().t_type == 'up':
            tree = readPen(lexer)
            return tree
        elif lexer.hasMoreTokens() and \
            lexer.getCurrentToken().t_type == 'forw' or lexer.getCurrentToken().t_type == 'back' or lexer.getCurrentToken().t_type == 'left' or lexer.getCurrentToken().t_type == 'right':
            tree = readMovement(lexer)
            return tree
        elif lexer.hasMoreTokens() and lexer.getCurrentToken().t_type == 'color':
            tree = readColor(lexer)
            return tree
        else:
            raise Syntaxfel('Syntaxfel på rad ')
    return

def readRep(lexer):
    tree=Tree()
    tree.token = lexer.getCurrentToken().t_type
    tree.value = getRepNumber(lexer.getCurrentToken().value)      
    lexer.getNextToken()
    if lexer.hasMoreTokens() and lexer.getCurrentToken().t_type == 'quote':
        lexer.getNextToken()
        tree.down = readProgram(lexer)
        if lexer.hasMoreTokens() and lexer.getCurrentToken().t_type == 'quote':
            lexer.getNextToken()
            return tree
        raise Syntaxfel('Syntaxfel på rad ')
    else:
        tree.down = readInstruction(lexer)
        return tree
    raise Syntaxfel('Syntaxfel på rad ')

def getRepNumber(value):
    numberstring=''
    for x in value:
        if x.isdigit():
            numberstring += x
    reps=int(numberstring)
    return reps

def readPen(lexer):
    tree=Tree()
    tree.token = lexer.getCurrentToken().t_type
    lexer.getNextToken()
    if lexer.hasMoreTokens() and lexer.getCurrentToken().t_type  == 'period':
        lexer.getNextToken()
        return tree
    else:
        raise Syntaxfel('Syntaxfel på rad ')


def readMovement(lexer):
    tree=Tree()
    tree.token = lexer.getCurrentToken().t_type
    lexer.getNextToken()
    if lexer.hasMoreTokens() and lexer.getCurrentToken().t_type  == 'digit':
        tree.value = lexer.getCurrentToken().value        
        lexer.getNextToken()
        if lexer.hasMoreTokens() and lexer.getCurrentToken().t_type  == 'period':
            lexer.getNextToken()
            return tree
    raise Syntaxfel('Syntaxfel på rad ')

def readColor(lexer):
    tree=Tree()
    tree.token = lexer.getCurrentToken().t_type
    lexer.getNextToken()                                   
    if lexer.hasMoreTokens() and lexer.getCurrentToken().t_type == 'hex':
        tree.value = lexer.getCurrentToken().value 
        lexer.getNextToken() 
        if lexer.hasMoreTokens() and lexer.getCurrentToken().t_type  == 'period':
            lexer.getNextToken()
            return tree
    raise Syntaxfel('Syntaxfel på rad ')


