class Tree(object):
    def __init__(self, token=None,value=None):
        self.token = token
        self.value = value
        self.next = None
        self.down = None