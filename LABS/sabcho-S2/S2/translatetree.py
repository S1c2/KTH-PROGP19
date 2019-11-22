from math import *
angle=0
pendown=False
output=['#0000FF', 0 , 0 , 0 , 0 ]

def printCalc(output):
    if pendown == True:
        print(output[0], end='\t')
        for i in range(1,len(output)):
            print('%.4f' % float(output[i]), end='\t')
        print()

def treeCalc(node):
    output[1]=output[3]
    output[2]=output[4]
    global angle
    global pendown
    if node == None:
        return 0
    elif node != None:
        if node.token == 'down':
            pendown = True
        elif node.token == 'up':
            pendown = False
        elif node.down != None:
            for x in range(node.value):
                treeCalc(node.down)
        elif node.token == "forw":
            output[3] = output[3] + node.value * cos((pi * angle/180))
            output[4] = output[4] + node.value * sin((pi * angle)/180)
            printCalc(output)
        elif node.token == "back":
            output[3] = output[3] - node.value * cos((pi * angle)/180)
            output[4] = output[4] - node.value * sin((pi * angle)/180)
            printCalc(output)
        elif node.token == "left":  angle += node.value
        elif node.token == "right": angle -= node.value
        elif node.token == "color": output[0] = node.value

    treeCalc(node.next)
