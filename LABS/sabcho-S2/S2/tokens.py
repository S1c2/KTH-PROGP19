
class Token(object):

    def __init__(self,row,t_type,value=None):
        self.value= value
        self.row = row
        self.t_type = t_type
